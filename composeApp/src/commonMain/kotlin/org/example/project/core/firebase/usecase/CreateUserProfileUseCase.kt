package org.example.project.core.firebase.usecase

import org.example.project.core.firebase.model.FirebaseUser
import org.example.project.core.firebase.repository.FirebaseUserRepository
import org.example.project.core.utils.TimeUtils

/**
 * Use case for creating or updating user profile after authentication
 */
class CreateUserProfileUseCase(
    private val userRepository: FirebaseUserRepository
) {
    /**
     * Create or update user profile with complete information
     */
    suspend fun execute(
        uid: String,
        name: String,
        email: String,
        phoneNumber: String? = null,
        username: String? = null,
        avatarUrl: String? = null
    ): Result<Unit> {
        return try {
            val currentTime = TimeUtils.currentTimeMillis()
            
            // Check if user already exists
            val existingUser = userRepository.getUserById(uid).getOrNull()
            
            val user = if (existingUser != null) {
                // Update existing user
                existingUser.copy(
                    name = name.ifBlank { existingUser.name },
                    email = email.ifBlank { existingUser.email },
                    phoneNumber = phoneNumber ?: existingUser.phoneNumber,
                    username = username ?: existingUser.username,
                    avatarUrl = avatarUrl ?: existingUser.avatarUrl,
                    updatedAt = currentTime,
                    lastSeen = currentTime
                )
            } else {
                // Create new user
                FirebaseUser(
                    uid = uid,
                    name = name,
                    email = email,
                    phoneNumber = phoneNumber,
                    username = username,
                    avatarUrl = avatarUrl,
                    createdAt = currentTime,
                    updatedAt = currentTime,
                    lastSeen = currentTime
                )
            }
            
            // Generate searchable fields for efficient querying
            val userWithSearchableFields = user.copy(
                searchableFields = user.generateSearchableFields()
            )
            
            userRepository.createOrUpdateUser(userWithSearchableFields)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Validate username availability before creating profile
     */
    suspend fun validateUsername(username: String): Result<Boolean> {
        return if (username.isBlank()) {
            Result.success(false)
        } else {
            userRepository.isUsernameAvailable(username)
        }
    }
}
