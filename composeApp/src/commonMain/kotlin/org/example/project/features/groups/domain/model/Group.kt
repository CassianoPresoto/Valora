package org.example.project.features.groups.domain.model

data class Group(
    val groupId: String,
    val serverId: String? = null,
    val name: String,
    val description: String? = null,
    val avatarUrl: String? = null,
    val memberIds: List<String> = emptyList(),
    val totalBalance: Double = 0.0,
    val createdBy: String,
    val metadata: Map<String, String> = emptyMap(),
    val createdAtEpochMillis: Long,
    val updatedAtEpochMillis: Long? = null,
)
