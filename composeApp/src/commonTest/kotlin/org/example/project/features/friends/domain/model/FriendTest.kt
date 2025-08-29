package org.example.project.features.friends.domain.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNull

class FriendTest {

    @Test
    fun `defaults are correctly set`() {
        val friend = Friend(
            userId = "u1",
            name = "John Doe"
        )

        assertEquals("u1", friend.userId)
        assertEquals("John Doe", friend.name)
        assertNull(friend.serverId)
        assertNull(friend.updatedAtEpochMillis)
        assertEquals(null, friend.avatarUrl)
        assertEquals(0.0, friend.totalBalance)
        assertEquals(0.0, friend.personalBalance)
        assertEquals(emptyMap(), friend.groupBalances)
        assertEquals(emptyMap(), friend.metadata)
    }

    @Test
    fun `values are assigned as provided`() {
        val groupBal = mapOf("g1" to 10.0, "g2" to -5.5)
        val metadata = mapOf("note" to "college friend")
        val friend = Friend(
            userId = "u2",
            serverId = "srv-123",
            name = "Maria",
            avatarUrl = "https://example.com/a.png",
            totalBalance = -12.34,
            personalBalance = 7.89,
            groupBalances = groupBal,
            metadata = metadata,
            updatedAtEpochMillis = 1724880000000
        )

        assertEquals("u2", friend.userId)
        assertEquals("srv-123", friend.serverId)
        assertEquals("Maria", friend.name)
        assertEquals("https://example.com/a.png", friend.avatarUrl)
        assertEquals(-12.34, friend.totalBalance)
        assertEquals(7.89, friend.personalBalance)
        assertEquals(groupBal, friend.groupBalances)
        assertEquals(metadata, friend.metadata)
        assertEquals(1724880000000, friend.updatedAtEpochMillis)
    }

    @Test
    fun `data class equality and copy work as expected`() {
        val base = Friend(userId = "u3", name = "Ana", totalBalance = 5.0)
        val same = Friend(userId = "u3", name = "Ana", totalBalance = 5.0)
        val different = Friend(userId = "u3", name = "Ana", totalBalance = -5.0)

        // equals based on structural equality
        assertEquals(base, same)
        assertNotEquals(base, different)

        // copy with modifications
        val modified = base.copy(totalBalance = -2.5, serverId = "srv-9")
        assertEquals("u3", modified.userId)
        assertEquals("Ana", modified.name)
        assertEquals(-2.5, modified.totalBalance)
        assertEquals("srv-9", modified.serverId)

        // original remains unchanged
        assertEquals(5.0, base.totalBalance)
        assertNull(base.serverId)
    }
}
