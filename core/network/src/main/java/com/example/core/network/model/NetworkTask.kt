package com.example.core.network.model

data class NetworkTask(
    val id: Long,
    val title: String,
    val description: String?,
    val isDone: Boolean,
    val updatedAt: Long
)
