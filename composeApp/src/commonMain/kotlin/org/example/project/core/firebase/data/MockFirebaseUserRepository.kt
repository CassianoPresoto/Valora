package org.example.project.core.firebase.data

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import org.example.project.core.firebase.model.FirebaseUser
import org.example.project.core.firebase.repository.FirebaseUserRepository
import org.example.project.core.utils.TimeUtils

/**
 * Mock implementation of FirebaseUserRepository for development and testing
 */
class MockFirebaseUserRepository : FirebaseUserRepository {
    
    private val users = mutableMapOf<String, FirebaseUser>()
    private val userFlows = mutableMapOf<String, MutableStateFlow<FirebaseUser?>>()
    private var currentUserId: String? = null
    
    init {
        // Add mock users for testing
        val mockUsers = listOf(
            FirebaseUser(
                uid = "user_1",
                name = "João Silva",
                email = "joao@example.com",
                phoneNumber = "+5511999999999",
                username = "joaosilva",
                searchableFields = listOf("joão silva", "joao", "joao@example.com", "joaosilva", "5511999999999"),
                createdAt = TimeUtils.daysAgo(1),
                updatedAt = TimeUtils.currentTimeMillis(),
                lastSeen = TimeUtils.currentTimeMillis()
            ),
            FirebaseUser(
                uid = "user_2",
                name = "Maria Santos",
                email = "maria@example.com",
                phoneNumber = "+5511888888888",
                username = "mariasantos",
                searchableFields = listOf("maria santos", "maria", "maria@example.com", "mariasantos", "5511888888888"),
                createdAt = TimeUtils.daysAgo(2),
                updatedAt = TimeUtils.currentTimeMillis(),
                lastSeen = TimeUtils.hoursAgo(1)
            ),
            FirebaseUser(
                uid = "user_3",
                name = "Pedro Costa",
                email = "pedro@example.com",
                phoneNumber = "+5511777777777",
                username = "pedrocosta",
                searchableFields = listOf("pedro costa", "pedro", "pedro@example.com", "pedrocosta", "5511777777777"),
                createdAt = TimeUtils.daysAgo(3),
                updatedAt = TimeUtils.currentTimeMillis(),
                lastSeen = TimeUtils.hoursAgo(2)
            ),
            FirebaseUser(
                uid = "user_4",
                name = "Ana Oliveira",
                email = "ana@example.com",
                phoneNumber = "+5511666666666",
                username = "anaoliveira",
                searchableFields = listOf("ana oliveira", "ana", "ana@example.com", "anaoliveira", "5511666666666"),
                createdAt = TimeUtils.daysAgo(4),
                updatedAt = TimeUtils.currentTimeMillis(),
                lastSeen = TimeUtils.hoursAgo(3)
            ),
            FirebaseUser(
                uid = "user_5",
                name = "Carlos Lima",
                email = "carlos@example.com",
                phoneNumber = "+5511555555555",
                username = "carloslima",
                searchableFields = listOf("carlos lima", "carlos", "carlos@example.com", "carloslima", "5511555555555"),
                createdAt = TimeUtils.daysAgo(5),
                updatedAt = TimeUtils.currentTimeMillis(),
                lastSeen = TimeUtils.hoursAgo(4)
            )
        )
        
        mockUsers.forEach { user ->
            users[user.uid] = user
            userFlows[user.uid] = MutableStateFlow(user)
        }
    }
    
