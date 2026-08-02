package com.example.androidactions.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class BuddyAccountabilityFormatterTest {

    @Test
    fun testFormatStartMessage_containsChallengeTitleAndObjective() {
        val formatter = BuddyAccountabilityFormatter()
        val msg = formatter.formatStartMessage(challengeTitle = "Morning Power Hour", currentTask = "Meditate")

        assertTrue(msg.contains("Starting Challenge: Morning Power Hour"))
        assertTrue(msg.contains("Current Task: Meditate"))
    }

    @Test
    fun testFormatFinishMessage_containsCompletionStats() {
        val formatter = BuddyAccountabilityFormatter()
        val msg = formatter.formatFinishMessage(challengeTitle = "30-Min Reset", totalTimeSeconds = 1450L)

        assertTrue(msg.contains("Challenge Completed!"))
        assertTrue(msg.contains("30-Min Reset"))
    }

    @Test
    fun testFormatTimeoutMessage_containsTimeoutStats() {
        val formatter = BuddyAccountabilityFormatter()
        val msg = formatter.formatTimeoutMessage(challengeTitle = "30-Min Reset", completedTasks = 2, totalTasks = 5)

        assertTrue(msg.contains("Challenge Timed Out: 30-Min Reset"))
        assertTrue(msg.contains("2/5 tasks"))
    }
}

