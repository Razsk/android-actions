package com.example.androidactions.ui.main

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
}

private class FakeMyModelRepository : DataRepository {
    override val data: Flow<List<String>> = flow { emit(listOf("Sample")) }
}
