package org.example.project.core.database.entities

data class UserEntity(
    val userId: String,
    val serverId: String? = null,
    val name: String,
    val email: String? = null,
    val phoneNumber: String? = null,
    val username: String? = null,
    val avatarUrl: String? = null,
    val isCurrentUser: Boolean = false,
    val createdAtEpochMillis: Long,
    val updatedAtEpochMillis: Long? = null
)
