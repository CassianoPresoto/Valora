package org.example.project.features.friends.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.example.project.features.friends.domain.model.Friend
import org.example.project.features.friends.domain.repository.FriendsRepository

class GetFriendsUseCase(
    private val friendsRepository: FriendsRepository
) {
    operator fun invoke(): Flow<List<Friend>> {
        return friendsRepository.getFriends()
    }
}
