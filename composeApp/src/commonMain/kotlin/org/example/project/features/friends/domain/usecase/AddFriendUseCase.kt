package org.example.project.features.friends.domain.usecase

import org.example.project.features.friends.domain.repository.FriendsRepository

class AddFriendUseCase(
    private val friendsRepository: FriendsRepository
) {
    suspend operator fun invoke(userId: String): Result<Unit> {
        if (userId.isBlank()) {
            return Result.failure(IllegalArgumentException("User ID cannot be empty"))
        }
        
        return friendsRepository.addFriend(userId)
    }
}
