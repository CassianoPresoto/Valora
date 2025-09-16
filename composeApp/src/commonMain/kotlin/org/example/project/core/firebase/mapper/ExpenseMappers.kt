package org.example.project.core.firebase.mapper

import org.example.project.core.firebase.model.FirebaseExpense
import org.example.project.core.utils.TimeUtils
import org.example.project.features.add.domain.model.Expense

object ExpenseMappers {
    /**
     * Map domain Expense (user-to-user, no group) to FirebaseExpense
     */
    fun toFirebase(
        domain: Expense
    ): FirebaseExpense {
        val participants = domain.splitBetween.distinct()
        val payerId = domain.paidBy
        val total = domain.amount
        val splitMode = domain.metadata["splitMode"] ?: "EQUAL"
        val otherId: String? = domain.metadata["otherUserId"]
        val splits: Map<String, Double> = when (splitMode) {
            "PAYER_ALL" -> {
                // Payer takes full share
                participants.associateWith { userId -> if (userId == payerId) total else 0.0 }
            }
            "OTHER_ALL" -> {
                val target = otherId ?: participants.firstOrNull { it != payerId }
                val targetId = target ?: payerId
                participants.associateWith { userId -> if (userId == targetId) total else 0.0 }
            }
            else -> {
                // Equal split among participants by default
                if (participants.isEmpty()) emptyMap() else {
                    val perHead = total / participants.size
                    participants.associateWith { perHead }
                }
            }
        }

        // Deterministic friendshipId for two users (sorted join)
        val friendshipId = if (participants.size == 2) participants.sorted().joinToString("_") else null

        val createdAt = if (domain.createdAtEpochMillis > 0L) domain.createdAtEpochMillis else TimeUtils.currentTimeMillis()

        return FirebaseExpense(
            id = domain.serverId, // Use serverId when updating existing; null to create new
            name = domain.name,
            createdBy = payerId,
            participants = participants,
            payerId = payerId,
            splits = splits,
            totalAmount = total,
            currency = "BRL",
            notes = domain.description,
            friendshipId = friendshipId,
            status = "ACTIVE",
            createdAt = createdAt,
            updatedAt = domain.updatedAtEpochMillis
        )
    }
}
