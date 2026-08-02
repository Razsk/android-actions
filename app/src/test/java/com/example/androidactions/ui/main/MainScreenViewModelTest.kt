package com.example.androidactions.ui.main

import com.example.androidactions.data.ActionType
import com.example.androidactions.data.DataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MainScreenViewModelTest {

    @Test
    fun uiState_emitsSuccessWithActiveChallengeAndCards() = runTest {
        val viewModel = MainScreenViewModel(FakeMyModelRepository())
        val state = viewModel.uiState.filter { it is MainScreenUiState.Success }.first()
        assertTrue(state is MainScreenUiState.Success)
        val successState = state as MainScreenUiState.Success
        assertEquals("30-MIN APARTMENT RESET", successState.activeChallenge?.title)
        assertEquals(1, successState.suggestionCards.size)
    }

    @Test
    fun acceptSuggestionCard_removesCardFromState() = runTest {
        val viewModel = MainScreenViewModel(FakeMyModelRepository())
        val initialState = viewModel.uiState.filter { it is MainScreenUiState.Success }.first() as MainScreenUiState.Success
        assertEquals(1, initialState.suggestionCards.size)

        viewModel.acceptSuggestionCard(taskId = 10L)

        val updatedState = viewModel.uiState.filter { it is MainScreenUiState.Success }.first() as MainScreenUiState.Success
        assertEquals(0, updatedState.suggestionCards.size)
    }

    @Test
    fun createNewTask_addsTaskAndRoutineToState() = runTest {
        val viewModel = MainScreenViewModel(FakeMyModelRepository())
        val initialState = viewModel.uiState.filter { it is MainScreenUiState.Success }.first() as MainScreenUiState.Success
        assertEquals(0, initialState.createdTasks.size)
        assertEquals(0, initialState.createdRoutines.size)

        viewModel.createNewTask("Morning Yoga", listOf("Health"), "Fitness", 1)

        val updatedState = viewModel.uiState.filter {
            it is MainScreenUiState.Success && it.createdTasks.isNotEmpty() && it.createdRoutines.isNotEmpty()
        }.first() as MainScreenUiState.Success

        assertEquals(1, updatedState.createdTasks.size)
        assertEquals(1, updatedState.createdRoutines.size)
        assertEquals("Morning Yoga", updatedState.createdTasks.first().title)
        assertEquals(1, updatedState.createdRoutines.first().frequencyDays)
    }

    @Test
    fun completeTask_routineReschedulesNextDueTimestamp() = runTest {
        val viewModel = MainScreenViewModel(FakeMyModelRepository())
        viewModel.createNewTask("Descaling Coffee Machine", listOf("Home"), "Default", 90)

        val createdState = viewModel.uiState.filter {
            it is MainScreenUiState.Success && it.createdTasks.isNotEmpty()
        }.first() as MainScreenUiState.Success

        val taskId = createdState.createdTasks.first().id
        viewModel.completeTask(taskId, isChecked = true)

        val updatedState = viewModel.uiState.filter {
            it is MainScreenUiState.Success && it.completedTaskIds.contains(taskId)
        }.first() as MainScreenUiState.Success

        assertEquals(1, updatedState.completedTaskIds.size)
        val routine = updatedState.createdRoutines.find { it.taskId == taskId }
        assertTrue(routine != null)
        assertTrue(routine!!.dueTimestamp > System.currentTimeMillis())
        assertEquals(false, routine.isPostponed)
    }

    @Test
    fun postponeRoutine_recordsExecutionLogWithPostponedAction() = runTest {
        val viewModel = MainScreenViewModel(FakeMyModelRepository())
        val initialState = viewModel.uiState.filter { it is MainScreenUiState.Success }.first() as MainScreenUiState.Success
        assertEquals(0, initialState.postponedLogs.size)

        viewModel.postponeRoutine(taskId = 101L, deferDays = 3)

        val updatedState = viewModel.uiState.filter {
            it is MainScreenUiState.Success && it.postponedLogs.isNotEmpty()
        }.first() as MainScreenUiState.Success

        assertEquals(1, updatedState.postponedLogs.size)
        assertEquals(101L, updatedState.postponedLogs.first().taskId)
        assertEquals(ActionType.POSTPONED, updatedState.postponedLogs.first().actionType)
    }
}

private class FakeMyModelRepository : DataRepository {
    override val data: Flow<List<String>> = flow { emit(listOf("Sample")) }
}
