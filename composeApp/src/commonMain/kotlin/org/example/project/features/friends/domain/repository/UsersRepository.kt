package org.example.project.features.friends.domain.repository

import kotlinx.coroutines.flow.Flow
import org.example.project.core.database.entities.UserEntity
import org.example.project.core.database.entities.ContactEntity

interface UsersRepository {
    /**
     * Search users by username, email, or phone number
     */
    suspend fun searchUsers(query: String): Result<List<UserEntity>>
    
    /**
     * Get user by ID
     */
    suspend fun getUserById(userId: String): Result<UserEntity?>
    
    /**
     * Get current user
     */
    suspend fun getCurrentUser(): Result<UserEntity?>
    
    /**
     * Update current user profile
     */
    suspend fun updateCurrentUser(user: UserEntity): Result<Unit>
    
    /**
     * Sync contacts from device (mobile only)
     */
    suspend fun syncContacts(contacts: List<ContactEntity>): Result<Unit>
    
    /**
     * Get synced contacts
     */
    fun getContacts(): Flow<List<ContactEntity>>
    
    /**
     * Search contacts by name or phone
     */
    fun searchContacts(query: String): Flow<List<ContactEntity>>
    
    /**
     * Check if users exist by phone numbers or emails
     */
    suspend fun checkUsersExist(phoneNumbers: List<String>, emails: List<String>): Result<List<UserEntity>>
}
