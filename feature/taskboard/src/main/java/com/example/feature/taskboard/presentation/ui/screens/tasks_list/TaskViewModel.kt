package com.example.feature.taskboard.presentation.ui.screens.tasks_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.common.ResultState
import com.example.core.common.domain.model.Task
import com.example.core.common.domain.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {

    private val _syncState = MutableStateFlow<SyncState>(SyncState.Idle)
    val syncState: StateFlow<SyncState> = _syncState.asStateFlow()

    // Directly derive UI state from the Flow<List<Task>>
    val uiState: StateFlow<ResultState<List<Task>>> =
        repository.getAllTasks() // <-- This should return Flow<List<Task>>
            .map<List<Task>, ResultState<List<Task>>> { tasks ->
                when {
                    tasks.isEmpty() -> ResultState.Empty
                    else -> {
                        println(tasks)
                        ResultState.Success(tasks)
                    }
                }
            }
            .onStart { emit(ResultState.Loading) }
            .catch { emit(ResultState.Error(it.message ?: "Unknown error")) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = ResultState.Loading
            )

    fun updateTask(task: Task) {
        viewModelScope.launch {
            repository.update(task.copy(isDone = !task.isDone))
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repository.delete(task.id)
        }
    }

    fun syncTasks() {
        viewModelScope.launch {
            _syncState.value = SyncState.Syncing
            try {
                repository.syncWithNetwork()
                _syncState.value = SyncState.Success
            } catch (e: Exception) {
                _syncState.value = SyncState.Error(e.message ?: "Sync failed")
            } finally {
                // Let the success Snackbar show before resetting to Idle
                delay(1000)
                _syncState.value = SyncState.Idle
            }
        }
    }
}

sealed class SyncState {
    object Idle : SyncState()
    object Syncing : SyncState()
    object Success : SyncState()
    data class Error(val message: String) : SyncState()
}
