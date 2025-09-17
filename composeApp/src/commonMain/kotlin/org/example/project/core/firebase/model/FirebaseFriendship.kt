package org.example.project.core.firebase.model

/**
 * Firebase Firestore document structure for friendships
 * Collection: "friendships"
 * Document ID: "{userId1}_{userId2}" (alphabetically ordered)
 */
data class FirebaseFriendship(
    val id: String = "",
    val userId1: String = "", // Alphabetically first user ID
    val userId2: String = "", // Alphabetically second user ID
    val status: FriendshipStatus = FriendshipStatus.PENDING,
    val requestedBy: String = "", // Who sent the friend request
    val requestedAt: Long = 0L,
    val acceptedAt: Long? = null,
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L,
    
    // Balance information
    val totalBalance: Double = 0.0, // Total balance between users
    val user1Balance: Double = 0.0, // How much user1 owes to user2 (negative) or is owed by user2 (positive)
    val user2Balance: Double = 0.0, // How much user2 owes to user1 (negative) or is owed by user1 (positive)
    
    // Metadata
    val lastExpenseAt: Long? = null,
    val expenseCount: Int = 0
) {
    companion object {
        const val COLLECTION_NAME = "friendships"
        
        // Field names for Firestore queries
        const val FIELD_USER_ID_1 = "userId1"
        const val FIELD_USER_ID_2 = "userId2"
        const val FIELD_STATUS = "status"
        const val FIELD_REQUESTED_BY = "requestedBy"
        const val FIELD_REQUESTED_AT = "requestedAt"
        const val FIELD_ACCEPTED_AT = "acceptedAt"
        const val FIELD_TOTAL_BALANCE = "totalBalance"
        const val FIELD_USER_1_BALANCE = "user1Balance"
        const val FIELD_USER_2_BALANCE = "user2Balance"
        
        /**
         * Generate friendship document ID from two user IDs
         * Always puts the alphabetically first ID first for consistency
         */
        fun generateId(userId1: String, userId2: String): String {
            return if (userId1 < userId2) {
                "${userId1}_${userId2}"
            } else {
                "${userId2}_${userId1}"
            }
        }
        
        /**
         * Create ordered user IDs for consistent document structure
         */
        fun createOrderedUserIds(userId1: String, userId2: String): Pair<String, String> {
            return if (userId1 < userId2) {
                Pair(userId1, userId2)
            } else {
                Pair(userId2, userId1)
            }
        }
    }
    
    /**
     * Get the balance for a specific user
     * Returns positive if the user is owed money, negative if they owe money
     */
    fun getBalanceForUser(userId: String): Double {
        return when (userId) {
            userId1 -> user1Balance
            userId2 -> user2Balance
            else -> 0.0
        }
    }
    
    /**
     * Get the other user ID in the friendship
     */
    fun getOtherUserId(currentUserId: String): String? {
        return when (currentUserId) {
            userId1 -> userId2
            userId2 -> userId1
            else -> null
        }
    }
    
    /**
     * Check if a user is part of this friendship
     */
    fun containsUser(userId: String): Boolean {
        return userId == userId1 || userId == userId2
    }
}

enum class FriendshipStatus {
    PENDING,    // Friend request sent but not accepted
    ACCEPTED,   // Friend request accepted
    BLOCKED,    // One user blocked the other
    DECLINED    // Friend request was declined
}
