package org.example.project.core.firebase.model

import kotlinx.serialization.Serializable

@Serializable
data class FirebaseExpense(
    val id: String? = null,
    val name: String,
    val createdBy: String,
    val participants: List<String>,
    val payerId: String,
    val splits: Map<String, Double>,
    val totalAmount: Double,
    val currency: String = "BRL",
    val notes: String? = null,
    val friendshipId: String? = null,
    val status: String = "ACTIVE", // ACTIVE | DELETED | SETTLED
    val createdAt: Long,
    val updatedAt: Long? = null
)
