package org.example.project.core.firebase.repository

import kotlinx.coroutines.flow.Flow
import org.example.project.core.firebase.model.FirebaseUser

/**
 * Repository interface for Firebase Authentication operations
 */
interface FirebaseAuthRepository {
    /**
     * Get current authenticated user
     */
    suspend fun getCurrentUser(): Result<FirebaseUser?>
    
    /**
     * Sign in with email and password
     */
    suspend fun signInWithEmail(email: String, password: String): Result<FirebaseUser>
    
    /**
     * Sign in with Google
     */
    suspend fun signInWithGoogle(): Result<FirebaseUser>
    
    /**
     * Create account with email and password
     */
    suspend fun createAccountWithEmail(email: String, password: String): Result<FirebaseUser>
    
    /**
     * Sign out current user
     */
    suspend fun signOut(): Result<Unit>
    
    /**
     * Listen to authentication state changes
     */
    fun getAuthStateFlow(): Flow<FirebaseUser?>
    
    /**
     * Send password reset email
     */
    suspend fun sendPasswordResetEmail(email: String): Result<Unit>
    
    /**
     * Update user email
     */
    suspend fun updateEmail(newEmail: String): Result<Unit>
    
    /**
     * Update user password
     */
    suspend fun updatePassword(newPassword: String): Result<Unit>
    
    /**
     * Delete user account
     */
    suspend fun deleteAccount(): Result<Unit>
    
    /**
     * Re-authenticate user (required for sensitive operations)
     */
    suspend fun reauthenticate(password: String): Result<Unit>
}
