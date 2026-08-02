package com.example.androidactions

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable data object Main : NavKey
@Serializable data object Challenges : NavKey
@Serializable data object Stats : NavKey
@Serializable data class FocusHud(val challengeId: Long) : NavKey
@Serializable data class ChallengeSummary(
    val challengeTitle: String,
    val totalTimeSeconds: Long,
    val ghostDeltaSeconds: Long
) : NavKey
