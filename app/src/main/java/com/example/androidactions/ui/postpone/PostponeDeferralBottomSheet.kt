package com.example.androidactions.ui.postpone

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
import androidx.compose.foundation.shape.RoundedCornerShape
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
fun PostponeDeferralBottomSheet(
    taskTitle: String,
    onDismiss: () -> Unit,
    onConfirmDeferral: (deferDays: Int) -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState()
) {
    var selectedDays by remember { mutableStateOf(1) }
    var customDaysText by remember { mutableStateOf("") }
    var isCustom by remember { mutableStateOf(false) }

    val presetOptions = listOf(
        1 to "+1 DAY",
        3 to "+3 DAYS",
        7 to "+1 WEEK"
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = SurfaceDark
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Text(
                text = "POSTPONE ROUTINE DEFERRAL",
                style = MaterialTheme.typography.labelSmall,
                color = ActionBlue
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = taskTitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "SELECT DEFERRAL PERIOD",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                presetOptions.forEach { (days, label) ->
                    val isSelected = !isCustom && selectedDays == days
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
                            .clickable {
                                isCustom = false
                                selectedDays = days
                            }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isSelected) ActionBlue else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .background(
                            if (isCustom) CyberCyan.copy(alpha = 0.2f) else SurfaceContainer,
                            RoundedCornerShape(4.dp)
                        )
                        .border(
                            1.dp,
                            if (isCustom) CyberCyan else Color(0x338D90A0),
                            RoundedCornerShape(4.dp)
                        )
                        .clickable { isCustom = true }
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "CUSTOM",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isCustom) CyberCyan else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            if (isCustom) {
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = customDaysText,
                    onValueChange = { customDaysText = it },
                    label = { Text("Deferral Days", color = MaterialTheme.colorScheme.onSurfaceVariant) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CyberCyan,
                        unfocusedBorderColor = Color(0x338D90A0),
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                    )
                )
            }

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
                    text = "Confirm Postpone",
                    onClick = {
                        val finalDays = if (isCustom) customDaysText.toIntOrNull() ?: 1 else selectedDays
                        onConfirmDeferral(finalDays)
                        onDismiss()
                    },
                    isPrimary = true,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
