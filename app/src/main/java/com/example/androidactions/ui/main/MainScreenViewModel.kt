package com.example.androidactions.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidactions.data.ActionType
import com.example.androidactions.data.DataRepository
import com.example.androidactions.data.DefaultDataRepository
import com.example.androidactions.data.RoutineEntity
import com.example.androidactions.data.TaskEntity
import com.example.androidactions.data.TaskExecutionLog
import com.example.androidactions.domain.OptimizationCardAnalyzer
import com.example.androidactions.domain.SuggestionCard
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ActiveChallengeSummary(
    val id: Long,
    val title: String,
    val progressFraction: Float
)

class MainScreenViewModel(
    private val dataRepository: DataRepository = DefaultDataRepository(),
    private val cardAnalyzer: OptimizationCardAnalyzer = OptimizationCardAnalyzer()
) : ViewModel() {

    private val _acceptedCards = MutableStateFlow<Set<Long>>(emptySet())
    private val _createdTasks = MutableStateFlow<List<TaskEntity>>(emptyList())
    private val _completedTaskIds = MutableStateFlow<Set<Long>>(emptySet())
    private val _createdRoutines = MutableStateFlow<List<RoutineEntity>>(emptyList())
    private val _postponedLogs = MutableStateFlow<List<TaskExecutionLog>>(emptyList())

    val uiState: StateFlow<MainScreenUiState> =
        combine(_acceptedCards, _createdTasks, _completedTaskIds, _createdRoutines, _postponedLogs) { accepted, createdTasks, completedIds, createdRoutines, postponed ->
            val allLogs = listOf(
                TaskExecutionLog(id = 1L, taskId = 10L, actionType = ActionType.POSTPONED, timestamp = 100L),
                TaskExecutionLog(id = 2L, taskId = 10L, actionType = ActionType.POSTPONED, timestamp = 200L),
                TaskExecutionLog(id = 3L, taskId = 10L, actionType = ActionType.POSTPONED, timestamp = 300L)
            ) + postponed

            val detectedCard = cardAnalyzer.analyzeLogs(taskId = 10L, logs = allLogs)
            val activeCards = detectedCard?.let { listOf(it) } ?: emptyList()

            val activeDueTasks = createdTasks.filterNot { completedIds.contains(it.id) }

            MainScreenUiState.Success(
                activeChallenge = ActiveChallengeSummary(
                    id = 1L,
                    title = "30-MIN APARTMENT RESET",
                    progressFraction = 0.4f
                ),
                suggestionCards = activeCards.filterNot { accepted.contains(it.targetTaskId) },
                createdTasks = activeDueTasks,
                completedTaskIds = completedIds,
                createdRoutines = createdRoutines,
                postponedLogs = postponed
            ) as MainScreenUiState
        }
        .catch { emit(MainScreenUiState.Error(it)) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), MainScreenUiState.Loading)

    fun acceptSuggestionCard(taskId: Long) {
        _acceptedCards.value = _acceptedCards.value + taskId
    }

    fun completeTask(taskId: Long, isChecked: Boolean) {
        viewModelScope.launch {
            if (isChecked) {
                _completedTaskIds.value = _completedTaskIds.value + taskId
                val log = TaskExecutionLog(
                    id = System.currentTimeMillis(),
                    taskId = taskId,
                    actionType = ActionType.COMPLETED,
                    timestamp = System.currentTimeMillis()
                )
                _postponedLogs.value = _postponedLogs.value + log

                // Automatic Rescheduling: If task is a routine (frequencyDays > 0), reschedule next dueTimestamp
                val task = _createdTasks.value.find { it.id == taskId }
                if (task != null && task.defaultPeriodDays > 0) {
                    val nextDueTime = System.currentTimeMillis() + TimeUnit.DAYS.toMillis(task.defaultPeriodDays.toLong())
                    val updatedRoutines = _createdRoutines.value.map { routine ->
                        if (routine.taskId == taskId) {
                            routine.copy(
                                dueTimestamp = nextDueTime,
                                isPostponed = false
                            )
                        } else {
                            routine
                        }
                    }
                    _createdRoutines.value = updatedRoutines
                }
            } else {
                _completedTaskIds.value = _completedTaskIds.value - taskId
            }
        }
    }

    fun postponeRoutine(taskId: Long, deferDays: Int) {
        viewModelScope.launch {
            val log = TaskExecutionLog(
                id = System.currentTimeMillis(),
                taskId = taskId,
                actionType = ActionType.POSTPONED,
                timestamp = System.currentTimeMillis()
            )
            _postponedLogs.value = _postponedLogs.value + log

            // Update routine due timestamp if exists
            val updatedRoutines = _createdRoutines.value.map { routine ->
                if (routine.taskId == taskId) {
                    routine.copy(
                        dueTimestamp = System.currentTimeMillis() + TimeUnit.DAYS.toMillis(deferDays.toLong()),
                        isPostponed = true
                    )
                } else {
                    routine
                }
            }
            _createdRoutines.value = updatedRoutines
        }
    }

    fun createNewTask(title: String, tags: List<String>, listName: String, frequencyDays: Int, isReusable: Boolean = true) {
        viewModelScope.launch {
            val taskId = System.currentTimeMillis()
            val newTask = TaskEntity(
                id = taskId,
                title = title,
                isReusable = isReusable,
                defaultPeriodDays = frequencyDays,
                listName = listName,
                tagsCsv = tags.joinToString(",")
            )
            _createdTasks.value = _createdTasks.value + newTask

            if (frequencyDays > 0) {
                val dueTime = System.currentTimeMillis() + TimeUnit.DAYS.toMillis(frequencyDays.toLong())
                val newRoutine = RoutineEntity(
                    taskId = taskId,
                    frequencyDays = frequencyDays,
                    dueTimestamp = dueTime,
                    isPostponed = false
                )
                _createdRoutines.value = _createdRoutines.value + newRoutine
            }
        }
    }
}

sealed interface MainScreenUiState {
    object Loading : MainScreenUiState
    data class Error(val throwable: Throwable) : MainScreenUiState
    data class Success(
        val activeChallenge: ActiveChallengeSummary?,
        val suggestionCards: List<SuggestionCard>,
        val createdTasks: List<TaskEntity> = emptyList(),
        val completedTaskIds: Set<Long> = emptySet(),
        val createdRoutines: List<RoutineEntity> = emptyList(),
        val postponedLogs: List<TaskExecutionLog> = emptyList()
    ) : MainScreenUiState
}
