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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import com.example.androidactions.data.TaskEntity
import com.example.androidactions.domain.SuggestionCard
import com.example.androidactions.theme.ActionBlue
import com.example.androidactions.theme.CyberCyan
import com.example.androidactions.theme.SurfaceContainer
import com.example.androidactions.theme.SurfaceDark
import com.example.androidactions.ui.hud.GhostProgressBar
import com.example.androidactions.ui.hud.HudButton
import com.example.androidactions.ui.hud.HudCard
import com.example.androidactions.ui.postpone.PostponeDeferralBottomSheet
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
    var selectedTaskToPostpone by remember { mutableStateOf<TaskEntity?>(null) }

    when (state) {
        MainScreenUiState.Loading -> {}
        is MainScreenUiState.Success -> {
            val successData = state as MainScreenUiState.Success
            Scaffold(
                modifier = modifier,
                containerColor = SurfaceDark,
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = { showTaskCreationModal = true },
                        containerColor = ActionBlue,
                        contentColor = SurfaceDark
                    ) {
                        Text(
                            text = "+",
                            style = MaterialTheme.typography.headlineLarge
                        )
                    }
                }
            ) { padding ->
                MissionControlScreen(
                    activeChallenge = successData.activeChallenge,
                    dueTasks = successData.createdTasks,
                    cards = successData.suggestionCards,
                    onLaunchChallenge = { challengeId -> onItemClick(FocusHud(challengeId)) },
                    onAcceptCard = { taskId -> viewModel.acceptSuggestionCard(taskId) },
                    onOpenTaskCreation = { showTaskCreationModal = true },
                    onPostponeClick = { task ->
                        if (task.defaultPeriodDays > 0) {
                            viewModel.postponeRoutine(task.id, task.defaultPeriodDays)
                        } else {
                            selectedTaskToPostpone = task
                        }
                    },
                    modifier = Modifier.padding(padding)
                )

                if (showTaskCreationModal) {
                    TaskCreationBottomSheet(
                        onDismiss = { showTaskCreationModal = false },
                        onSaveTask = { title, tags, listName, frequencyDays, isReusable ->
                            viewModel.createNewTask(title, tags, listName, frequencyDays, isReusable)
                        }
                    )
                }

                selectedTaskToPostpone?.let { task ->
                    PostponeDeferralBottomSheet(
                        taskTitle = task.title,
                        onDismiss = { selectedTaskToPostpone = null },
                        onConfirmDeferral = { days ->
                            viewModel.postponeRoutine(task.id, days)
                            selectedTaskToPostpone = null
                        }
                    )
                }
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
    dueTasks: List<TaskEntity>,
    cards: List<SuggestionCard>,
    onLaunchChallenge: (Long) -> Unit,
    onAcceptCard: (Long) -> Unit,
    onOpenTaskCreation: () -> Unit,
    onPostponeClick: (TaskEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SurfaceDark)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        // Mission Control Header Block with Inviting Action Blue Button
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
                    .background(ActionBlue, RoundedCornerShape(4.dp))
                    .clickable { onOpenTaskCreation() }
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Text(
                    text = "+ CREATE TASK",
                    style = MaterialTheme.typography.labelSmall,
                    color = SurfaceDark
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Prominent Due Objectives Section (Immediate Focus)
        HudCard(title = "DUE OBJECTIVES PROTOCOLS") {
            val sampleDueTasks = if (dueTasks.isNotEmpty()) dueTasks else listOf(
                TaskEntity(id = 101L, title = "Morning Meditation & Breathing", defaultPeriodDays = 1, listName = "Health", tagsCsv = "Health,Routine"),
                TaskEntity(id = 102L, title = "Review Daily Code Commit Logs", defaultPeriodDays = 0, listName = "Work", tagsCsv = "Work")
            )
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                sampleDueTasks.forEach { task ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(SurfaceContainer, RoundedCornerShape(4.dp))
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            Checkbox(
                                checked = false,
                                onCheckedChange = { },
                                colors = CheckboxDefaults.colors(
                                    uncheckedColor = CyberCyan,
                                    checkedColor = ActionBlue
                                )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = task.title,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "${task.listName.uppercase()} // TAGS: ${task.tagsCsv}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        Box(
                            modifier = Modifier
                                .border(1.dp, Color(0x338D90A0), RoundedCornerShape(4.dp))
                                .clickable { onPostponeClick(task) }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "POSTPONE",
                                style = MaterialTheme.typography.labelSmall,
                                color = ActionBlue
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

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
