package org.example.project.core.firebase.usecase.expense

import kotlinx.coroutines.flow.Flow
import org.example.project.core.firebase.model.FirebaseExpense
import org.example.project.core.firebase.repository.FirebaseExpenseRepository

class ObserveMyExpensesUseCase(
    private val repository: FirebaseExpenseRepository
) {
    operator fun invoke(userId: String): Flow<List<FirebaseExpense>> =
        repository.observeExpensesByUser(userId)
}
