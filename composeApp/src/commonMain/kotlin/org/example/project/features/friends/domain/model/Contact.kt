package org.example.project.features.friends.domain.model

/**
 * Domain model representing a contact from the user's device
 * Used for friend suggestions and invitations
 */
data class Contact(
    val id: String,
    val name: String,
    val phoneNumber: String? = null,
    val email: String? = null,
    val isRegistered: Boolean = false,
    val registeredUserId: String? = null
)
