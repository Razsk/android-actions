package com.example.androidactions

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.androidactions.ui.focushud.FocusHudScreen
import com.example.androidactions.ui.main.MainScreen

@Composable
fun MainNavigation() {
  val backStack = rememberNavBackStack(Main)

  NavDisplay(
    backStack = backStack,
    onBack = { backStack.removeLastOrNull() },
    entryProvider =
      entryProvider {
        entry<Main> {
          MainScreen(
            onItemClick = { navKey -> backStack.add(navKey) },
            modifier = Modifier.safeDrawingPadding()
          )
        }
        entry<FocusHud> {
          FocusHudScreen(
            challengeTitle = "30-MIN APARTMENT RESET",
            activeTaskTitle = "Clear Kitchen Countertop",
            remainingSeconds = 1450L,
            activeTaskIndex = 1,
            totalTasks = 5,
            ghostDeltaSeconds = -12L,
            onCompleteTask = { backStack.removeLastOrNull() },
            onShareStatus = {},
            modifier = Modifier.safeDrawingPadding()
          )
        }
      },
  )
}
