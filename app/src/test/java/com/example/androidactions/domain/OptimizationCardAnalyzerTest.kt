package com.example.androidactions.domain

import com.example.androidactions.data.ActionType
import com.example.androidactions.data.TaskExecutionLog
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class OptimizationCardAnalyzerTest {

    @Test
    fun testAnalyzePostponementHistory_triggersSuggestionCard_whenPostponed3Times() {
        val analyzer = OptimizationCardAnalyzer()
        val logs = listOf(
            TaskExecutionLog(id = 1L, taskId = 10L, actionType = ActionType.POSTPONED.name, timestamp = 100L),
            TaskExecutionLog(id = 2L, taskId = 10L, actionType = ActionType.POSTPONED.name, timestamp = 200L),
            TaskExecutionLog(id = 3L, taskId = 10L, actionType = ActionType.POSTPONED.name, timestamp = 300L)
        )

        val card = analyzer.analyzeLogs(taskId = 10L, logs = logs)
        assertNotNull(card)
        assertEquals("Optimization Protocol", card?.title)
        assertEquals(10L, card?.targetTaskId)
    }

    @Test
    fun testAnalyzePostponementHistory_returnsNull_whenPostponedLessThan3Times() {
        val analyzer = OptimizationCardAnalyzer()
        val logs = listOf(
            TaskExecutionLog(id = 1L, taskId = 10L, actionType = ActionType.POSTPONED.name, timestamp = 100L),
            TaskExecutionLog(id = 2L, taskId = 10L, actionType = ActionType.COMPLETED.name, timestamp = 200L)
        )

        val card = analyzer.analyzeLogs(taskId = 10L, logs = logs)
        assertNull(card)
    }
}
