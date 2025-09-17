package org.example.project.core.firebase.data

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import org.example.project.core.firebase.model.FirebaseFriendship
import org.example.project.core.firebase.model.FriendshipStatus
import org.example.project.core.firebase.repository.FirebaseFriendsRepository
import org.example.project.core.utils.TimeUtils
import org.example.project.features.friends.domain.model.Friend

/**
 * Mock implementation of FirebaseFriendsRepository for development and testing
 */
class MockFirebaseFriendsRepository : FirebaseFriendsRepository {
    
    private val friendships = mutableMapOf<String, FirebaseFriendship>()
    private val friendshipFlows = mutableMapOf<String, MutableStateFlow<FirebaseFriendship?>>()
    private var currentUserId: String = "user_1" // Default for testing
    
    // Mock user data for friend display
    private val mockUserNames = mapOf(
        "user_1" to "João Silva",
        "user_2" to "Maria Santos", 
        "user_3" to "Pedro Costa",
        "user_4" to "Ana Oliveira",
        "user_5" to "Carlos Lima"
    )
    
    init {
        // Add mock friendships for testing
        val mockFriendships = listOf(
            FirebaseFriendship(
                id = FirebaseFriendship.generateId("user_1", "user_2"),
                userId1 = "user_1",
                userId2 = "user_2",
                status = FriendshipStatus.ACCEPTED,
                requestedBy = "user_1",
                requestedAt = TimeUtils.daysAgo(1),
                acceptedAt = TimeUtils.daysAgo(1),
                createdAt = TimeUtils.daysAgo(1),
                updatedAt = TimeUtils.daysAgo(1),
                user1Balance = 25.50,
                user2Balance = -25.50,
                totalBalance = 25.50,
                expenseCount = 3,
                lastExpenseAt = TimeUtils.hoursAgo(1)
            ),
            FirebaseFriendship(
                id = FirebaseFriendship.generateId("user_1", "user_3"),
                userId1 = "user_1",
                userId2 = "user_3",
                status = FriendshipStatus.ACCEPTED,
                requestedBy = "user_3",
                requestedAt = TimeUtils.daysAgo(2),
                acceptedAt = TimeUtils.daysAgo(2),
                createdAt = TimeUtils.daysAgo(2),
                updatedAt = TimeUtils.daysAgo(2),
                user1Balance = -100.00,
                user2Balance = 100.00,
                totalBalance = 100.00,
                expenseCount = 5,
                lastExpenseAt = TimeUtils.hoursAgo(2)
            ),
            FirebaseFriendship(
                id = FirebaseFriendship.generateId("user_1", "user_4"),
                userId1 = "user_1",
                userId2 = "user_4",
                status = FriendshipStatus.PENDING,
                requestedBy = "user_4",
                requestedAt = TimeUtils.hoursAgo(1),
                createdAt = TimeUtils.hoursAgo(1),
                updatedAt = TimeUtils.hoursAgo(1),
                user1Balance = 0.0,
                user2Balance = 0.0,
                totalBalance = 0.0,
                expenseCount = 0
            ),
            FirebaseFriendship(
                id = FirebaseFriendship.generateId("user_1", "user_5"),
                userId1 = "user_1",
                userId2 = "user_5",
                status = FriendshipStatus.ACCEPTED,
                requestedBy = "user_1",
                requestedAt = TimeUtils.daysAgo(3),
                acceptedAt = TimeUtils.daysAgo(3),
                createdAt = TimeUtils.daysAgo(3),
                updatedAt = TimeUtils.daysAgo(3),
                user1Balance = 30.25,
                user2Balance = -30.25,
                totalBalance = 30.25,
                expenseCount = 2,
                lastExpenseAt = TimeUtils.hoursAgo(4)
            )
        )
        
        mockFriendships.forEach { friendship ->
            friendships[friendship.id] = friendship
            friendshipFlows[friendship.id] = MutableStateFlow(friendship)
        }
    }
    
    override fun getFriends(): Flow<List<Friend>> {
        return MutableStateFlow(
            friendships.values
                .filter { it.status == FriendshipStatus.ACCEPTED && it.containsUser(currentUserId) }
                .map { friendship ->
                    val otherUserId = friendship.getOtherUserId(currentUserId)!!
                    val balance = friendship.getBalanceForUser(currentUserId)
                    
                    Friend(
                        userId = otherUserId,
                        name = mockUserNames[otherUserId] ?: "Unknown User",
                        totalBalance = balance
                    )
                }
        ).asStateFlow()
    }
    
    override fun searchFriends(query: String): Flow<List<Friend>> {
        return getFriends().map { friends ->
            if (query.isBlank()) {
                friends
            } else {
                friends.filter { friend ->
                    friend.name.contains(query, ignoreCase = true)
                }
            }
        }
    }
    
