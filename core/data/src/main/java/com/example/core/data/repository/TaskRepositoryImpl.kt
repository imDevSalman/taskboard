package com.example.core.data.repository

import com.example.core.common.domain.model.Task
import com.example.core.common.domain.repository.TaskRepository
import com.example.core.data.local.TaskDao
import com.example.core.data.mappers.domainToEntity
import com.example.core.data.mappers.domainToEntityList
import com.example.core.data.mappers.entityToDomain
import com.example.core.data.mappers.entityToDomainList
import com.example.core.data.mappers.networkToEntityList
import com.example.core.network.FakeNetworkApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao,
    private val fakeNetworkApi: FakeNetworkApi
) : TaskRepository {


    override fun getAllTasks(): Flow<List<Task>> =
        taskDao.getTasks()
            .map { it.entityToDomainList() }.flowOn(Dispatchers.IO)


    override suspend fun syncWithNetwork() {
        val networkTasks = fakeNetworkApi.fetchTasks()
        if (networkTasks.isNotEmpty()) {
            taskDao.insertAll(networkTasks.networkToEntityList())
        }
    }

    override suspend fun getTaskById(id: Long): Task? = withContext(Dispatchers.IO) {
        taskDao.getById(id)?.entityToDomain()
    }

    override suspend fun insertAll(tasks: List<Task>) {
        taskDao.insertAll(tasks.domainToEntityList())
    }

    override suspend fun insert(task: Task) {
        taskDao.insert(task.domainToEntity())
    }

    override suspend fun update(task: Task) {
        taskDao.update(task.domainToEntity())
    }

    override suspend fun delete(id: Long) {
        taskDao.delete(id)
    }
}
