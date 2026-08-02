package com.example.androidactions.ui.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidactions.data.ActionType
import com.example.androidactions.data.TaskExecutionLog
import com.example.androidactions.domain.ReliabilityCalculator
import com.example.androidactions.domain.ReliabilityStats
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class StatsViewModel(
    private val calculator: ReliabilityCalculator = ReliabilityCalculator()
) : ViewModel() {

    private val _executionLogs = MutableStateFlow(
        listOf(
            TaskExecutionLog(id = 1L, taskId = 101L, actionType = ActionType.COMPLETED, timestamp = 1000L),
            TaskExecutionLog(id = 2L, taskId = 101L, actionType = ActionType.COMPLETED, timestamp = 2000L),
            TaskExecutionLog(id = 3L, taskId = 102L, actionType = ActionType.POSTPONED, timestamp = 3000L),
            TaskExecutionLog(id = 4L, taskId = 101L, actionType = ActionType.COMPLETED, timestamp = 4000L)
        )
    )

    val uiState: StateFlow<StatsUiState> =
        _executionLogs
            .map { logs ->
                val stats = calculator.calculateStats(logs)
                StatsUiState.Success(stats) as StatsUiState
            }
            .catch { emit(StatsUiState.Error(it)) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), StatsUiState.Loading)
}

sealed interface StatsUiState {
    object Loading : StatsUiState
    data class Error(val throwable: Throwable) : StatsUiState
    data class Success(val stats: ReliabilityStats) : StatsUiState
}
