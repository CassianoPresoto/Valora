package org.example.project.core.firebase.adapter

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.example.project.core.firebase.repository.FirebaseFriendsRepository
import org.example.project.core.firebase.repository.FirebaseUserRepository
import org.example.project.features.friends.domain.model.Friend
import org.example.project.features.friends.domain.repository.FriendsRepository

/**
 * Adapter to connect Firebase friends repository with domain friends repository interface
 */
class FirebaseFriendsRepositoryAdapter(
    private val firebaseFriendsRepository: FirebaseFriendsRepository,
    private val firebaseUserRepository: FirebaseUserRepository
) : FriendsRepository {
    
    override fun getFriends(): Flow<List<Friend>> {
        return firebaseFriendsRepository.getFriends()
    }
    
    override fun searchFriends(query: String): Flow<List<Friend>> {
        return firebaseFriendsRepository.searchFriends(query)
    }
    
    override suspend fun addFriend(userId: String): Result<Unit> {
        return firebaseFriendsRepository.sendFriendRequest(userId)
    }
    
    override fun getTotalDebtSummary(): Flow<Double> {
        return firebaseFriendsRepository.getTotalDebtSummary()
    }
    
    // Additional methods that might be needed for the domain layer
    override suspend fun acceptFriendRequest(fromUserId: String): Result<Unit> {
        return firebaseFriendsRepository.acceptFriendRequest(fromUserId)
    }
    
    override suspend fun declineFriendRequest(fromUserId: String): Result<Unit> {
        return firebaseFriendsRepository.declineFriendRequest(fromUserId)
    }
    
    override suspend fun removeFriend(friendUserId: String): Result<Unit> {
        return firebaseFriendsRepository.removeFriend(friendUserId)
    }
    
    override fun getPendingFriendRequests(): Flow<List<Friend>> {
        return firebaseFriendsRepository.getPendingFriendRequests()
    }
    
    override fun getSentFriendRequests(): Flow<List<Friend>> {
        return firebaseFriendsRepository.getSentFriendRequests()
    }

    override suspend fun updateFriendBalance(
        friendUserId: String,
        totalBalance: Double,
        personalBalance: Double
    ): Result<Unit> {
        TODO("Not yet implemented")
    }
}
