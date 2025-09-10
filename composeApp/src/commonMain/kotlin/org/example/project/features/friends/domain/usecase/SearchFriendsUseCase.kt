package org.example.project.features.friends.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.example.project.features.friends.domain.model.Friend
import org.example.project.features.friends.domain.repository.FriendsRepository

class SearchFriendsUseCase(
    private val friendsRepository: FriendsRepository
) {
    operator fun invoke(query: String): Flow<List<Friend>> {
        return if (query.isBlank()) {
            friendsRepository.getFriends()
        } else {
            friendsRepository.searchFriends(query.trim())
        }
    }
}
