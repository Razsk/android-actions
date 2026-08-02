package com.example.androidactions.domain

import com.example.androidactions.data.ChallengeEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class FocusHudModeTest {

    @Test
    fun testChallengeProgress_advancesTaskIndex() {
        val engine = ChallengeEngine()
        val challenge = ChallengeEntity(
            id = 5L,
            title = "30-Min Apartment Reset",
            description = "Quick apartment cleanup",
            totalTimeBudgetMinutes = 30
        )

        val state = engine.startChallenge(challenge, totalTasks = 3)
        assertEquals(0, state.activeTaskIndex)
        assertEquals(false, state.isFinished)

        val nextState = engine.advanceTask(state)
        assertEquals(1, nextState.activeTaskIndex)
        assertEquals(false, nextState.isFinished)
    }
}
