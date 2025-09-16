package org.example.project.core.firebase.data

import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.Direction
import dev.gitlive.firebase.firestore.FirebaseFirestore
import dev.gitlive.firebase.firestore.firestore
import dev.gitlive.firebase.firestore.where
import dev.gitlive.firebase.firestore.orderBy
import dev.gitlive.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.example.project.core.firebase.model.FirebaseExpense
import org.example.project.core.firebase.repository.FirebaseExpenseRepository
import org.example.project.core.utils.TimeUtils

private const val EXPENSES_COLLECTION = "expenses"

class FirebaseExpenseRepositoryImpl : FirebaseExpenseRepository {

    private val db: FirebaseFirestore = Firebase.firestore

    override suspend fun addExpense(expense: FirebaseExpense): Result<String> = try {
        val collection = db.collection(EXPENSES_COLLECTION)
        val now = TimeUtils.currentTimeMillis()
        val createdAt = if (expense.createdAt <= 0L) now else expense.createdAt
        if (expense.id != null) {
            val payload = expense.copy(id = expense.id, createdAt = createdAt, updatedAt = now)
            collection.document(expense.id).set(payload)
            Result.success(expense.id)
        } else {
            val payload = expense.copy(id = null, createdAt = createdAt, updatedAt = now)
            val ref = collection.add(payload)
            Result.success(ref.id)
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    override fun observeAllExpenses(): Flow<List<FirebaseExpense>> {
        return db.collection(EXPENSES_COLLECTION)
            .orderBy("createdAt", Direction.DESCENDING)
            .snapshots
            .map { snapshot: QuerySnapshot ->
                snapshot.documents.mapNotNull { doc ->
                    runCatching { doc.data<FirebaseExpense>().copy(id = doc.id) }.getOrNull()
                }
            }
    }

    @Suppress("DEPRECATION")
    override fun observeExpensesByUser(userId: String): Flow<List<FirebaseExpense>> {
        return db.collection(EXPENSES_COLLECTION)
            .where("participants", arrayContains = userId)
            .orderBy("createdAt", Direction.DESCENDING)
            .snapshots
            .map { snapshot: QuerySnapshot ->
                snapshot.documents.mapNotNull { doc ->
                    runCatching { doc.data<FirebaseExpense>().copy(id = doc.id) }.getOrNull()
                }
            }
    }

    @Suppress("DEPRECATION")
    override fun observeExpensesByFriendship(friendshipId: String): Flow<List<FirebaseExpense>> {
        return db.collection(EXPENSES_COLLECTION)
            .where("friendshipId", equalTo = friendshipId)
            .orderBy("createdAt", Direction.DESCENDING)
            .snapshots
            .map { snapshot: QuerySnapshot ->
                snapshot.documents.mapNotNull { doc ->
                    runCatching { doc.data<FirebaseExpense>().copy(id = doc.id) }.getOrNull()
                }
            }
    }

    override suspend fun deleteExpense(expenseId: String): Result<Unit> = try {
        val now = TimeUtils.currentTimeMillis()
        db.collection(EXPENSES_COLLECTION).document(expenseId)
            .update(
                mapOf(
                    "status" to "DELETED",
                    "updatedAt" to now
                )
            )
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun settleExpense(expenseId: String): Result<Unit> = try {
        val now = TimeUtils.currentTimeMillis()
        db.collection(EXPENSES_COLLECTION).document(expenseId)
            .update(
                mapOf(
                    "status" to "SETTLED",
                    "updatedAt" to now
                )
            )
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
}
