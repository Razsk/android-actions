package com.example.androidactions.domain

import com.example.androidactions.data.RoutineEntity
import com.example.androidactions.data.TaskExecutionLog
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RoutineSchedulerTest {

    @Test
    fun testCompleteRoutine_reschedulesDefaultPeriod() {
        val scheduler = RoutineScheduler()
        val routine = RoutineEntity(id = 1L, taskId = 10L, frequencyDays = 3, dueTimestamp = 1000L, isPostponed = false)
        val updated = scheduler.completeRoutine(routine, currentTimestamp = 1000L)

        assertEquals(1000L + (3 * 86400000L), updated.dueTimestamp)
        assertEquals(false, updated.isPostponed)
    }

    @Test
    fun testPostponeRoutine_reschedulesCustomPeriod() {
        val scheduler = RoutineScheduler()
        val routine = RoutineEntity(id = 1L, taskId = 10L, frequencyDays = 3, dueTimestamp = 1000L, isPostponed = false)
        val updated = scheduler.postponeRoutine(routine, currentTimestamp = 1000L, deferralDays = 1)

        assertEquals(1000L + (1 * 86400000L), updated.dueTimestamp)
        assertEquals(true, updated.isPostponed)
    }
}
