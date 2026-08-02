package com.example.androidactions.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import com.example.androidactions.FocusHud
import com.example.androidactions.domain.SuggestionCard
import com.example.androidactions.theme.ActionBlue
import com.example.androidactions.theme.CyberCyan
import com.example.androidactions.theme.SurfaceDark
import com.example.androidactions.ui.hud.GhostProgressBar
import com.example.androidactions.ui.hud.HudButton
import com.example.androidactions.ui.hud.HudCard
import com.example.androidactions.ui.taskcreate.TaskCreationBottomSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onItemClick: (NavKey) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MainScreenViewModel = MainScreenViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var showTaskCreationModal by remember { mutableStateOf(false) }

    when (state) {
        MainScreenUiState.Loading -> {}
        is MainScreenUiState.Success -> {
            val successData = state as MainScreenUiState.Success
            MissionControlScreen(
                activeChallenge = successData.activeChallenge,
                cards = successData.suggestionCards,
                onLaunchChallenge = { challengeId -> onItemClick(FocusHud(challengeId)) },
                onAcceptCard = { taskId -> viewModel.acceptSuggestionCard(taskId) },
                onOpenTaskCreation = { showTaskCreationModal = true },
                modifier = modifier
            )

            if (showTaskCreationModal) {
                TaskCreationBottomSheet(
                    onDismiss = { showTaskCreationModal = false },
                    onSaveTask = { title, tags, listName, frequencyDays ->
                        viewModel.createNewTask(title, tags, listName, frequencyDays)
                    }
                )
            }
        }
        is MainScreenUiState.Error -> {
            Text("Error: ${(state as MainScreenUiState.Error).throwable.message}")
        }
    }
}

@Composable
internal fun MissionControlScreen(
    activeChallenge: ActiveChallengeSummary?,
    cards: List<SuggestionCard>,
    onLaunchChallenge: (Long) -> Unit,
    onAcceptCard: (Long) -> Unit,
    onOpenTaskCreation: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SurfaceDark)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        // Mission Control Header Block with + Action
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "MISSION CONTROL",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "KINETIC HUD SYSTEM // ONLINE",
                    style = MaterialTheme.typography.labelSmall,
                    color = ActionBlue
                )
            }
            Box(
                modifier = Modifier
                    .background(ActionBlue.copy(alpha = 0.15f), RoundedCornerShape(4.dp))
                    .border(1.dp, ActionBlue, RoundedCornerShape(4.dp))
                    .clickable { onOpenTaskCreation() }
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "+ CREATE TASK",
                    style = MaterialTheme.typography.labelSmall,
                    color = ActionBlue
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Active Challenges HUD Section
        HudCard(title = "ACTIVE CHALLENGE PROTOCOLS") {
            if (activeChallenge != null) {
                Column {
                    Text(
                        text = activeChallenge.title,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    GhostProgressBar(progress = activeChallenge.progressFraction)
                    Spacer(modifier = Modifier.height(12.dp))
                    HudButton(
                        text = "Launch Focus HUD",
                        onClick = { onLaunchChallenge(activeChallenge.id) },
                        isPrimary = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            } else {
                Text(
                    text = "No active challenge currently running.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Gemma Optimization Cards Section
        HudCard(title = "GEMMA OPTIMIZATION PROTOCOLS") {
            if (cards.isEmpty()) {
                Text(
                    text = "No active routine drift detected. All routines on target.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    cards.forEach { card ->
                        Column {
                            Text(
                                text = card.title,
                                style = MaterialTheme.typography.labelSmall,
                                color = CyberCyan
                            )
                            Text(
                                text = card.description,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            HudButton(
                                text = card.suggestedAction,
                                onClick = { onAcceptCard(card.targetTaskId) },
                                isPrimary = false,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}
