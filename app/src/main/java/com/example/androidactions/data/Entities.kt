package com.example.androidactions.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val isReusable: Boolean = false,
    val defaultPeriodDays: Int = 1,
    val listName: String = "Default",
    val tagsCsv: String = ""
) {
    val tags: List<String>
        get() = if (tagsCsv.isEmpty()) emptyList() else tagsCsv.split(",")
}

@Entity(tableName = "routines")
data class RoutineEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val taskId: Long,
    val frequencyDays: Int = 1,
    val dueTimestamp: Long,
    val isPostponed: Boolean = false
)

@Entity(tableName = "task_execution_logs")
data class TaskExecutionLog(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val taskId: Long,
    val actionType: String,
    val timestamp: Long
)

@Entity(tableName = "challenges")
data class ChallengeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String,
    val totalTimeBudgetMinutes: Int,
    val personalBestSeconds: Long? = null
)

@Entity(tableName = "split_times")
data class SplitTimeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val challengeId: Long,
    val taskId: Long,
    val splitDurationSeconds: Long,
    val recordedTimestamp: Long
)
