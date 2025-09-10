package org.example.project.features.friends.domain.usecase

import org.example.project.core.database.entities.UserEntity
import org.example.project.features.friends.domain.repository.UsersRepository

class SearchUsersUseCase(
    private val usersRepository: UsersRepository
) {
    suspend operator fun invoke(query: String): Result<List<UserEntity>> {
        if (query.isBlank() || query.length < 2) {
            return Result.success(emptyList())
        }
        
        return usersRepository.searchUsers(query.trim())
    }
}
