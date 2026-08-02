package com.example.androidactions.domain

import com.example.androidactions.data.SplitTimeEntity

class GhostPaceCalculator {

    fun calculateDeltaSeconds(currentDurationSeconds: Long, personalBest: SplitTimeEntity?): Long {
        if (personalBest == null) return 0L
        return currentDurationSeconds - personalBest.splitDurationSeconds
    }
}
