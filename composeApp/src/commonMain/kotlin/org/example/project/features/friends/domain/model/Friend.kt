package org.example.project.features.friends.domain.model

data class Friend(
    val userId: String,
    val serverId: String? = null,
    val name: String,
    val avatarUrl: String? = null,
    val totalBalance: Double = 0.0,
    val personalBalance: Double = 0.0,
    val groupBalances: Map<String, Double> = emptyMap(),
    val metadata: Map<String, String> = emptyMap(),
    val updatedAtEpochMillis: Long? = null,
)
