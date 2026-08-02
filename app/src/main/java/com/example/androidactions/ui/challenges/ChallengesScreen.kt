package com.example.androidactions.ui.challenges

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.androidactions.data.ChallengeEntity
import com.example.androidactions.theme.ActionBlue
import com.example.androidactions.theme.CyberCyan
import com.example.androidactions.theme.SurfaceDark
import com.example.androidactions.ui.hud.HudButton
import com.example.androidactions.ui.hud.HudCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChallengesScreen(
    onLaunchChallenge: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var showCreateModal by remember { mutableStateOf(false) }
    var challenges by remember {
        mutableStateOf(
            listOf(
                ChallengeEntity(id = 1L, title = "30-MIN APARTMENT RESET", description = "Speedrun cleaning and resets", totalTimeBudgetMinutes = 30),
                ChallengeEntity(id = 2L, title = "MORNING ROUTINE SPEEDRUN", description = "Morning focus and routine reset", totalTimeBudgetMinutes = 15),
                ChallengeEntity(id = 3L, title = "DEEP WORK SHIFT PROTOCOL", description = "High focus task execution", totalTimeBudgetMinutes = 60)
            )
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SurfaceDark)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "CHALLENGES LIBRARY",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "TIMED SPEEDRUN PROTOCOLS // ACTIVE",
                    style = MaterialTheme.typography.labelSmall,
                    color = ActionBlue
                )
            }
            Box(
                modifier = Modifier
                    .background(ActionBlue.copy(alpha = 0.15f), RoundedCornerShape(4.dp))
                    .border(1.dp, ActionBlue, RoundedCornerShape(4.dp))
                    .clickable { showCreateModal = true }
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "+ NEW CHALLENGE",
                    style = MaterialTheme.typography.labelSmall,
                    color = ActionBlue
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Grid of Challenge Cards
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            challenges.forEach { challenge ->
                HudCard(title = challenge.title.uppercase()) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "BUDGET: ${challenge.totalTimeBudgetMinutes} MINS",
                                style = MaterialTheme.typography.labelSmall,
                                color = CyberCyan
                            )
                            Text(
                                text = challenge.description,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        HudButton(
                            text = "Launch Focus HUD",
                            onClick = { onLaunchChallenge(challenge.id) },
                            isPrimary = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }

        if (showCreateModal) {
            CreateCustomChallengeBottomSheet(
                onDismiss = { showCreateModal = false },
                onCreateChallenge = { title, mins ->
                    val newChallenge = ChallengeEntity(
                        id = System.currentTimeMillis(),
                        title = title.uppercase(),
                        description = "Custom speedrun protocol",
                        totalTimeBudgetMinutes = mins
                    )
                    challenges = challenges + newChallenge
                    showCreateModal = false
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CreateCustomChallengeBottomSheet(
    onDismiss: () -> Unit,
    onCreateChallenge: (title: String, targetMinutes: Int) -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState()
) {
    var title by remember { mutableStateOf("") }
    var minutesText by remember { mutableStateOf("30") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = SurfaceDark
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Text(
                text = "CREATE CUSTOM CHALLENGE PROTOCOL",
                style = MaterialTheme.typography.labelSmall,
                color = ActionBlue
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Challenge Title", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CyberCyan,
                    unfocusedBorderColor = Color(0x338D90A0),
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = minutesText,
                onValueChange = { minutesText = it },
                label = { Text("Time Budget (Minutes)", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CyberCyan,
                    unfocusedBorderColor = Color(0x338D90A0),
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                HudButton(
                    text = "Cancel",
                    onClick = onDismiss,
                    isPrimary = false,
                    modifier = Modifier.weight(1f)
                )
                HudButton(
                    text = "Create Protocol",
                    onClick = {
                        if (title.isNotBlank()) {
                            val mins = minutesText.toIntOrNull() ?: 30
                            onCreateChallenge(title.trim(), mins)
                        }
                    },
                    isPrimary = true,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
