package com.example.androidactions.domain

import com.example.androidactions.data.ChallengeEntity

data class ChallengeState(
    val challenge: ChallengeEntity,
    val activeTaskIndex: Int = 0,
    val totalTasks: Int = 0,
    val isFinished: Boolean = false
)

class ChallengeEngine {

    fun startChallenge(challenge: ChallengeEntity, totalTasks: Int): ChallengeState {
        return ChallengeState(
            challenge = challenge,
            activeTaskIndex = 0,
            totalTasks = totalTasks,
            isFinished = false
        )
    }

    fun advanceTask(state: ChallengeState): ChallengeState {
        val nextIndex = state.activeTaskIndex + 1
        val finished = nextIndex >= state.totalTasks
        return state.copy(
            activeTaskIndex = if (finished) state.activeTaskIndex else nextIndex,
            isFinished = finished
        )
    }
}
