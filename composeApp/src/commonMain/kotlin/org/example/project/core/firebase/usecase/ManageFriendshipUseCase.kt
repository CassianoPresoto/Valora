package org.example.project.core.firebase.usecase

import kotlinx.coroutines.flow.Flow
import org.example.project.core.firebase.model.FriendshipStatus
import org.example.project.core.firebase.repository.FirebaseFriendsRepository
import org.example.project.features.friends.domain.model.Friend

/**
 * Use case for managing friendships (add, remove, block, etc.)
 */
class ManageFriendshipUseCase(
    private val friendsRepository: FirebaseFriendsRepository
) {
    /**
     * Send friend request to a user
     */
    suspend fun sendFriendRequest(targetUserId: String): Result<Unit> {
        return try {
            // Check current friendship status first
            val currentStatus = friendsRepository.getFriendshipStatus(targetUserId).getOrNull()
            
            when (currentStatus) {
                FriendshipStatus.ACCEPTED -> {
                    Result.failure(Exception("Users are already friends"))
                }
                FriendshipStatus.PENDING -> {
                    Result.failure(Exception("Friend request already sent"))
                }
                FriendshipStatus.BLOCKED -> {
                    Result.failure(Exception("Cannot send friend request to blocked user"))
                }
                FriendshipStatus.DECLINED, null -> {
                    friendsRepository.sendFriendRequest(targetUserId)
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Accept friend request
     */
    suspend fun acceptFriendRequest(fromUserId: String): Result<Unit> {
        return friendsRepository.acceptFriendRequest(fromUserId)
    }
    
    /**
     * Decline friend request
     */
    suspend fun declineFriendRequest(fromUserId: String): Result<Unit> {
        return friendsRepository.declineFriendRequest(fromUserId)
    }
    
    /**
     * Remove friend (unfriend)
     */
    suspend fun removeFriend(friendUserId: String): Result<Unit> {
        return friendsRepository.removeFriend(friendUserId)
    }
    
    /**
     * Block user
     */
    suspend fun blockUser(userId: String): Result<Unit> {
        return friendsRepository.blockUser(userId)
    }
    
    /**
     * Unblock user
     */
    suspend fun unblockUser(userId: String): Result<Unit> {
        return friendsRepository.unblockUser(userId)
    }
    
    /**
     * Get pending friend requests (received)
     */
    fun getPendingFriendRequests(): Flow<List<Friend>> {
        return friendsRepository.getPendingFriendRequests()
    }
    
    /**
     * Get sent friend requests
     */
    fun getSentFriendRequests(): Flow<List<Friend>> {
        return friendsRepository.getSentFriendRequests()
    }
    
    /**
     * Get blocked users
     */
    fun getBlockedUsers(): Flow<List<Friend>> {
        return friendsRepository.getBlockedUsers()
    }
    
    /**
     * Check friendship status with another user
     */
    suspend fun getFriendshipStatus(userId: String): Result<FriendshipStatus?> {
        return friendsRepository.getFriendshipStatus(userId)
    }
    
    /**
     * Check if users are friends
     */
    suspend fun areFriends(userId1: String, userId2: String): Result<Boolean> {
        return friendsRepository.areFriends(userId1, userId2)
    }
}
