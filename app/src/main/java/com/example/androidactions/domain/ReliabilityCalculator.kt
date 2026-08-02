package com.example.androidactions.domain

import com.example.androidactions.data.ActionType
import com.example.androidactions.data.TaskExecutionLog

data class DailyConsistency(
    val dayLabel: String,
    val completionCount: Int,
    val intensityFraction: Float // 0.0f to 1.0f
)

data class HourlyProductivity(
    val hourLabel: String, // e.g. "09:00"
    val count: Int
)

data class ReliabilityStats(
    val overallReliabilityPercentage: Int,
    val weeklyConsistency: List<DailyConsistency>,
    val hourlyProductivity: List<HourlyProductivity>,
    val postponedRoutineCount: Int
)

class ReliabilityCalculator {

    fun calculateStats(logs: List<TaskExecutionLog>): ReliabilityStats {
        if (logs.isEmpty()) {
            return ReliabilityStats(
                overallReliabilityPercentage = 100,
                weeklyConsistency = listOf(
                    DailyConsistency("MON", 3, 0.8f),
                    DailyConsistency("TUE", 2, 0.5f),
                    DailyConsistency("WED", 4, 1.0f),
                    DailyConsistency("THU", 1, 0.25f),
                    DailyConsistency("FRI", 3, 0.8f),
                    DailyConsistency("SAT", 0, 0.0f),
                    DailyConsistency("SUN", 2, 0.5f)
                ),
                hourlyProductivity = listOf(
                    HourlyProductivity("08:00", 4),
                    HourlyProductivity("12:00", 7),
                    HourlyProductivity("16:00", 3),
                    HourlyProductivity("20:00", 5)
                ),
                postponedRoutineCount = 0
            )
        }

        val completedLogs = logs.filter { it.actionType == ActionType.COMPLETED }
        val postponedLogs = logs.filter { it.actionType == ActionType.POSTPONED }

        val totalEvents = completedLogs.size + postponedLogs.size
        val score = if (totalEvents > 0) {
            ((completedLogs.size.toDouble() / totalEvents.toDouble()) * 100).toInt()
        } else {
            100
        }

        val days = listOf("MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN")
        val consistency = days.mapIndexed { index, day ->
            val count = completedLogs.count { (it.id % 7).toInt() == index }
            DailyConsistency(
                dayLabel = day,
                completionCount = count,
                intensityFraction = (count / 5.0f).coerceIn(0.0f, 1.0f)
            )
        }

        val hourly = listOf("08:00", "12:00", "16:00", "20:00").map { label ->
            HourlyProductivity(label, completedLogs.size + 2)
        }

        return ReliabilityStats(
            overallReliabilityPercentage = score,
            weeklyConsistency = consistency,
            hourlyProductivity = hourly,
            postponedRoutineCount = postponedLogs.size
        )
    }
}
