package com.example.androidactions

import android.content.Intent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.androidactions.domain.BuddyAccountabilityFormatter
import com.example.androidactions.theme.ActionBlue
import com.example.androidactions.theme.CyberCyan
import com.example.androidactions.theme.SurfaceContainer
import com.example.androidactions.theme.SurfaceDark
import com.example.androidactions.ui.challenges.ChallengesScreen
import com.example.androidactions.ui.challengesummary.ChallengeSummaryScreen
import com.example.androidactions.ui.focushud.FocusHudScreen
import com.example.androidactions.ui.main.MainScreen
import com.example.androidactions.ui.search.SearchScreen
import com.example.androidactions.ui.stats.StatsScreen

@Composable
fun MainNavigation() {
  val backStack = rememberNavBackStack(Main)
  val currentKey = backStack.lastOrNull() ?: Main
  val isBottomBarVisible = currentKey is Main || currentKey is Search || currentKey is Challenges || currentKey is Stats
  val context = LocalContext.current
  val formatter = BuddyAccountabilityFormatter()

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    containerColor = SurfaceDark,
    bottomBar = {
      if (isBottomBarVisible) {
        NavigationBar(
          containerColor = SurfaceDark,
          contentColor = MaterialTheme.colorScheme.onSurface
        ) {
          NavigationBarItem(
            selected = currentKey is Main,
            onClick = {
              if (currentKey !is Main) {
                backStack.removeLastOrNull()
                if (backStack.isEmpty()) backStack.add(Main)
              }
            },
            label = { Text("Mission Control", style = MaterialTheme.typography.labelSmall) },
            icon = { Text("⚡", color = if (currentKey is Main) CyberCyan else ActionBlue) },
            colors = NavigationBarItemDefaults.colors(
              selectedIconColor = CyberCyan,
              selectedTextColor = CyberCyan,
              indicatorColor = SurfaceContainer,
              unselectedIconColor = ActionBlue,
              unselectedTextColor = ActionBlue
            )
          )
          NavigationBarItem(
            selected = currentKey is Search,
            onClick = {
              if (currentKey !is Search) {
                backStack.add(Search)
              }
            },
            label = { Text("Search", style = MaterialTheme.typography.labelSmall) },
            icon = { Text("🔍", color = if (currentKey is Search) CyberCyan else ActionBlue) },
            colors = NavigationBarItemDefaults.colors(
              selectedIconColor = CyberCyan,
              selectedTextColor = CyberCyan,
              indicatorColor = SurfaceContainer,
              unselectedIconColor = ActionBlue,
              unselectedTextColor = ActionBlue
            )
          )
          NavigationBarItem(
            selected = currentKey is Challenges,
            onClick = {
              if (currentKey !is Challenges) {
                backStack.add(Challenges)
              }
            },
            label = { Text("Challenges", style = MaterialTheme.typography.labelSmall) },
            icon = { Text("🏆", color = if (currentKey is Challenges) CyberCyan else ActionBlue) },
            colors = NavigationBarItemDefaults.colors(
              selectedIconColor = CyberCyan,
              selectedTextColor = CyberCyan,
              indicatorColor = SurfaceContainer,
              unselectedIconColor = ActionBlue,
              unselectedTextColor = ActionBlue
            )
          )
          NavigationBarItem(
            selected = currentKey is Stats,
            onClick = {
              if (currentKey !is Stats) {
                backStack.add(Stats)
              }
            },
            label = { Text("Stats & Reliability", style = MaterialTheme.typography.labelSmall) },
            icon = { Text("📊", color = if (currentKey is Stats) CyberCyan else ActionBlue) },
            colors = NavigationBarItemDefaults.colors(
              selectedIconColor = CyberCyan,
              selectedTextColor = CyberCyan,
              indicatorColor = SurfaceContainer,
              unselectedIconColor = ActionBlue,
              unselectedTextColor = ActionBlue
            )
          )
        }
      }
    }
  ) { innerPadding ->
    NavDisplay(
      backStack = backStack,
      onBack = { backStack.removeLastOrNull() },
      modifier = Modifier.padding(innerPadding),
      entryProvider =
        entryProvider {
          entry<Main> {
            MainScreen(
              onItemClick = { navKey -> backStack.add(navKey) },
              modifier = Modifier.safeDrawingPadding()
            )
          }
          entry<Search> {
            SearchScreen(
              modifier = Modifier.safeDrawingPadding()
            )
          }
          entry<Challenges> {
            ChallengesScreen(
              onLaunchChallenge = { challengeId -> backStack.add(FocusHud(challengeId)) },
              modifier = Modifier.safeDrawingPadding()
            )
          }
          entry<Stats> {
            StatsScreen(
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
              onCompleteTask = {
                backStack.removeLastOrNull()
                backStack.add(
                  ChallengeSummary(
                    challengeTitle = "30-MIN APARTMENT RESET",
                    totalTimeSeconds = 1450L,
                    ghostDeltaSeconds = -18L
                  )
                )
              },
              onShareStatus = {
                val text = formatter.formatStartMessage("30-MIN APARTMENT RESET", "Clear Kitchen Countertop")
                val sendIntent = Intent().apply {
                  action = Intent.ACTION_SEND
                  putExtra(Intent.EXTRA_TEXT, text)
                  type = "text/plain"
                }
                context.startActivity(Intent.createChooser(sendIntent, "Share Status"))
              },
              modifier = Modifier.safeDrawingPadding()
            )
          }
          entry<ChallengeSummary> { key ->
            ChallengeSummaryScreen(
              challengeTitle = key.challengeTitle,
              totalTimeSeconds = key.totalTimeSeconds,
              ghostDeltaSeconds = key.ghostDeltaSeconds,
              onDone = {
                backStack.removeLastOrNull()
                if (backStack.isEmpty()) backStack.add(Main)
              },
              modifier = Modifier.safeDrawingPadding()
            )
          }
        },
    )
  }
}
