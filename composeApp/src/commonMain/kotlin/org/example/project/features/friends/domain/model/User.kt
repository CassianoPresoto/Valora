package org.example.project.features.friends.domain.model

/**
 * Domain model representing a user in the friends system
 * Used for user search and friend management
 */
data class User(
    val id: String,
    val name: String,
    val email: String? = null,
    val username: String? = null,
    val avatarUrl: String? = null,
    val phoneNumber: String? = null
)
