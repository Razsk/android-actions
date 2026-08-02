package com.example.androidactions.domain

import com.example.androidactions.data.RoutineEntity

class RoutineScheduler {

    fun completeRoutine(routine: RoutineEntity, currentTimestamp: Long): RoutineEntity {
        val millisInDay = 86400000L
        val nextDue = currentTimestamp + (routine.frequencyDays * millisInDay)
        return routine.copy(
            dueTimestamp = nextDue,
            isPostponed = false
        )
    }

    fun postponeRoutine(routine: RoutineEntity, currentTimestamp: Long, deferralDays: Int): RoutineEntity {
        val millisInDay = 86400000L
        val nextDue = currentTimestamp + (deferralDays * millisInDay)
        return routine.copy(
            dueTimestamp = nextDue,
            isPostponed = true
        )
    }
}
