package com.example.feature.taskboard.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.feature.taskboard.presentation.ui.screens.add_edit_task.AddEditTaskScreen
import com.example.feature.taskboard.presentation.ui.screens.tasks_list.TaskListScreen

@Composable
fun TaskNavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(navController = navController, startDestination = "list") {
        composable("list") {
            TaskListScreen(navController)
        }
        composable(
            "edit/{taskId}",
            arguments = listOf(navArgument("taskId") { type = NavType.LongType })
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getLong("taskId") ?: -1L
            AddEditTaskScreen(taskId, navController)
        }
    }
}