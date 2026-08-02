# 02 — Interactive Routine Postponement & Deferral Selector

**What to build:**
A 1-tap **Postpone** button on due routine cards on Mission Control. Defers routine by default pre-defined period immediately, or opens a custom deferral selector (`+1 Day`, `+3 Days`, `Custom`) when unconfigured. Updates `RoutineEntity` due timestamp and records a `TaskExecutionLog` with `ActionType.POSTPONED`.

**Blocked by:** 01 — Task Creation Bottom Sheet Modal & Persistence

**Status:** ready-for-agent

- [ ] 1-tap Postpone button on due routine cards.
- [ ] Uses default deferral period or presents quick selector (`+1 Day`, `+3 Days`, `Custom`).
- [ ] Updates `RoutineEntity.dueTimestamp` and sets `isPostponed = true`.
- [ ] Inserts `TaskExecutionLog(actionType = ActionType.POSTPONED)` to feed Gemma AI optimization.
