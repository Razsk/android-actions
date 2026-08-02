package com.example.androidactions.domain

import com.example.androidactions.data.ActionType
import com.example.androidactions.data.TaskExecutionLog
import org.junit.Assert.assertEquals
import org.junit.Test

class ReliabilityCalculatorTest {

    private val calculator = ReliabilityCalculator()

    @Test
    fun calculateStats_withEmptyLogs_returnsDefaultOptimalStats() {
        val stats = calculator.calculateStats(emptyList())
        assertEquals(100, stats.overallReliabilityPercentage)
        assertEquals(7, stats.weeklyConsistency.size)
        assertEquals(4, stats.hourlyProductivity.size)
        assertEquals(0, stats.postponedRoutineCount)
    }

    @Test
    fun calculateStats_withLogs_calculatesCorrectPercentageAndCounts() {
        val logs = listOf(
            TaskExecutionLog(id = 1L, taskId = 10L, actionType = ActionType.COMPLETED, timestamp = 100L),
            TaskExecutionLog(id = 2L, taskId = 10L, actionType = ActionType.COMPLETED, timestamp = 200L),
            TaskExecutionLog(id = 3L, taskId = 10L, actionType = ActionType.POSTPONED, timestamp = 300L),
            TaskExecutionLog(id = 4L, taskId = 10L, actionType = ActionType.POSTPONED, timestamp = 400L)
        )

        val stats = calculator.calculateStats(logs)
        assertEquals(50, stats.overallReliabilityPercentage)
        assertEquals(2, stats.postponedRoutineCount)
    }
}
