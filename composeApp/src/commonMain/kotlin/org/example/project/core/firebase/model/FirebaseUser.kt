package org.example.project.core.firebase.model

/**
 * Firebase Firestore document structure for users
 * Collection: "users"
 * Document ID: Firebase Auth UID
 */
data class FirebaseUser(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val phoneNumber: String? = null,
    val username: String? = null,
    val avatarUrl: String? = null,
    val searchableFields: List<String> = emptyList(), // For search optimization
    val isActive: Boolean = true,
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L,
    val lastSeen: Long = 0L,
    
    // Privacy settings
    val isSearchableByPhone: Boolean = true,
    val isSearchableByEmail: Boolean = true,
    val isSearchableByUsername: Boolean = true
) {
    companion object {
        const val COLLECTION_NAME = "users"
        
        // Field names for Firestore queries
        const val FIELD_UID = "uid"
        const val FIELD_NAME = "name"
        const val FIELD_EMAIL = "email"
        const val FIELD_PHONE_NUMBER = "phoneNumber"
        const val FIELD_USERNAME = "username"
        const val FIELD_SEARCHABLE_FIELDS = "searchableFields"
        const val FIELD_IS_ACTIVE = "isActive"
        const val FIELD_CREATED_AT = "createdAt"
        const val FIELD_UPDATED_AT = "updatedAt"
    }
    
    /**
     * Generate searchable fields for efficient querying
     * Includes name, email, username, and phone variations
     */
    fun generateSearchableFields(): List<String> {
        val fields = mutableListOf<String>()
        
        // Add name variations (lowercase, partial matches)
        name.lowercase().let { lowerName ->
            fields.add(lowerName)
            // Add partial matches for name (first 3+ characters)
            for (i in 3..lowerName.length) {
                fields.add(lowerName.substring(0, i))
            }
        }
        
        // Add email (lowercase)
        if (email.isNotBlank()) {
            fields.add(email.lowercase())
        }
        
        // Add username (lowercase)
        if (!username.isNullOrBlank()) {
            fields.add(username.lowercase())
        }
        
        // Add phone number variations (with and without formatting)
        if (!phoneNumber.isNullOrBlank()) {
            val cleanPhone = phoneNumber.replace(Regex("[^0-9]"), "")
            fields.add(cleanPhone)
            if (cleanPhone.startsWith("55")) {
                fields.add(cleanPhone.substring(2)) // Remove country code
            }
        }
        
        return fields.distinct()
    }
}
