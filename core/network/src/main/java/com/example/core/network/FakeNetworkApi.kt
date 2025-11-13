package com.example.core.network

import com.example.core.network.model.NetworkTask
import kotlinx.coroutines.delay
import javax.inject.Inject

class FakeNetworkApi @Inject constructor() {
    suspend fun fetchTasks(): List<NetworkTask> {
        delay(1500)
        return listOf(
            NetworkTask(
                1,
                title = "Review Compose UI",
                description = "From server",
                isDone = false,
                updatedAt = System.currentTimeMillis() - 86_400_000
            ),
            NetworkTask(
                2,
                title = "Write Documentation",
                description = "From Server",
                isDone = false,
                updatedAt = System.currentTimeMillis()
            )
        )
    }
}