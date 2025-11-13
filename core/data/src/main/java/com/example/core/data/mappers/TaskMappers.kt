package com.example.core.data.mappers

import com.example.core.common.domain.model.Task
import com.example.core.data.local.entity.TaskEntity
import com.example.core.network.model.NetworkTask


fun TaskEntity.entityToDomain() = Task(id, title, description ?: "", isDone, updatedAt)

fun List<TaskEntity>.entityToDomainList() = map { it.entityToDomain() }
fun Task.domainToEntity() = TaskEntity(
    id = id,
    title = title,
    description = description,
    isDone = isDone,
    updatedAt = updatedAt
)

fun List<Task>.domainToEntityList() = map { it.domainToEntity() }

fun NetworkTask.networkToEntity() = TaskEntity(
    id = id,
    title = title,
    description = description,
    isDone = isDone,
    updatedAt = updatedAt
)
fun List<NetworkTask>.networkToEntityList() = map { it.networkToEntity() }

fun NetworkTask.networkToDomain() = Task(
    id = id,
    title = title,
    description = description,
    isDone = isDone,
    updatedAt = updatedAt
)
fun List<NetworkTask>.toDomainList() = map { it.networkToDomain() }


