package org.example.project.core.utils

import kotlinx.datetime.Clock

/**
 * Utility object for time-related operations in Kotlin Multiplatform
 * Uses kotlinx-datetime for cross-platform compatibility
 */
object TimeUtils {
    /**
     * Get current timestamp in milliseconds
     * Compatible with all Kotlin Multiplatform targets
     */
    fun currentTimeMillis(): Long {
        return Clock.System.now().toEpochMilliseconds()
    }
    
    /**
     * Get timestamp from days ago
     */
    fun daysAgo(days: Int): Long {
        return currentTimeMillis() - (days * 24 * 60 * 60 * 1000L)
    }
    
    /**
     * Get timestamp from hours ago
     */
    fun hoursAgo(hours: Int): Long {
        return currentTimeMillis() - (hours * 60 * 60 * 1000L)
    }
    
    /**
     * Get timestamp from minutes ago
     */
    fun minutesAgo(minutes: Int): Long {
        return currentTimeMillis() - (minutes * 60 * 1000L)
    }
}