    override suspend fun sendFriendRequest(targetUserId: String): Result<Unit> {
        delay(300) // Simulate network delay
        
        return try {
            if (targetUserId == currentUserId) {
                throw Exception("Cannot send friend request to yourself")
            }
            
            val friendshipId = FirebaseFriendship.generateId(currentUserId, targetUserId)
            
            // Check if friendship already exists
            val existingFriendship = friendships[friendshipId]
            if (existingFriendship != null) {
                when (existingFriendship.status) {
                    FriendshipStatus.ACCEPTED -> throw Exception("Users are already friends")
                    FriendshipStatus.PENDING -> throw Exception("Friend request already sent")
                    FriendshipStatus.BLOCKED -> throw Exception("Cannot send friend request to blocked user")
                    FriendshipStatus.DECLINED -> {
                        // Allow resending after decline
                    }
                }
            }
            
            val (userId1, userId2) = FirebaseFriendship.createOrderedUserIds(currentUserId, targetUserId)
            val currentTime = TimeUtils.currentTimeMillis()
            
            val friendship = FirebaseFriendship(
                id = friendshipId,
                userId1 = userId1,
                userId2 = userId2,
                status = FriendshipStatus.PENDING,
                requestedBy = currentUserId,
                requestedAt = currentTime,
                createdAt = currentTime,
                updatedAt = currentTime
            )
            
            friendships[friendshipId] = friendship
            friendshipFlows.getOrPut(friendshipId) { MutableStateFlow(null) }.value = friendship
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun acceptFriendRequest(fromUserId: String): Result<Unit> {
        delay(200)
        
        return try {
            val friendshipId = FirebaseFriendship.generateId(currentUserId, fromUserId)
            val friendship = friendships[friendshipId] ?: throw Exception("Friend request not found")
            
            if (friendship.status != FriendshipStatus.PENDING) {
                throw Exception("No pending friend request from this user")
            }
            
            if (friendship.requestedBy == currentUserId) {
                throw Exception("Cannot accept your own friend request")
            }
            
            val updatedFriendship = friendship.copy(
                status = FriendshipStatus.ACCEPTED,
                acceptedAt = TimeUtils.currentTimeMillis(),
                updatedAt = TimeUtils.currentTimeMillis()
            )
            
            friendships[friendshipId] = updatedFriendship
            friendshipFlows[friendshipId]?.value = updatedFriendship
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun declineFriendRequest(fromUserId: String): Result<Unit> {
        delay(200)
        
        return try {
            val friendshipId = FirebaseFriendship.generateId(currentUserId, fromUserId)
            val friendship = friendships[friendshipId] ?: throw Exception("Friend request not found")
            
            if (friendship.status != FriendshipStatus.PENDING) {
                throw Exception("No pending friend request from this user")
            }
            
            val updatedFriendship = friendship.copy(
                status = FriendshipStatus.DECLINED,
                updatedAt = TimeUtils.currentTimeMillis()
            )
            
            friendships[friendshipId] = updatedFriendship
            friendshipFlows[friendshipId]?.value = updatedFriendship
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun removeFriend(friendUserId: String): Result<Unit> {
        delay(200)
        
        return try {
            val friendshipId = FirebaseFriendship.generateId(currentUserId, friendUserId)
            
            // Remove friendship completely
            friendships.remove(friendshipId)
            friendshipFlows[friendshipId]?.value = null
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun blockUser(userId: String): Result<Unit> {
        delay(200)
        
        return try {
            val friendshipId = FirebaseFriendship.generateId(currentUserId, userId)
            val existingFriendship = friendships[friendshipId]
            
            val (userId1, userId2) = FirebaseFriendship.createOrderedUserIds(currentUserId, userId)
            val currentTime = TimeUtils.currentTimeMillis()
            
            val friendship = existingFriendship?.copy(
                status = FriendshipStatus.BLOCKED,
                updatedAt = currentTime
            ) ?: FirebaseFriendship(
                id = friendshipId,
                userId1 = userId1,
                userId2 = userId2,
                status = FriendshipStatus.BLOCKED,
                requestedBy = currentUserId,
                requestedAt = currentTime,
                createdAt = currentTime,
                updatedAt = currentTime
            )
            
            friendships[friendshipId] = friendship
            friendshipFlows.getOrPut(friendshipId) { MutableStateFlow(null) }.value = friendship
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun unblockUser(userId: String): Result<Unit> {
        delay(200)
        
        return try {
            val friendshipId = FirebaseFriendship.generateId(currentUserId, userId)
            
            // Remove blocked relationship
            friendships.remove(friendshipId)
            friendshipFlows[friendshipId]?.value = null
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override fun getPendingFriendRequests(): Flow<List<Friend>> {
        return MutableStateFlow(
            friendships.values
                .filter { 
                    it.status == FriendshipStatus.PENDING && 
                    it.requestedBy != currentUserId &&
                    it.containsUser(currentUserId)
                }
                .map { friendship ->
                    val requesterId = friendship.requestedBy
                    Friend(
                        userId = requesterId,
                        name = mockUserNames[requesterId] ?: "Unknown User",
                        totalBalance = 0.0
                    )
                }
        ).asStateFlow()
    }
    
    override fun getSentFriendRequests(): Flow<List<Friend>> {
        return MutableStateFlow(
            friendships.values
                .filter { 
                    it.status == FriendshipStatus.PENDING && 
                    it.requestedBy == currentUserId 
                }
                .map { friendship ->
                    val otherUserId = friendship.getOtherUserId(currentUserId)!!
                    Friend(
                        userId = otherUserId,
                        name = mockUserNames[otherUserId] ?: "Unknown User",
                        totalBalance = 0.0
                    )
                }
        ).asStateFlow()
    }
    
    override fun getBlockedUsers(): Flow<List<Friend>> {
        return MutableStateFlow(
            friendships.values
                .filter { it.status == FriendshipStatus.BLOCKED && it.containsUser(currentUserId) }
                .map { friendship ->
                    val otherUserId = friendship.getOtherUserId(currentUserId)!!
                    Friend(
                        userId = otherUserId,
                        name = mockUserNames[otherUserId] ?: "Unknown User",
                        totalBalance = 0.0
                    )
                }
        ).asStateFlow()
    }
    
    override suspend fun getFriendshipStatus(userId: String): Result<FriendshipStatus?> {
        delay(100)
        
        return try {
            val friendshipId = FirebaseFriendship.generateId(currentUserId, userId)
            val friendship = friendships[friendshipId]
            Result.success(friendship?.status)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun updateFriendshipBalance(
        friendUserId: String,
        totalBalance: Double,
        currentUserBalance: Double,
        friendBalance: Double
    ): Result<Unit> {
        delay(200)
        
        return try {
            val friendshipId = FirebaseFriendship.generateId(currentUserId, friendUserId)
            val friendship = friendships[friendshipId] ?: throw Exception("Friendship not found")
            
            if (friendship.status != FriendshipStatus.ACCEPTED) {
                throw Exception("Cannot update balance for non-accepted friendship")
            }
            
            val (userId1, userId2) = FirebaseFriendship.createOrderedUserIds(currentUserId, friendUserId)
            val (user1Balance, user2Balance) = if (currentUserId == userId1) {
                Pair(currentUserBalance, friendBalance)
            } else {
                Pair(friendBalance, currentUserBalance)
            }
            
            val updatedFriendship = friendship.copy(
                totalBalance = totalBalance,
                user1Balance = user1Balance,
                user2Balance = user2Balance,
                updatedAt = TimeUtils.currentTimeMillis(),
                lastExpenseAt = TimeUtils.currentTimeMillis(),
                expenseCount = friendship.expenseCount + 1
            )
            
            friendships[friendshipId] = updatedFriendship
            friendshipFlows[friendshipId]?.value = updatedFriendship
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override fun getTotalDebtSummary(): Flow<Double> {
        return MutableStateFlow(
            friendships.values
                .filter { it.status == FriendshipStatus.ACCEPTED && it.containsUser(currentUserId) }
                .sumOf { friendship -> friendship.getBalanceForUser(currentUserId) }
        ).asStateFlow()
    }
    
    override fun getFriendshipFlow(friendUserId: String): Flow<FirebaseFriendship?> {
        val friendshipId = FirebaseFriendship.generateId(currentUserId, friendUserId)
        return friendshipFlows.getOrPut(friendshipId) { 
            MutableStateFlow(friendships[friendshipId]) 
        }.asStateFlow()
    }
    
    override suspend fun getFriendship(userId1: String, userId2: String): Result<FirebaseFriendship?> {
        delay(100)
        
        return try {
            val friendshipId = FirebaseFriendship.generateId(userId1, userId2)
            Result.success(friendships[friendshipId])
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun areFriends(userId1: String, userId2: String): Result<Boolean> {
        delay(100)
        
        return try {
            val friendshipId = FirebaseFriendship.generateId(userId1, userId2)
            val friendship = friendships[friendshipId]
            Result.success(friendship?.status == FriendshipStatus.ACCEPTED)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    // Helper method to set current user (for testing)
    fun setCurrentUser(userId: String) {
        currentUserId = userId
    }
}
