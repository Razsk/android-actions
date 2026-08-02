
## Problem Statement

Users trying to maintain consistent personal routines and complete daily tasks face three primary points of friction:
1. **Routine Adherence Decay**: Static recurring reminders get skipped or postponed continuously without intelligent schedule adjustment, leading to notification fatigue.
2. **Accountability Friction**: Speedruns or timed task challenges require partners to install specialized apps or create accounts just to view progress.
3. **Distracted Execution**: Standard task managers present dense lists during active completion, causing context switching and lack of focus.

## Solution

A native, privacy-first Android application designed with the **Kinetic HUD** aesthetic featuring:
- **On-Device Gemma 2B AI Inference** (via MediaPipe Tasks) analyzing historical completion logs to propose non-intrusive 1-tap schedule adjustment cards.
- **3-Destination Kinetic Bottom Navigation Bar**: Clean NavigationBar linking **Mission Control** (`Main`), **Challenges Library** (`Challenges`), and **Stats & Analytics** (`Stats`), which automatically hides during active Focus HUD execution.
- **Prominent Due Objectives Card**: Dedicated HUD container prominently displaying open due routines and active tasks directly below the Mission Control header.
- **Inviting Multi-Entry Task Creation**: Solid Action Blue header button and Floating Action Button (FAB) launching a Kinetic HUD dark bottom sheet modal.
- **Inline Custom Tag & List Creation**: Allows users to define new tags and lists on the fly directly inside the Task Creation modal via `+ NEW TAG` and `+ NEW LIST` chip text fields.
- **Reusable Task Switch**: Material 3 Switch toggle in the creation modal enabling users to specify whether a task should be saved to the reusable library for quick re-selection.
- **Local Buddy Directory & Zero-Friction Accountability**: Save buddy contact names and handles locally in Room (`BuddyEntity`) for 1-tap SMS sharing during challenge start/finish/timeout updates without requiring partner app installations.
- **Single-Day Challenge System with Focus HUD**: High-contrast, single-objective execution view with live countdowns, Ghost Pace split timing against personal bests, and a dedicated Challenge Summary completion screen.

## User Stories

1. As a user, I want a 3-item bottom navigation bar (`Mission Control ⚡`, `Challenges 🏆`, `Stats 📊`) so that I can easily navigate across core areas of the app.
2. As a user, I want the bottom navigation bar to automatically hide when I launch an active Focus HUD challenge so that I am not distracted during speedruns.
3. As a user, I want to view a dedicated 'Challenges Library' screen displaying preset challenge cards and a 'Create Custom Challenge' button.
4. As a user, I want to manage a local 'Buddy Directory' (Name + Phone Number) within creation modals so that I can easily select who receives my challenge updates.
5. As a user, I want to see open due routines and tasks in a prominent 'Due Objectives' HUD section on Mission Control so that I immediately know what needs action.
6. As a user, I want an inviting solid Action Blue button in the header and a Floating Action Button (FAB) at the bottom-right so that creating a task is always accessible.
7. As a user, I want to switch to a dedicated 'Stats & Analytics' bottom navigation tab so that I can view my completion performance metrics.
8. As a user, I want to view an Overall Reliability % gauge and a 7-day Cyber Cyan Consistency Heatmap grid so that I can visualize my weekly task completion volume.
9. As a user, I want to view an Hourly Peak Productivity distribution chart so that I know what time of day I complete tasks most efficiently.
10. As a user, I want to create new custom tags and lists inline within the Task Creation modal so that I don't have to exit my task creation flow.
11. As a user, I want a 'REUSABLE TASK' switch toggle in the Task Creation modal so that I can choose whether to keep a task definition in my library for future re-selection.
12. As a user, I want to assign tags and lists to tasks so that I can organize objectives by context (Work, Health, Home).
13. As a user, I want to define recurring routines with default frequency periods so that due tasks auto-appear on my Mission Control view.
14. As a user, I want to postpone a due routine with a single tap using a pre-defined deferral period so that it reschedules without extra prompts unless a custom period is required.
15. As a user, I want Gemma to analyze my routine postponement history so that I receive non-intrusive cards suggesting baseline schedule shifts.
16. As a user, I want to accept or dismiss Gemma's schedule shift suggestions with a single tap.
17. As a user, I want to launch single-day challenge modules (e.g. 30-Min Apartment Reset) so that I can complete grouped tasks within a time budget.
18. As a user, I want to see an uncluttered Focus HUD screen during an active challenge so that I only focus on the active task and remaining timer.
19. As a user, I want a Ghost Pace Bar during challenges so that I can compare my current split time against my personal best attempt.
20. As a user, I want to see a Challenge Summary screen upon completing all tasks so that I can view total time elapsed, personal best split deltas, and share progress.
21. As a user, I want to send challenge start, finish, and timeout updates to a buddy via standard SMS or messaging apps without requiring them to install the app.

## Implementation Decisions

### Android Clean MVVM Architecture & Stack
- **UI Layer**: Jetpack Compose + Material 3 implementing the Kinetic HUD dark design system (`#0b1326` base, `#b2c5ff` action blue, `#00daf3` ghost cyan, Hanken Grotesk & JetBrains Mono typography).
- **Navigation & Screen Architecture**: AndroidX Navigation3 with a 3-destination bottom `NavigationBar` linking **Mission Control** (`Main`), **Challenges Library** (`Challenges`), and **Stats & Analytics** (`Stats`), automatically hidden when rendering `FocusHudScreen`.
- **Data & Domain Layer**: Room Database for offline persistence (`TaskEntity`, `RoutineEntity`, `TaskExecutionLog`, `ChallengeEntity`, `SplitTimeEntity`, `BuddyEntity`), Kotlin Coroutines, and Flow.
- **AI Inference Engine**: `com.google.mediapipe:tasks-genai` executing quantized Gemma 2B on-device on CPU/GPU.
- **Notifications & Background Scheduler**: WorkManager for scheduled routine checks and AlarmManager for exact due-time alerts.

### User Interaction Contracts (From Interview Alignment)
- **Local Buddy Directory**:
  - Saved as `BuddyEntity(id, name, phoneNumber)` in Room.
  - Interactive `+ ADD BUDDY` inline field inside task and challenge creation bottom sheet modals.
  - Selected buddy details pre-populate the native SMS intent string generated by `BuddyAccountabilityFormatter`.
- **Primary Navigation Structure**: 3-destination Bottom Navigation Bar:
  1. `Mission Control ⚡`: Daily Overview, Due Objectives, Gemma Optimization Cards.
  2. `Challenges 🏆`: Challenge Library Grid & Custom Challenge Builder.
  3. `Stats & Reliability 📊`: Reliability Score Gauge, 7-day Consistency Heatmap, Hourly Productivity Bar Chart.
- **Focus HUD Seclusion**: NavigationBar automatically hides when entering `FocusHudScreen`.
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
