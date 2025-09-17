package org.example.project.features.friends.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.example.project.features.friends.domain.repository.FriendsRepository

class GetTotalDebtSummaryUseCase(
    private val friendsRepository: FriendsRepository
) {
    operator fun invoke(): Flow<Double> {
        return friendsRepository.getTotalDebtSummary()
    }
}
