package org.example.project.core.database.entities

data class FriendshipEntity(
    val friendshipId: String,
    val userId: String, // Current user ID
    val friendUserId: String, // Friend's user ID
    val status: FriendshipStatus = FriendshipStatus.PENDING,
    val totalBalance: Double = 0.0,
    val personalBalance: Double = 0.0,
    val groupBalances: Map<String, Double> = emptyMap(),
    val createdAtEpochMillis: Long,
    val updatedAtEpochMillis: Long? = null
)

enum class FriendshipStatus {
    PENDING,    // Friend request sent but not accepted
    ACCEPTED,   // Friend request accepted
    BLOCKED     // User blocked this friend
}
