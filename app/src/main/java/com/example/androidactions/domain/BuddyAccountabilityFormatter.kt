package com.example.androidactions.domain

class BuddyAccountabilityFormatter {

    fun formatStartMessage(challengeTitle: String, currentTask: String): String {
        return "Starting Challenge: $challengeTitle. Current Task: $currentTask. Wish me luck!"
    }

    fun formatFinishMessage(challengeTitle: String, totalTimeSeconds: Long): String {
        val minutes = totalTimeSeconds / 60
        return "Challenge Completed! $challengeTitle finished in $minutes mins."
    }
}
