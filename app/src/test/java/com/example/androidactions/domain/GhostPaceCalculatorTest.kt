package com.example.androidactions.domain

import com.example.androidactions.data.SplitTimeEntity
import org.junit.Assert.assertEquals
import org.junit.Test

class GhostPaceCalculatorTest {

    @Test
    fun testCalculateGhostPace_returnsAheadDelta_whenCurrentFasterThanPersonalBest() {
        val calculator = GhostPaceCalculator()
        val pbSplit = SplitTimeEntity(id = 1L, challengeId = 5L, taskId = 10L, splitDurationSeconds = 120L, recordedTimestamp = 1000L)

        val deltaSeconds = calculator.calculateDeltaSeconds(currentDurationSeconds = 105L, personalBest = pbSplit)
        assertEquals(-15L, deltaSeconds) // 15 seconds ahead of PB
    }

    @Test
    fun testCalculateGhostPace_returnsBehindDelta_whenCurrentSlowerThanPersonalBest() {
        val calculator = GhostPaceCalculator()
        val pbSplit = SplitTimeEntity(id = 1L, challengeId = 5L, taskId = 10L, splitDurationSeconds = 120L, recordedTimestamp = 1000L)

        val deltaSeconds = calculator.calculateDeltaSeconds(currentDurationSeconds = 130L, personalBest = pbSplit)
        assertEquals(10L, deltaSeconds) // 10 seconds behind PB
    }
}
