# 02 — Interactive Routine Postponement & Deferral Selector

**What to build:**
A 1-tap **Postpone** button on due routine cards on Mission Control. Defers routine by default pre-defined period immediately, or opens a custom deferral selector (`+1 Day`, `+3 Days`, `Custom`) when unconfigured. Updates `RoutineEntity` due timestamp and records a `TaskExecutionLog` with `ActionType.POSTPONED`.

**Blocked by:** 01 — Task Creation Bottom Sheet Modal & Persistence

**Status:** closed

- [x] 1-tap Postpone button on due routine cards.
- [x] Uses default deferral period or presents quick selector (`+1 Day`, `+3 Days`, `Custom`).
- [x] Updates `RoutineEntity.dueTimestamp` and sets `isPostponed = true`.
- [x] Inserts `TaskExecutionLog(actionType = ActionType.POSTPONED)` to feed Gemma AI optimization.
