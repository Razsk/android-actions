package com.example.androidactions.ui.stats

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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.androidactions.domain.ReliabilityStats
import com.example.androidactions.theme.ActionBlue
import com.example.androidactions.theme.CyberCyan
import com.example.androidactions.theme.SurfaceContainer
import com.example.androidactions.theme.SurfaceDark
import com.example.androidactions.ui.hud.HudCard

@Composable
fun StatsScreen(
    modifier: Modifier = Modifier,
    viewModel: StatsViewModel = StatsViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    when (state) {
        StatsUiState.Loading -> {}
        is StatsUiState.Success -> {
            val stats = (state as StatsUiState.Success).stats
            StatsDashboardContent(stats = stats, modifier = modifier)
        }
        is StatsUiState.Error -> {
            Text("Error loading stats: ${(state as StatsUiState.Error).throwable.message}")
        }
    }
}

@Composable
internal fun StatsDashboardContent(
    stats: ReliabilityStats,
    modifier: Modifier = Modifier
) {
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
                text = "STATS & RELIABILITY",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "PERFORMANCE ANALYTICS // KINETIC ENGINE",
                style = MaterialTheme.typography.labelSmall,
                color = ActionBlue
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Overall Reliability % Gauge Card
        HudCard(title = "OVERALL RELIABILITY SCORE") {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "${stats.overallReliabilityPercentage}%",
                        fontSize = 48.sp,
                        style = MaterialTheme.typography.displayLarge,
                        color = CyberCyan
                    )
                    Text(
                        text = "30-DAY ON-TIME COMPLETION RATE",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Box(
                    modifier = Modifier
                        .background(CyberCyan.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
                        .border(1.dp, CyberCyan, RoundedCornerShape(8.dp))
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = if (stats.overallReliabilityPercentage >= 80) "OPTIMAL" else "DRIFT",
                        style = MaterialTheme.typography.labelSmall,
                        color = CyberCyan
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 7-Day Consistency Heatmap Grid Card
        HudCard(title = "WEEKLY CONSISTENCY HEATMAP") {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                stats.weeklyConsistency.forEach { daily ->
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp)
                                .background(
                                    if (daily.intensityFraction > 0f) CyberCyan.copy(alpha = 0.2f + (daily.intensityFraction * 0.8f)) else SurfaceContainer,
                                    RoundedCornerShape(4.dp)
                                )
                                .border(
                                    1.dp,
                                    if (daily.intensityFraction > 0f) CyberCyan else Color(0x338D90A0),
                                    RoundedCornerShape(4.dp)
                                )
                        )
                        Text(
                            text = daily.dayLabel,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Hourly Peak Productivity Bar Chart
        HudCard(title = "HOURLY PEAK PRODUCTIVITY DISTRIBUTION") {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                stats.hourlyProductivity.forEach { hourly ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = hourly.hourLabel,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.fillMaxWidth(0.18f)
                        )
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(16.dp)
                                .background(SurfaceContainer, RoundedCornerShape(2.dp))
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth((hourly.count / 10.0f).coerceIn(0.1f, 1.0f))
                                    .fillMaxSize()
                                    .background(ActionBlue, RoundedCornerShape(2.dp))
                            )
                        }
                    }
                }
            }
        }
    }
}
