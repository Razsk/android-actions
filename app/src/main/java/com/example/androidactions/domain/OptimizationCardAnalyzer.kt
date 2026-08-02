package com.example.androidactions.domain

import com.example.androidactions.data.ActionType
import com.example.androidactions.data.TaskExecutionLog

data class SuggestionCard(
    val id: Long = 0,
    val targetTaskId: Long,
    val title: String,
    val description: String,
    val suggestedAction: String
)

class OptimizationCardAnalyzer {

    fun analyzeLogs(taskId: Long, logs: List<TaskExecutionLog>): SuggestionCard? {
        val consecutivePostponements = logs.takeWhile { it.actionType == ActionType.POSTPONED.name }.size
        if (consecutivePostponements >= 3) {
            return SuggestionCard(
                targetTaskId = taskId,
                title = "Optimization Protocol",
                description = "Task has been postponed 3 times in a row. Consider shifting start time or frequency.",
                suggestedAction = "Shift Frequency +1 Day"
            )
        }
        return null
    }
}
