package com.example.androidactions.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.androidactions.data.TaskEntity
import com.example.androidactions.theme.ActionBlue
import com.example.androidactions.theme.CyberCyan
import com.example.androidactions.theme.SurfaceContainer
import com.example.androidactions.theme.SurfaceDark
import com.example.androidactions.ui.hud.HudCard

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SearchScreen(
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("ALL") }

    val sampleRoutines = remember {
        listOf(
            TaskEntity(id = 1L, title = "Descaling Coffee Machine", isReusable = true, defaultPeriodDays = 90, listName = "Home", tagsCsv = "Maintenance,Home"),
            TaskEntity(id = 2L, title = "Clear Kitchen Countertop", isReusable = true, defaultPeriodDays = 1, listName = "Home", tagsCsv = "Home"),
            TaskEntity(id = 3L, title = "Trash & Recycling Run", isReusable = true, defaultPeriodDays = 3, listName = "Home", tagsCsv = "Home"),
            TaskEntity(id = 4L, title = "Deep Work Shift", isReusable = true, defaultPeriodDays = 7, listName = "Work", tagsCsv = "Work")
        )
    }

    val filteredRoutines = sampleRoutines.filter { routine ->
        val matchesQuery = routine.title.contains(searchQuery, ignoreCase = true) ||
                routine.tagsCsv.contains(searchQuery, ignoreCase = true) ||
                routine.listName.contains(searchQuery, ignoreCase = true)

        val matchesFilter = when (selectedFilter) {
            "BACKGROUND (3M+)" -> routine.defaultPeriodDays >= 30
            "WORK" -> routine.listName.equals("Work", ignoreCase = true)
            "HOME" -> routine.listName.equals("Home", ignoreCase = true)
            else -> true
        }

        matchesQuery && matchesFilter
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
                text = "SEARCH & DIRECTORY",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "BROWSE ALL ROUTINES & BACKGROUND OBJECTIVES",
                style = MaterialTheme.typography.labelSmall,
                color = ActionBlue
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Search Text Input
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Search by title, tag, or list...", color = MaterialTheme.colorScheme.onSurfaceVariant) },
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

        // Filter Chips
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("ALL", "BACKGROUND (3M+)", "HOME", "WORK").forEach { filter ->
                val isSelected = selectedFilter == filter
                Box(
                    modifier = Modifier
                        .background(
                            if (isSelected) ActionBlue.copy(alpha = 0.2f) else SurfaceContainer,
                            RoundedCornerShape(4.dp)
                        )
                        .border(
                            1.dp,
                            if (isSelected) ActionBlue else Color(0x338D90A0),
                            RoundedCornerShape(4.dp)
                        )
                        .clickable { selectedFilter = filter }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = filter,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isSelected) ActionBlue else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Routines List
        Text(
            text = "MATCHING ROUTINES (${filteredRoutines.size})",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(12.dp))

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            filteredRoutines.forEach { routine ->
                HudCard(title = routine.title.uppercase()) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "EVERY ${routine.defaultPeriodDays} DAYS",
                                style = MaterialTheme.typography.labelSmall,
                                color = CyberCyan
                            )
                            Text(
                                text = "LIST: ${routine.listName.uppercase()}",
                                style = MaterialTheme.typography.labelSmall,
                                color = ActionBlue
                            )
                        }
                    }
                }
            }
        }
    }
}
