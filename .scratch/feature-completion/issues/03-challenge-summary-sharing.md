# 03 — Challenge Summary & Accountability Sharing Flow

**What to build:**
A dedicated Challenge Summary screen presented when the final task in Focus HUD is completed. Displays total elapsed time, Ghost Pace delta (`-18s (PERSONAL BEST)`), and split breakdown. Features a prominent `Share to Buddy` button triggering native Android sharing intents with formatted accountability updates (`BuddyAccountabilityFormatter`).

**Blocked by:** 01 — Task Creation Bottom Sheet Modal & Persistence

**Status:** closed

- [x] Focus HUD automatically transitions to Challenge Summary upon completing final task.
- [x] Displays total duration, Ghost Pace delta vs PB split, and split breakdown.
- [x] `Share to Buddy` button launches native OS share intent with `formatFinishMessage`.
- [x] Inserts `SplitTimeEntity` into Room DB if a new personal best is achieved.
