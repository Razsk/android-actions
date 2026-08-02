# 01 — Task Creation Bottom Sheet Modal & Persistence

**What to build:**
A Kinetic HUD dark bottom sheet modal launched from a top-bar `+` header action on Mission Control. Allows users to enter a title, choose tag chips (`Work`, `Health`, `Home`), assign a list, and specify recurrence frequency (days). Saving persists a `TaskEntity` to Room Database and automatically creates a corresponding `RoutineEntity` if frequency > 0.

**Blocked by:** None — can start immediately.

**Status:** closed

- [x] Top-bar `+` icon on Mission Control opens Task Creation bottom sheet modal.
- [x] Form includes title, tag chips (`Work`, `Health`, `Home`), list dropdown, and frequency period.
- [x] Saving writes `TaskEntity` to Room DB and creates `RoutineEntity` if frequency > 0.
- [x] ViewModel state flow reactively updates Mission Control upon saving.
