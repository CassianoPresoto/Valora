package org.example.project.core.firebase.repository

import kotlinx.coroutines.flow.Flow
import org.example.project.core.firebase.model.FirebaseExpense

interface FirebaseExpenseRepository {
    suspend fun addExpense(expense: FirebaseExpense): Result<String>
    fun observeAllExpenses(): Flow<List<FirebaseExpense>>
    fun observeExpensesByUser(userId: String): Flow<List<FirebaseExpense>>
    fun observeExpensesByFriendship(friendshipId: String): Flow<List<FirebaseExpense>>
    suspend fun deleteExpense(expenseId: String): Result<Unit>
    suspend fun settleExpense(expenseId: String): Result<Unit>
}