    override suspend fun createOrUpdateUser(user: FirebaseUser): Result<Unit> {
        delay(200) // Simulate network delay
        
        return try {
            val userWithSearchableFields = user.copy(
                searchableFields = user.generateSearchableFields(),
                updatedAt = TimeUtils.currentTimeMillis()
            )
            
            users[user.uid] = userWithSearchableFields
            userFlows.getOrPut(user.uid) { MutableStateFlow(null) }.value = userWithSearchableFields
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getUserById(userId: String): Result<FirebaseUser?> {
        delay(100)
        return Result.success(users[userId])
    }
    
    override suspend fun getCurrentUserProfile(): Result<FirebaseUser?> {
        delay(100)
        return Result.success(currentUserId?.let { users[it] })
    }
    
    override suspend fun updateUserProfile(
        name: String?,
        phoneNumber: String?,
        username: String?,
        avatarUrl: String?,
        isSearchableByPhone: Boolean?,
        isSearchableByEmail: Boolean?,
        isSearchableByUsername: Boolean?
    ): Result<Unit> {
        delay(300)
        
        return try {
            val userId = currentUserId ?: throw Exception("No user signed in")
            val currentUser = users[userId] ?: throw Exception("User not found")
            
            val updatedUser = currentUser.copy(
                name = name ?: currentUser.name,
                phoneNumber = phoneNumber ?: currentUser.phoneNumber,
                username = username ?: currentUser.username,
                avatarUrl = avatarUrl ?: currentUser.avatarUrl,
                isSearchableByPhone = isSearchableByPhone ?: currentUser.isSearchableByPhone,
                isSearchableByEmail = isSearchableByEmail ?: currentUser.isSearchableByEmail,
                isSearchableByUsername = isSearchableByUsername ?: currentUser.isSearchableByUsername,
                updatedAt = TimeUtils.currentTimeMillis()
            ).let { user ->
                user.copy(searchableFields = user.generateSearchableFields())
            }
            
            users[userId] = updatedUser
            userFlows[userId]?.value = updatedUser
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun searchUsers(query: String, limit: Int): Result<List<FirebaseUser>> {
        delay(300) // Simulate search delay
        
        return try {
            val searchQuery = query.lowercase().trim()
            
            val results = users.values
                .filter { user ->
                    user.isActive && user.searchableFields.any { field ->
                        field.contains(searchQuery)
                    }
                }
                .sortedByDescending { it.lastSeen }
                .take(limit)
            
            Result.success(results)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun searchUsersByPhoneNumbers(phoneNumbers: List<String>): Result<List<FirebaseUser>> {
        delay(200)
        
        return try {
            val cleanNumbers = phoneNumbers.map { it.replace(Regex("[^0-9]"), "") }
            
            val results = users.values.filter { user ->
                user.isActive && user.phoneNumber != null &&
                cleanNumbers.any { number ->
                    val userPhone = user.phoneNumber!!.replace(Regex("[^0-9]"), "")
                    userPhone == number || userPhone.endsWith(number) || number.endsWith(userPhone)
                }
            }
            
            Result.success(results)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun searchUsersByEmails(emails: List<String>): Result<List<FirebaseUser>> {
        delay(200)
        
        return try {
            val lowerEmails = emails.map { it.lowercase() }
            
            val results = users.values.filter { user ->
                user.isActive && lowerEmails.contains(user.email.lowercase())
            }
            
            Result.success(results)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun isUsernameAvailable(username: String): Result<Boolean> {
        delay(150)
        
        return try {
            val isAvailable = users.values.none { 
                it.username?.equals(username, ignoreCase = true) == true 
            }
            Result.success(isAvailable)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getUsersByIds(userIds: List<String>): Result<List<FirebaseUser>> {
        delay(200)
        
        return try {
            val results = userIds.mapNotNull { users[it] }
            Result.success(results)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override fun getUserProfileFlow(userId: String): Flow<FirebaseUser?> {
        return userFlows.getOrPut(userId) { MutableStateFlow(users[userId]) }.asStateFlow()
    }
    
    override suspend fun updateLastSeen(): Result<Unit> {
        delay(100)
        
        return try {
            val userId = currentUserId ?: throw Exception("No user signed in")
            val currentUser = users[userId] ?: throw Exception("User not found")
            
            val updatedUser = currentUser.copy(lastSeen = TimeUtils.currentTimeMillis())
            users[userId] = updatedUser
            userFlows[userId]?.value = updatedUser
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun deactivateUser(): Result<Unit> {
        delay(200)
        
        return try {
            val userId = currentUserId ?: throw Exception("No user signed in")
            val currentUser = users[userId] ?: throw Exception("User not found")
            
            val updatedUser = currentUser.copy(
                isActive = false,
                updatedAt = TimeUtils.currentTimeMillis()
            )
            users[userId] = updatedUser
            userFlows[userId]?.value = updatedUser
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun reactivateUser(): Result<Unit> {
        delay(200)
        
        return try {
            val userId = currentUserId ?: throw Exception("No user signed in")
            val currentUser = users[userId] ?: throw Exception("User not found")
            
            val updatedUser = currentUser.copy(
                isActive = true,
                updatedAt = TimeUtils.currentTimeMillis()
            )
            users[userId] = updatedUser
            userFlows[userId]?.value = updatedUser
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    // Helper method to set current user (for testing)
    fun setCurrentUser(userId: String) {
        currentUserId = userId
    }
}
