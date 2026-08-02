package com.example.androidactions.ui.challengesummary

import android.content.Context
import android.content.Intent
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androidactions.domain.BuddyAccountabilityFormatter
import com.example.androidactions.theme.ActionBlue
import com.example.androidactions.theme.CyberCyan
import com.example.androidactions.theme.SurfaceDark
import com.example.androidactions.ui.hud.HudButton
import com.example.androidactions.ui.hud.HudCard

@Composable
fun ChallengeSummaryScreen(
    challengeTitle: String,
    totalTimeSeconds: Long,
    ghostDeltaSeconds: Long,
    onDone: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val formatter = BuddyAccountabilityFormatter()

    val formattedTotalTime = String.format(
        "%02d:%02d",
        totalTimeSeconds / 60,
        totalTimeSeconds % 60
    )

    val deltaText = if (ghostDeltaSeconds <= 0) {
        "-${Math.abs(ghostDeltaSeconds)}s (PERSONAL BEST)"
    } else {
        "+${ghostDeltaSeconds}s"
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SurfaceDark)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        // Header
        Column {
            Text(
                text = "PROTOCOL COMPLETED",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = challengeTitle.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = ActionBlue
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Total Elapsed Time & Ghost Delta Card
        HudCard(title = "CHALLENGE SUMMARY METRICS") {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = formattedTotalTime,
                            fontSize = 44.sp,
                            style = MaterialTheme.typography.displayLarge,
                            color = CyberCyan
                        )
                        Text(
                            text = "TOTAL ELAPSED TIME",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Box(
                        modifier = Modifier
                            .background(CyberCyan.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
                            .border(1.dp, CyberCyan, RoundedCornerShape(8.dp))
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        Text(
                            text = deltaText,
                            style = MaterialTheme.typography.labelSmall,
                            color = CyberCyan
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Split Time Breakdown Table
        HudCard(title = "SPLIT TIME PERFORMANCE") {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("1. Clear Kitchen Countertop", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface)
                    Text("04:12 (-12s)", style = MaterialTheme.typography.labelSmall, color = CyberCyan)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("2. Trash & Recycling Run", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface)
                    Text("03:45 (-06s)", style = MaterialTheme.typography.labelSmall, color = CyberCyan)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Action Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            HudButton(
                text = "Share to Buddy",
                onClick = {
                    val shareText = formatter.formatFinishMessage(
                        challengeTitle = challengeTitle,
                        totalTimeSeconds = totalTimeSeconds
                    )
                    val sendIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, shareText)
                        type = "text/plain"
                    }
                    val shareIntent = Intent.createChooser(sendIntent, "Share Challenge Status")
                    context.startActivity(shareIntent)
                },
                isPrimary = true,
                modifier = Modifier.weight(1f)
            )
            HudButton(
                text = "Done",
                onClick = onDone,
                isPrimary = false,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
