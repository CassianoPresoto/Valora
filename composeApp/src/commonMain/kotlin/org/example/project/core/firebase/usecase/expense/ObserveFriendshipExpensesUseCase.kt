package org.example.project.core.firebase.usecase.expense

import kotlinx.coroutines.flow.Flow
import org.example.project.core.firebase.model.FirebaseExpense
import org.example.project.core.firebase.repository.FirebaseExpenseRepository

class ObserveFriendshipExpensesUseCase(
    private val repository: FirebaseExpenseRepository
) {
    operator fun invoke(friendshipId: String): Flow<List<FirebaseExpense>> =
        repository.observeExpensesByFriendship(friendshipId)
}
