package org.example.project.core.firebase.data

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.example.project.core.firebase.model.FirebaseUser
import org.example.project.core.firebase.repository.FirebaseAuthRepository
import org.example.project.core.utils.TimeUtils

/**
 * Mock implementation of FirebaseAuthRepository for development and testing
 */
class MockFirebaseAuthRepository : FirebaseAuthRepository {
    
    private val _currentUser = MutableStateFlow<FirebaseUser?>(null)
    private var isSignedIn = false
    
    // Mock users database
    private val mockUsers = mutableMapOf<String, FirebaseUser>()
    
    init {
        // Add some mock users for testing
        val mockUser1 = FirebaseUser(
            uid = "mock_user_1",
            name = "João Silva",
            email = "joao@example.com",
            phoneNumber = "+5511999999999",
            username = "joaosilva",
            avatarUrl = null,
            createdAt = TimeUtils.daysAgo(1),
            updatedAt = TimeUtils.currentTimeMillis(),
            lastSeen = TimeUtils.currentTimeMillis()
        )
        mockUsers[mockUser1.uid] = mockUser1
    }
    
    override suspend fun getCurrentUser(): Result<FirebaseUser?> {
        delay(100) // Simulate network delay
        return Result.success(_currentUser.value)
    }
    
    override suspend fun signInWithEmail(email: String, password: String): Result<FirebaseUser> {
        delay(500) // Simulate network delay
        
        return try {
            // Simple mock validation
            if (email.isBlank() || password.length < 6) {
                throw Exception("Invalid email or password")
            }
            
            // Find or create mock user
            val user = mockUsers.values.find { it.email == email } ?: run {
                val newUser = FirebaseUser(
                    uid = "mock_${TimeUtils.currentTimeMillis()}",
                    name = email.substringBefore("@").replaceFirstChar { it.uppercase() },
                    email = email,
                    createdAt = TimeUtils.currentTimeMillis(),
                    updatedAt = TimeUtils.currentTimeMillis(),
                    lastSeen = TimeUtils.currentTimeMillis()
                )
                mockUsers[newUser.uid] = newUser
                newUser
            }
            
            _currentUser.value = user
            isSignedIn = true
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun signInWithGoogle(): Result<FirebaseUser> {
        delay(800) // Simulate Google sign-in delay
        
        return try {
            val user = FirebaseUser(
                uid = "google_mock_${TimeUtils.currentTimeMillis()}",
                name = "Google User",
                email = "googleuser@gmail.com",
                avatarUrl = "https://example.com/avatar.jpg",
                createdAt = TimeUtils.currentTimeMillis(),
                updatedAt = TimeUtils.currentTimeMillis(),
                lastSeen = TimeUtils.currentTimeMillis()
            )
            
            mockUsers[user.uid] = user
            _currentUser.value = user
            isSignedIn = true
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun createAccountWithEmail(email: String, password: String): Result<FirebaseUser> {
        delay(600) // Simulate account creation delay
        
        return try {
            // Check if user already exists
            if (mockUsers.values.any { it.email == email }) {
                throw Exception("User with this email already exists")
            }
            
            if (email.isBlank() || password.length < 6) {
                throw Exception("Invalid email or password too short")
            }
            
            val user = FirebaseUser(
                uid = "new_mock_${TimeUtils.currentTimeMillis()}",
                name = email.substringBefore("@").replaceFirstChar { it.uppercase() },
                email = email,
                createdAt = TimeUtils.currentTimeMillis(),
                updatedAt = TimeUtils.currentTimeMillis(),
                lastSeen = TimeUtils.currentTimeMillis()
            )
            
            mockUsers[user.uid] = user
            _currentUser.value = user
            isSignedIn = true
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun signOut(): Result<Unit> {
        delay(200) // Simulate sign out delay
        
        return try {
            _currentUser.value = null
            isSignedIn = false
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override fun getAuthStateFlow(): Flow<FirebaseUser?> {
        return _currentUser.asStateFlow()
    }
    
    override suspend fun sendPasswordResetEmail(email: String): Result<Unit> {
        delay(400) // Simulate email sending delay
        
        return try {
            if (email.isBlank() || !email.contains("@")) {
                throw Exception("Invalid email address")
            }
            
            // In real implementation, this would send an email
            println("Mock: Password reset email sent to $email")
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun updateEmail(newEmail: String): Result<Unit> {
        delay(300)
        
        return try {
            val currentUser = _currentUser.value ?: throw Exception("No user signed in")
            
            if (newEmail.isBlank() || !newEmail.contains("@")) {
                throw Exception("Invalid email address")
            }
            
            val updatedUser = currentUser.copy(
                email = newEmail,
                updatedAt = TimeUtils.currentTimeMillis()
            )
            
            mockUsers[currentUser.uid] = updatedUser
            _currentUser.value = updatedUser
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun updatePassword(newPassword: String): Result<Unit> {
        delay(300)
        
        return try {
            if (_currentUser.value == null) {
                throw Exception("No user signed in")
            }
            
            if (newPassword.length < 6) {
                throw Exception("Password must be at least 6 characters")
            }
            
            // In real implementation, this would update the password
            println("Mock: Password updated successfully")
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun deleteAccount(): Result<Unit> {
        delay(500)
        
        return try {
            val currentUser = _currentUser.value ?: throw Exception("No user signed in")
            
            mockUsers.remove(currentUser.uid)
            _currentUser.value = null
            isSignedIn = false
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun reauthenticate(password: String): Result<Unit> {
        delay(400)
        
        return try {
            if (_currentUser.value == null) {
                throw Exception("No user signed in")
            }
            
            if (password.length < 6) {
                throw Exception("Invalid password")
            }
            
            // In real implementation, this would verify the password
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
