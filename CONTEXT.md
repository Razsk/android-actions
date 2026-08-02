# Task Actions & Challenge System

An intelligent, privacy-first native Android task management app featuring on-device Gemma AI predictions, reusable task completion logs, recurring routines, and zero-friction accountability challenges.

## Language

**Reusable Task**:
A task definition configured to be suggested or re-selected during creation, maintaining a log of when instances are created and completed.
_Avoid_: Task template, predefined task

**Routine**:
A recurring action assigned a default frequency period.
_Avoid_: Recurring task, habit

**Due Objectives Card**:
A prominent Mission Control HUD container displaying open due routines and active tasks with 1-tap complete and postpone actions.
_Avoid_: Task list, pending box

**Inline Creation**:
Adding new custom tags or lists directly within the Task Creation modal via inline chip text fields.
_Avoid_: Category manager, tag settings

**Postponement**:
The explicit deferral of a due Routine, which reschedules it after a custom period rather than the default recurrence period.
_Avoid_: Snooze, delay, skip

**Challenge**:
A time-bounded, grouped execution of multiple Tasks completed sequentially or within an overall time budget.
_Avoid_: Quest, streak, sprint

**Ghost Mode**:
A competitive view within a Challenge where the user runs against their own personal best completion time.
_Avoid_: Time trial, solo race

**Zero-Friction Accountability**:
Sending challenge status updates (Start, Finish, Timeout) via standard system SMS or deep links without requiring the recipient to install the app.
_Avoid_: Peer invite, social sharing

**Focus HUD Mode**:
An uncluttered, high-contrast user interface active during a Challenge showing only the active task, remaining time, and ghost pace.
_Avoid_: Zen mode, active view

**Suggestion Card**:
A non-intrusive UI card displaying Gemma-generated schedule or frequency adjustments with one-tap action buttons.
_Avoid_: AI alert, popup dialog

**Split Time**:
The elapsed duration recorded for an individual Task within a Challenge execution used to calculate Ghost pace.
_Avoid_: Task duration, lap time

**Ghost Pace**:
A live visual comparison showing time delta (+/- seconds) against the personal best split for the active task.
_Avoid_: Delta, time diff
