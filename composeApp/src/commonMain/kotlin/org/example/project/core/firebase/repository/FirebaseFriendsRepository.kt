package org.example.project.core.firebase.repository

import kotlinx.coroutines.flow.Flow
import org.example.project.core.firebase.model.FirebaseFriendship
import org.example.project.core.firebase.model.FriendshipStatus
import org.example.project.features.friends.domain.model.Friend

/**
 * Repository interface for Firebase Firestore friendship operations
 */
interface FirebaseFriendsRepository {
    /**
     * Get all friends for current user
     */
    fun getFriends(): Flow<List<Friend>>
    
    /**
     * Search friends by name
     */
    fun searchFriends(query: String): Flow<List<Friend>>
    
    /**
     * Send friend request
     */
    suspend fun sendFriendRequest(targetUserId: String): Result<Unit>
    
    /**
     * Accept friend request
     */
    suspend fun acceptFriendRequest(fromUserId: String): Result<Unit>
    
    /**
     * Decline friend request
     */
    suspend fun declineFriendRequest(fromUserId: String): Result<Unit>
    
    /**
     * Remove friend (unfriend)
     */
    suspend fun removeFriend(friendUserId: String): Result<Unit>
    
    /**
     * Block user
     */
    suspend fun blockUser(userId: String): Result<Unit>
    
    /**
     * Unblock user
     */
    suspend fun unblockUser(userId: String): Result<Unit>
    
    /**
     * Get pending friend requests (received)
     */
    fun getPendingFriendRequests(): Flow<List<Friend>>
    
    /**
     * Get sent friend requests
     */
    fun getSentFriendRequests(): Flow<List<Friend>>
    
    /**
     * Get blocked users
     */
    fun getBlockedUsers(): Flow<List<Friend>>
    
    /**
     * Get friendship status between current user and another user
     */
    suspend fun getFriendshipStatus(userId: String): Result<FriendshipStatus?>
    
    /**
     * Update friendship balance
     */
    suspend fun updateFriendshipBalance(
        friendUserId: String, 
        totalBalance: Double,
        currentUserBalance: Double,
        friendBalance: Double
    ): Result<Unit>
    
    /**
     * Get total debt summary for current user
     */
    fun getTotalDebtSummary(): Flow<Double>
    
    /**
     * Listen to friendship changes
     */
    fun getFriendshipFlow(friendUserId: String): Flow<FirebaseFriendship?>
    
    /**
     * Get friendship by user IDs
     */
    suspend fun getFriendship(userId1: String, userId2: String): Result<FirebaseFriendship?>
    
    /**
     * Check if users are friends
     */
    suspend fun areFriends(userId1: String, userId2: String): Result<Boolean>
}
