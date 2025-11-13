package com.example.core.common.domain.repository

import com.example.core.common.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun getAllTasks(): Flow<List<Task>>

    suspend fun getTaskById(id: Long): Task?
    suspend fun insertAll(tasks: List<Task>)
    suspend fun insert(task: Task)
    suspend fun update(task: Task)
    suspend fun delete(id: Long)
    suspend fun syncWithNetwork()
}