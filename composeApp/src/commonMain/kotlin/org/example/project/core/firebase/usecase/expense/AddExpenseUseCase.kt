package org.example.project.core.firebase.usecase.expense

import org.example.project.core.firebase.model.FirebaseExpense
import org.example.project.core.firebase.repository.FirebaseExpenseRepository

class AddExpenseUseCase(
    private val repository: FirebaseExpenseRepository
) {
    suspend operator fun invoke(expense: FirebaseExpense): Result<String> =
        repository.addExpense(expense)
}
