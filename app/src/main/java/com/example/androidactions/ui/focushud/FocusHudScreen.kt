package com.example.androidactions.ui.focushud

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androidactions.theme.CyberCyan
import com.example.androidactions.theme.SurfaceContainer
import com.example.androidactions.theme.SurfaceDark
import com.example.androidactions.ui.hud.GhostProgressBar
import com.example.androidactions.ui.hud.HudButton

@Composable
fun FocusHudScreen(
    challengeTitle: String,
    activeTaskTitle: String,
    remainingSeconds: Long,
    activeTaskIndex: Int,
    totalTasks: Int,
    ghostDeltaSeconds: Long,
    onCompleteTask: () -> Unit,
    onShareStatus: () -> Unit,
    modifier: Modifier = Modifier
) {
    val minutes = remainingSeconds / 60
    val seconds = remainingSeconds % 60
    val formattedTimer = String.format("%02d:%02d", minutes, seconds)
    val ghostDeltaText = if (ghostDeltaSeconds <= 0) "-${Math.abs(ghostDeltaSeconds)}s (AHEAD)" else "+${ghostDeltaSeconds}s (BEHIND)"

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SurfaceDark)
            .padding(20.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Instrument Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "FOCUS HUD // $challengeTitle".uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "TASK $activeTaskIndex/$totalTasks",
                style = MaterialTheme.typography.labelSmall,
                color = CyberCyan
            )
        }

        // Center Countdown & Ghost Pace
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "ACTIVE OBJECTIVE",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = activeTaskTitle,
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Timer Display
            Text(
                text = formattedTimer,
                style = MaterialTheme.typography.headlineMedium,
                color = CyberCyan,
                fontSize = 56.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Ghost Pace HUD Block
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SurfaceContainer)
                    .border(1.dp, CyberCyan.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "GHOST PACE",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = ghostDeltaText,
                            style = MaterialTheme.typography.labelSmall,
                            color = if (ghostDeltaSeconds <= 0) CyberCyan else MaterialTheme.colorScheme.error
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    GhostProgressBar(
                        progress = if (totalTasks > 0) activeTaskIndex.toFloat() / totalTasks else 0f,
                        height = 6
                    )
                }
            }
        }

        // Bottom Controls
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            HudButton(
                text = "Share Status",
                onClick = onShareStatus,
                isPrimary = false,
                modifier = Modifier.weight(1f)
            )
            HudButton(
                text = "Complete Task",
                onClick = onCompleteTask,
                isPrimary = true,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
