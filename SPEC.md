
## Problem Statement

Users trying to maintain consistent personal routines and complete daily tasks face three primary points of friction:
1. **Routine Adherence Decay**: Static recurring reminders get skipped or postponed continuously without intelligent schedule adjustment, leading to notification fatigue.
2. **Accountability Friction**: Speedruns or timed task challenges require partners to install specialized apps or create accounts just to view progress.
3. **Distracted Execution**: Standard task managers present dense lists during active completion, causing context switching and lack of focus.

## Solution

A native, privacy-first Android application designed with the **Kinetic HUD** aesthetic featuring:
- **On-Device Gemma 2B AI Inference** (via MediaPipe Tasks) analyzing historical completion logs to propose non-intrusive 1-tap schedule adjustment cards.
- **Reusable Tasks & Routines**: Re-selectable tasks maintaining timestamped execution histories and flexible postponement periods.
- **Prominent Due Objectives Card**: Dedicated HUD container prominently displaying open due routines and active tasks directly below the Mission Control header.
- **Inviting Multi-Entry Task Creation**: Solid Action Blue header button and Floating Action Button (FAB) launching a Kinetic HUD dark bottom sheet modal.
- **Inline Custom Tag & List Creation**: Allows users to define new tags and lists on the fly directly inside the Task Creation modal via `+ NEW TAG` and `+ NEW LIST` chip text fields.
- **Reusable Task Switch**: Material 3 Switch toggle in the creation modal enabling users to specify whether a task should be saved to the reusable library for quick re-selection.
- **Dedicated Stats & Analytics Navigation Tab**: Dedicated bottom navigation tab switching to a full Stats screen with Overall Reliability % gauge, Weekly Consistency Heatmap, Hourly Peak Productivity distribution bar chart, and Routine Drift breakdowns.
- **Single-Day Challenge System with Focus HUD**: High-contrast, single-objective execution view with live countdowns, Ghost Pace split timing against personal bests, and a dedicated Challenge Summary completion screen.
- **Zero-Friction Accountability**: Instant status updates (Start, Finish, Timeout) sent via native Android sharing intents (SMS, WhatsApp, Signal) requiring zero partner setup.

## User Stories

1. As a user, I want to see open due routines and tasks in a prominent 'Due Objectives' HUD section on Mission Control so that I immediately know what needs action.
2. As a user, I want an inviting solid Action Blue button in the header and a Floating Action Button (FAB) at the bottom-right so that creating a task is always accessible.
3. As a user, I want to switch to a dedicated 'Stats & Analytics' bottom navigation tab so that I can view my completion performance metrics.
4. As a user, I want to view an Overall Reliability % gauge and a 7-day Cyber Cyan Consistency Heatmap grid so that I can visualize my weekly task completion volume.
5. As a user, I want to view an Hourly Peak Productivity distribution chart so that I know what time of day I complete tasks most efficiently.
6. As a user, I want to create new custom tags and lists inline within the Task Creation modal so that I don't have to exit my task creation flow.
7. As a user, I want a 'REUSABLE TASK' switch toggle in the Task Creation modal so that I can choose whether to keep a task definition in my library for future re-selection.
8. As a user, I want to assign tags and lists to tasks so that I can organize objectives by context (Work, Health, Home).
9. As a user, I want to define recurring routines with default frequency periods so that due tasks auto-appear on my Mission Control view.
10. As a user, I want to postpone a due routine with a single tap using a pre-defined deferral period so that it reschedules without extra prompts unless a custom period is required.
11. As a user, I want Gemma to analyze my routine postponement history so that I receive non-intrusive cards suggesting baseline schedule shifts.
12. As a user, I want to accept or dismiss Gemma's schedule shift suggestions with a single tap.
13. As a user, I want to launch single-day challenge modules (e.g. 30-Min Apartment Reset) so that I can complete grouped tasks within a time budget.
14. As a user, I want to see an uncluttered Focus HUD screen during an active challenge so that I only focus on the active task and remaining timer.
15. As a user, I want a Ghost Pace Bar during challenges so that I can compare my current split time against my personal best attempt.
16. As a user, I want to see a Challenge Summary screen upon completing all tasks so that I can view total time elapsed, personal best split deltas, and share progress.
17. As a user, I want to send challenge start, finish, and timeout updates to a buddy via standard SMS or messaging apps without requiring them to install the app.

