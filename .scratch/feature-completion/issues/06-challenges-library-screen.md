# 06 — Challenges Library & Custom Challenge Builder Screen

**What to build:**
A dedicated **Challenges Library** screen accessible via the 3-destination bottom `NavigationBar`. Presents a grid of challenge speedrun cards (e.g. *30-Min Apartment Reset*, *Morning Speedrun*) with task counts and time budgets, a `+ Create Custom Challenge` builder modal, and auto-hiding `NavigationBar` when entering Focus HUD mode.

**Blocked by:** 01 — Task Creation Bottom Sheet Modal & Persistence

**Status:** closed

- [x] Add `Challenges` tab to 3-item bottom `NavigationBar` (`Mission Control ⚡`, `Challenges 🏆`, `Stats 📊`).
- [x] Build `ChallengesScreen` grid displaying preset challenge cards and task budgets.
- [x] Implement `+ Create Custom Challenge` modal allowing users to group tasks into a challenge.
- [x] Ensure `NavigationBar` automatically hides during `FocusHudScreen` active execution.
