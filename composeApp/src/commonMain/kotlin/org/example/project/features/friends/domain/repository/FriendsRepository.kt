package org.example.project.features.friends.domain.repository

import kotlinx.coroutines.flow.Flow
import org.example.project.features.friends.domain.model.Friend
import org.example.project.core.database.entities.FriendshipStatus

interface FriendsRepository {
    /**
     * Get all friends for the current user
     */
    fun getFriends(): Flow<List<Friend>>
    
    /**
     * Search friends by name
     */
    fun searchFriends(query: String): Flow<List<Friend>>
    
    /**
     * Add a friend by user ID
     */
    suspend fun addFriend(userId: String): Result<Unit>
    
    /**
     * Remove a friend
     */
    suspend fun removeFriend(friendUserId: String): Result<Unit>
    
    /**
     * Accept a friend request
     */
    suspend fun acceptFriendRequest(friendUserId: String): Result<Unit>
    
    /**
     * Decline a friend request
     */
    suspend fun declineFriendRequest(friendUserId: String): Result<Unit>
    
    /**
     * Get pending friend requests (received)
     */
    fun getPendingFriendRequests(): Flow<List<Friend>>
    
    /**
     * Get sent friend requests
     */
    fun getSentFriendRequests(): Flow<List<Friend>>
    
    /**
     * Update friend's balance
     */
    suspend fun updateFriendBalance(friendUserId: String, totalBalance: Double, personalBalance: Double): Result<Unit>
    
    /**
     * Get total debt summary
     */
    fun getTotalDebtSummary(): Flow<Double>
}
