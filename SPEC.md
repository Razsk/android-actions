
## Problem Statement

Users trying to maintain consistent personal routines and complete daily tasks face three primary points of friction:
1. **Routine Adherence Decay**: Static recurring reminders get skipped or postponed continuously without intelligent schedule adjustment, leading to notification fatigue.
2. **Accountability Friction**: Speedruns or timed task challenges require partners to install specialized apps or create accounts just to view progress.
3. **Distracted Execution**: Standard task managers present dense lists during active completion, causing context switching and lack of focus.

## Solution

A native, privacy-first Android application designed with the **Kinetic HUD** aesthetic featuring:
- **On-Device Gemma 2B AI Inference** (via MediaPipe Tasks) analyzing historical completion logs to propose non-intrusive 1-tap schedule adjustment cards.
- **Reusable Tasks & Routines**: Re-selectable tasks maintaining timestamped execution histories and flexible postponement periods.
- **Single-Day Challenge System with Focus HUD**: High-contrast, single-objective execution view with live countdowns and Ghost Pace split timing against personal bests.
- **Zero-Friction Accountability**: Instant status updates (Start, Finish, Timeout) sent via native Android sharing intents (SMS, WhatsApp, Signal) requiring zero partner setup.

## User Stories

1. As a user, I want to create reusable tasks so that I can quickly log or re-select them when planning my day.
2. As a user, I want to assign tags and lists to tasks so that I can organize objectives by context (Work, Health, Home).
3. As a user, I want to define recurring routines with default frequency periods so that due tasks auto-appear on my Mission Control view.
4. As a user, I want to postpone a due routine with a custom deferral period so that it reschedules with a reminder instead of being marked complete.
5. As a user, I want Gemma to analyze my routine postponement history so that I receive non-intrusive cards suggesting baseline schedule shifts.
6. As a user, I want to accept or dismiss Gemma's schedule shift suggestions with a single tap.
7. As a user, I want to launch single-day challenge modules (e.g. 30-Min Apartment Reset) so that I can complete grouped tasks within a time budget.
8. As a user, I want to see a uncluttered Focus HUD screen during an active challenge so that I only focus on the active task and remaining timer.
9. As a user, I want a Ghost Pace Bar during challenges so that I can compare my current split time against my personal best attempt.
10. As a user, I want to send challenge start and finish updates to a buddy via standard SMS or messaging apps without requiring them to install the app.
11. As a user, I want to view routine consistency heatmaps and hourly productivity charts so that I can visualize my completion performance.

## Implementation Decisions

### Android Clean MVVM Architecture & Stack
- **UI Layer**: Jetpack Compose + Material 3 implementing the Kinetic HUD dark design system (`#0b1326` base, `#b2c5ff` action blue, `#00daf3` ghost cyan, Hanken Grotesk & JetBrains Mono typography).
- **Data & Domain Layer**: Room Database for offline persistence (`TaskEntity`, `RoutineEntity`, `TaskExecutionLog`, `ChallengeEntity`, `SplitTimeEntity`), Kotlin Coroutines, and Flow.
- **AI Inference Engine**: `com.google.mediapipe:tasks-genai` executing quantized Gemma 2B on-device on CPU/GPU.
- **Notifications & Background Scheduler**: WorkManager for scheduled routine checks and AlarmManager for exact due-time alerts.

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
- **Single Core Testing Seam**: Primary domain business logic and state reducers (`TaskEngine`, `RoutineScheduler`, `GhostPaceCalculator`, `GemmaPredictor`) will be tested at the ViewModel/UseCase layer using standard JUnit 5 & Kotlinx Coroutines Test framework.
- **Data Layer Seam**: Room DAO migrations and queries verified using in-memory Room database tests.

## Out of Scope

- Remote cloud server sync or user account authentication (100% offline, privacy-first local storage).
- Automatic background SMS sending requiring Android `SEND_SMS` permission (all sharing uses native OS intents).

## Further Notes

- Design tokens, color palette, and HUD components strictly adhere to `DESIGN.md` and `CONTEXT.md`.
