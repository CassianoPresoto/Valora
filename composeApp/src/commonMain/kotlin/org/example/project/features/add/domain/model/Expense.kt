package org.example.project.features.add.domain.model

data class Expense(
    val expenseId: String,
    val serverId: String? = null,
    val name: String,
    val description: String? = null,
    val amount: Double,
    val paidBy: String, // userId who paid
    val splitBetween: List<String>, // userIds who should split the expense
    val groupId: String? = null, // null if it's a personal expense between friends
    val category: String? = null,
    val receiptUrl: String? = null,
    val metadata: Map<String, String> = emptyMap(),
    val createdAtEpochMillis: Long,
    val updatedAtEpochMillis: Long? = null,
)

enum class ExpenseType {
    PERSONAL, // Between friends
    GROUP     // Within a group
}
