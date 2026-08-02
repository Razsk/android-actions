package com.example.androidactions.ui.taskcreate

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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import com.example.androidactions.theme.ActionBlue
import com.example.androidactions.theme.CyberCyan
import com.example.androidactions.theme.SurfaceContainer
import com.example.androidactions.theme.SurfaceDark
import com.example.androidactions.ui.hud.HudButton

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun TaskCreationBottomSheet(
    onDismiss: () -> Unit,
    onSaveTask: (title: String, selectedTags: List<String>, listName: String, frequencyDays: Int, isReusable: Boolean) -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState()
) {
    var title by remember { mutableStateOf("") }
    var tagsList by remember { mutableStateOf(listOf("Work", "Health", "Home", "Leisure")) }
    var selectedTags by remember { mutableStateOf(setOf("Work")) }
    var newTagInput by remember { mutableStateOf("") }
    var isAddingTag by remember { mutableStateOf(false) }

    var listsList by remember { mutableStateOf(listOf("Default", "Personal", "Fitness")) }
    var selectedList by remember { mutableStateOf("Default") }
    var newListInput by remember { mutableStateOf("") }
    var isAddingList by remember { mutableStateOf(false) }

    var frequencyText by remember { mutableStateOf("1") }
    var isReusable by remember { mutableStateOf(true) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = SurfaceDark
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Text(
                text = "CREATE TASK / ROUTINE",
                style = MaterialTheme.typography.labelSmall,
                color = ActionBlue
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Title Input
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Task Title", color = MaterialTheme.colorScheme.onSurfaceVariant) },
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

            // Tag Chips + Inline Tag Creation
            Text(
                text = "TAGS",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                tagsList.forEach { tag ->
                    val isSelected = selectedTags.contains(tag)
                    Box(
                        modifier = Modifier
                            .background(
                                if (isSelected) CyberCyan.copy(alpha = 0.2f) else SurfaceContainer,
                                RoundedCornerShape(4.dp)
                            )
                            .border(
                                1.dp,
                                if (isSelected) CyberCyan else Color(0x338D90A0),
                                RoundedCornerShape(4.dp)
                            )
                            .clickable {
                                selectedTags = if (isSelected) selectedTags - tag else selectedTags + tag
                            }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = tag.uppercase(),
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isSelected) CyberCyan else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .background(ActionBlue.copy(alpha = 0.15f), RoundedCornerShape(4.dp))
                        .border(1.dp, ActionBlue, RoundedCornerShape(4.dp))
                        .clickable { isAddingTag = !isAddingTag }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "+ NEW TAG",
                        style = MaterialTheme.typography.labelSmall,
                        color = ActionBlue
                    )
                }
            }

            if (isAddingTag) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = newTagInput,
                        onValueChange = { newTagInput = it },
                        label = { Text("New Tag Name") },
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ActionBlue,
                            unfocusedBorderColor = Color(0x338D90A0)
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    HudButton(
                        text = "Add",
                        onClick = {
                            if (newTagInput.isNotBlank()) {
                                val tag = newTagInput.trim()
                                if (!tagsList.contains(tag)) tagsList = tagsList + tag
                                selectedTags = selectedTags + tag
                                newTagInput = ""
                                isAddingTag = false
                            }
                        },
                        isPrimary = true
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // List Selector + Inline List Creation
            Text(
                text = "ASSIGN TO LIST",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listsList.forEach { list ->
                    val isSelected = selectedList == list
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
                            .clickable { selectedList = list }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = list.uppercase(),
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isSelected) ActionBlue else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .background(ActionBlue.copy(alpha = 0.15f), RoundedCornerShape(4.dp))
                        .border(1.dp, ActionBlue, RoundedCornerShape(4.dp))
                        .clickable { isAddingList = !isAddingList }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "+ NEW LIST",
                        style = MaterialTheme.typography.labelSmall,
                        color = ActionBlue
                    )
                }
            }

            if (isAddingList) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = newListInput,
                        onValueChange = { newListInput = it },
                        label = { Text("New List Name") },
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ActionBlue,
                            unfocusedBorderColor = Color(0x338D90A0)
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    HudButton(
                        text = "Add",
                        onClick = {
                            if (newListInput.isNotBlank()) {
                                val list = newListInput.trim()
                                if (!listsList.contains(list)) listsList = listsList + list
                                selectedList = list
                                newListInput = ""
                                isAddingList = false
                            }
                        },
                        isPrimary = true
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Clean Reusable Task Toggle Switch (No subtext explanation)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "REUSABLE TASK",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Switch(
                    checked = isReusable,
                    onCheckedChange = { isReusable = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = CyberCyan,
                        checkedTrackColor = CyberCyan.copy(alpha = 0.3f),
                        uncheckedThumbColor = Color(0xFF8D90A0),
                        uncheckedTrackColor = SurfaceContainer
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Frequency Input
            OutlinedTextField(
                value = frequencyText,
                onValueChange = { frequencyText = it },
                label = { Text("Recurrence Frequency (Days, 0 = Non-Recurring)", color = MaterialTheme.colorScheme.onSurfaceVariant) },
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

            // Action Buttons
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
                    text = "Save Task",
                    onClick = {
                        if (title.isNotBlank()) {
                            val days = frequencyText.toIntOrNull() ?: 0
                            onSaveTask(title.trim(), selectedTags.toList(), selectedList, days, isReusable)
                            onDismiss()
                        }
                    },
                    isPrimary = true,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
