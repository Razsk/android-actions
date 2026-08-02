package com.example.androidactions.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidactions.data.ActionType
import com.example.androidactions.data.DataRepository
import com.example.androidactions.data.DefaultDataRepository
import com.example.androidactions.data.TaskExecutionLog
import com.example.androidactions.domain.OptimizationCardAnalyzer
import com.example.androidactions.domain.SuggestionCard
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class ActiveChallengeSummary(
    val id: Long,
    val title: String,
    val progressFraction: Float
)

class MainScreenViewModel(
    dataRepository: DataRepository = DefaultDataRepository(),
    private val cardAnalyzer: OptimizationCardAnalyzer = OptimizationCardAnalyzer()
) : ViewModel() {

    private val _acceptedCards = MutableStateFlow<Set<Long>>(emptySet())

    val uiState: StateFlow<MainScreenUiState> =
        combine(dataRepository.data, _acceptedCards) { data, accepted ->
            val mockLogs = listOf(
                TaskExecutionLog(id = 1L, taskId = 10L, actionType = ActionType.POSTPONED, timestamp = 100L),
                TaskExecutionLog(id = 2L, taskId = 10L, actionType = ActionType.POSTPONED, timestamp = 200L),
                TaskExecutionLog(id = 3L, taskId = 10L, actionType = ActionType.POSTPONED, timestamp = 300L)
            )
            val detectedCard = cardAnalyzer.analyzeLogs(taskId = 10L, logs = mockLogs)
            val activeCards = detectedCard?.let { listOf(it) } ?: emptyList()

            MainScreenUiState.Success(
                activeChallenge = ActiveChallengeSummary(
                    id = 1L,
                    title = "30-MIN APARTMENT RESET",
                    progressFraction = 0.4f
                ),
                suggestionCards = activeCards.filterNot { accepted.contains(it.targetTaskId) }
            ) as MainScreenUiState
        }
        .catch { emit(MainScreenUiState.Error(it)) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), MainScreenUiState.Loading)

    fun acceptSuggestionCard(taskId: Long) {
        _acceptedCards.value = _acceptedCards.value + taskId
    }
}

sealed interface MainScreenUiState {
    object Loading : MainScreenUiState
    data class Error(val throwable: Throwable) : MainScreenUiState
    data class Success(
        val activeChallenge: ActiveChallengeSummary?,
        val suggestionCards: List<SuggestionCard>
    ) : MainScreenUiState
}
