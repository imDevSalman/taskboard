package com.example.feature.taskboard.presentation.ui.screens.tasks_list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.core.common.ResultState
import com.example.core.common.domain.model.Task
import com.example.feature.taskboard.presentation.ui.component.EmptyView
import com.example.feature.taskboard.presentation.ui.component.ErrorView
import com.example.feature.taskboard.presentation.ui.component.LoadingScreen
import com.example.feature.taskboard.presentation.ui.component.TaskItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: TaskListViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val syncState by viewModel.syncState.collectAsStateWithLifecycle()

    Scaffold(topBar = {
        TopAppBar(
            title = { Text("TaskBoard") },
            actions = {
                IconButton(
                    onClick = { viewModel.syncTasks() },
                    enabled = syncState !is SyncState.Syncing
                ) {
                    if (syncState is SyncState.Syncing) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(Icons.Default.Refresh, "Sync")
                    }
                }
            }
        )
    }, floatingActionButton = {
        FloatingActionButton(onClick = { navController.navigate("edit/-1") }) {
            Icon(Icons.Default.Add, "")
        }
    }) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            when (state) {
                is ResultState.Loading -> LoadingScreen()
                is ResultState.Empty -> EmptyView()
                is ResultState.Success -> {
                    val tasks = (state as ResultState.Success<List<Task>>).data
                    if (tasks.isEmpty()) EmptyView()
                    else {
                        TasksList(
                            tasks,
                            onToggle = viewModel::updateTask,
                            onEdit = { task -> navController.navigate("edit/${task.id}") },
                            onDelete = viewModel::deleteTask
                        )
                    }
                }

                is ResultState.Error -> {
                    val error = (state as ResultState.Error).message ?: "Something went wrong"
                    ErrorView(error) { viewModel.syncTasks() }
                }
            }
            AnimatedVisibility(
                visible = syncState is SyncState.Success,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                Snackbar(
                    modifier = Modifier.align(Alignment.BottomCenter)
                ) {
                    Text("Sync successful!")
                }
            }
        }
    }
}

@Composable
fun TasksList(
    tasks: List<Task>,
    onToggle: (Task) -> Unit,
    onDelete: (Task) -> Unit,
    onEdit: (Task) -> Unit
) {
    if (tasks.isEmpty()) {
        EmptyView()
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(tasks, key = { it.id }) { task ->
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    TaskItem(
                        task = task,
                        onToggle = { onToggle(task) },
                        onDelete = { onDelete(task) },
                        onEdit = { onEdit(task) }
                    )
                }
            }
        }
    }
}
