package com.example.core.common.domain.model

data class Task(
    val id: Long = 0L,
    val title: String,
    val description: String? = null,
    val isDone: Boolean = false,
    val updatedAt: Long = System.currentTimeMillis()
)