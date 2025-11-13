package com.example.feature.taskboard.presentation.ui.screens.add_edit_task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.common.domain.model.Task
import com.example.core.common.domain.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditTaskViewModel @Inject
constructor(private val repository: TaskRepository) : ViewModel() {

    private val _task = MutableStateFlow<Task?>(null)
    val task: StateFlow<Task?> = _task.asStateFlow()

    fun loadTask(taskId: Long) {
        if (taskId == -1L) return
        viewModelScope.launch {
            _task.value = repository.getTaskById(taskId)
        }
    }

    fun saveTask(taskId: Long, title: String, description: String) {
        viewModelScope.launch {
            if (taskId == -1L) {
                repository.insert(Task(title = title, description = description))
            } else {
                val existing = repository.getTaskById(taskId)
                existing?.let {
                    repository.update(
                        it.copy(
                            title = title,
                            description = description,
                            updatedAt = System.currentTimeMillis()
                        )
                    )
                }
            }
        }
    }
}