## Implementation Decisions

### Android Clean MVVM Architecture & Stack
- **UI Layer**: Jetpack Compose + Material 3 implementing the Kinetic HUD dark design system (`#0b1326` base, `#b2c5ff` action blue, `#00daf3` ghost cyan, Hanken Grotesk & JetBrains Mono typography).
- **Navigation & Screen Architecture**: AndroidX Navigation3 with a bottom `NavigationBar` navigating between **Mission Control** (`Main`) and **Stats & Analytics** (`Stats`).
- **Data & Domain Layer**: Room Database for offline persistence (`TaskEntity`, `RoutineEntity`, `TaskExecutionLog`, `ChallengeEntity`, `SplitTimeEntity`), Kotlin Coroutines, and Flow.
- **AI Inference Engine**: `com.google.mediapipe:tasks-genai` executing quantized Gemma 2B on-device on CPU/GPU.
- **Notifications & Background Scheduler**: WorkManager for scheduled routine checks and AlarmManager for exact due-time alerts.

### User Interaction Contracts (From Interview Alignment)
- **Stats Entry Point**: Bottom `NavigationBar` tab item (`Stats & Analytics`).
- **Stats Dashboard Components**:
  - Overall Reliability % Circular Progress Gauge.
  - Weekly 7-day Cyber Cyan Consistency Heatmap grid (intensity 0%-100%).
  - Hourly Productivity Bar Chart (24-hour distribution of completed tasks).
  - Routine Postponement Drift breakdown list.
- **Due Objectives Card**: Prominently rendered below Mission Control header, featuring quick completion checkboxes and 1-tap postponement.
- **Inviting Task Creation Entry Points**: Header solid button (`+ CREATE TASK`) and Floating Action Button (FAB).
- **Inline Custom Tag/List Creation**: `+ NEW TAG` / `+ NEW LIST` chip buttons revealing inline text inputs within `TaskCreationBottomSheet`.
- **Reusable Task Switch**: Material 3 `Switch` toggle persisting `isReusable = true/false` on `TaskEntity`.
- **Postponement Deferral Logic**: 1-tap postponement using pre-defined deferral period; opens custom deferral selector (`+1 Day`, `+3 Days`, `Custom`) only when unconfigured.
- **Challenge Summary Transition**: Completing the final objective in Focus HUD navigates to a summary view displaying total elapsed time, Ghost Pace delta, and a prominent `Share to Buddy` action.

### State Machine Prototype Extract (Focus HUD & Ghost Pace)
```typescript
interface ChallengeRunState {
  challengeId: string;
  activeTaskIndex: number;
  remainingSeconds: number;
  currentSplitDelta: number; // ahead/behind vs PB split in seconds
  isGhostMode: boolean;
  status: 'IDLE' | 'ACTIVE' | 'PAUSED' | 'COMPLETED' | 'TIMEOUT';
}
```

## Testing Decisions

### Seams & Boundaries
- **Single Core Testing Seam**: Primary domain business logic and state reducers (`TaskEngine`, `RoutineScheduler`, `GhostPaceCalculator`, `OptimizationCardAnalyzer`, `ReliabilityCalculator`, `BuddyAccountabilityFormatter`) will be tested at the ViewModel/UseCase layer using standard JUnit 5 & Kotlinx Coroutines Test framework.
- **Data Layer Seam**: Room DAO migrations and queries verified using in-memory Room database tests (`AppDatabase`).

## Out of Scope

- Remote cloud server sync or user account authentication (100% offline, privacy-first local storage).
- Automatic background SMS sending requiring Android `SEND_SMS` permission (all sharing uses native OS intents).

## Further Notes

- Design tokens, color palette, and HUD components strictly adhere to `DESIGN.md` and `CONTEXT.md`.
