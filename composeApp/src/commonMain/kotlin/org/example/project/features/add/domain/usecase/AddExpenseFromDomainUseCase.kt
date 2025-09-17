package org.example.project.features.add.domain.usecase

import org.example.project.core.firebase.mapper.ExpenseMappers
import org.example.project.core.firebase.repository.FirebaseExpenseRepository
import org.example.project.features.add.domain.model.Expense

class AddExpenseFromDomainUseCase(
    private val expenseRepository: FirebaseExpenseRepository
) {
    suspend operator fun invoke(expense: Expense): Result<String> {
        val fb = ExpenseMappers.toFirebase(expense)
        return expenseRepository.addExpense(fb)
    }
}
