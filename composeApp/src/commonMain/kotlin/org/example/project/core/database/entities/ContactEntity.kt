package org.example.project.core.database.entities

/**
 * Represents a contact from the user's phone contacts
 * Used for mobile platforms to suggest friends
 */
data class ContactEntity(
    val contactId: String,
    val name: String,
    val phoneNumbers: List<String> = emptyList(),
    val emails: List<String> = emptyList(),
    val isRegisteredUser: Boolean = false, // Whether this contact is a registered app user
    val registeredUserId: String? = null, // If registered, their user ID
    val lastSyncedEpochMillis: Long
)
