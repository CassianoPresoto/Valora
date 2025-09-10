package org.example.project.core.firebase.repository

import kotlinx.coroutines.flow.Flow
import org.example.project.core.firebase.model.FirebaseUser

/**
 * Repository interface for Firebase Firestore user operations
 */
interface FirebaseUserRepository {
    /**
     * Create or update user profile in Firestore
     */
    suspend fun createOrUpdateUser(user: FirebaseUser): Result<Unit>
    
    /**
     * Get user by ID
     */
    suspend fun getUserById(userId: String): Result<FirebaseUser?>
    
    /**
     * Get current user profile
     */
    suspend fun getCurrentUserProfile(): Result<FirebaseUser?>
    
    /**
     * Update user profile
     */
    suspend fun updateUserProfile(
        name: String? = null,
        phoneNumber: String? = null,
        username: String? = null,
        avatarUrl: String? = null,
        isSearchableByPhone: Boolean? = null,
        isSearchableByEmail: Boolean? = null,
        isSearchableByUsername: Boolean? = null
    ): Result<Unit>
    
    /**
     * Search users by query (name, email, username, phone)
     */
    suspend fun searchUsers(query: String, limit: Int = 20): Result<List<FirebaseUser>>
    
    /**
     * Search users by phone numbers
     */
    suspend fun searchUsersByPhoneNumbers(phoneNumbers: List<String>): Result<List<FirebaseUser>>
    
    /**
     * Search users by emails
     */
    suspend fun searchUsersByEmails(emails: List<String>): Result<List<FirebaseUser>>
    
    /**
     * Check if username is available
     */
    suspend fun isUsernameAvailable(username: String): Result<Boolean>
    
    /**
     * Get users by IDs
     */
    suspend fun getUsersByIds(userIds: List<String>): Result<List<FirebaseUser>>
    
    /**
     * Listen to user profile changes
     */
    fun getUserProfileFlow(userId: String): Flow<FirebaseUser?>
    
    /**
     * Update user's last seen timestamp
     */
    suspend fun updateLastSeen(): Result<Unit>
    
    /**
     * Deactivate user account (soft delete)
     */
    suspend fun deactivateUser(): Result<Unit>
    
    /**
     * Reactivate user account
     */
    suspend fun reactivateUser(): Result<Unit>
}
