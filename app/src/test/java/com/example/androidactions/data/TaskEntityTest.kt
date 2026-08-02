package com.example.androidactions.data

import org.junit.Assert.assertEquals
import org.junit.Test

class TaskEntityTest {
    @Test
    fun testTaskEntityCreation() {
        val task = TaskEntity(
            id = 101L,
            title = "Daily Supplements",
            isReusable = true,
            defaultPeriodDays = 1,
            tagsCsv = "Health,Morning",
            listName = "Routines"
        )
        assertEquals(101L, task.id)
        assertEquals("Daily Supplements", task.title)
        assertEquals(true, task.isReusable)
        assertEquals(2, task.tags.size)
    }
}
