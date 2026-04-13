# Manu's Contributions — MediStock

## 1. Withdraw Feature (end-to-end)

### Files
- **WithdrawCommand.java** — Entire file. Executes withdraw, records history with actual withdrawn quantity.
- **InventoryItem.withdraw(int)** (lines 143–166) — FIFO depletion from earliest-expiring non-expired batches. Returns actual amount withdrawn (supports partial fulfillment).
- **Parser.prepareWithdraw()** (lines 320–350) — Validates `n/` and `q/` parameters, enforces correct order.
- **Ui.printWithdraw()** (lines 377–387) — Shows withdrawn amount; warns user if withdrawal was partial ("Please top up at least X more to fulfill request.").

### Tests
- **WithdrawParserTest.java** — 7 tests: valid input, missing tags, wrong order, bare command, non-numeric quantity, non-positive quantity.
- **InventoryItemTest.java** — 6 withdraw tests: single batch reduce, exact quantity depletion, insufficient stock (partial withdraw), multi-batch FIFO order, expired-batch skip, all-expired returns 0.

---

## 2. Expired Medicine System

### Files
- **Batch.java** — Added `isExpired` boolean flag (line 16), `isExpired()` getter (line 44), `markExpired()` setter (line 48).
- **InventoryItem.sortAndMarkExpiredBatches()** (lines 173–190) — Sorts batches by expiry date (earliest first), marks expired batches, prints warnings. This is the shared backbone called by `withdraw`, `list`, `batch`, and `remove-expired`.
- **InventoryItem.getExpiredBatches()** (lines 215–223) — Filters and returns expired batches.
- **InventoryItem.getActiveBatches()** (lines 225–233) — Filters and returns non-expired batches.
- **InventoryItem.getQuantity()** (lines 85–93) — Only sums non-expired batch quantities.
- **InventoryItem.getBatchQuantity()** (lines 63–71) — Only counts non-expired batches.

---

## 3. Remove-Expired Command (end-to-end)

### Files
- **RemoveExpiredCommand.java** — Entire file. Supports two variants:
  - `remove-expired` — removes all expired batches across entire inventory.
  - `remove-expired n/NAME` — removes expired batches for a specific item.
- **InventoryItem.removeExpiredBatches()** (lines 207–213) — Marks, collects, and removes expired batches. Returns count removed.
- **Inventory.removeAllExpiredBatches()** (lines 209–215) — Iterates all items and sums removed counts.
- **Inventory.getExpiredBatches()** (lines 217–225) — Returns items that have at least one expired batch.
- **Parser** (lines 63–66) — Dispatches `remove-expired` (no args) and `remove-expired n/NAME`.
- **Parser.prepareRemoveExpired()** (lines 413–427) — Parses the `n/NAME` variant.
- **Ui.printRemoveExpired(int)** (lines 254–263) — Feedback for all-items removal.
- **Ui.printRemoveExpired(String, int)** (lines 265–275) — Feedback for single-item removal.

---

## 4. List Command — Expired Display

### Files
- **Ui.showInventoryList()** (lines 130–164) — Splits inventory display into two sections: "Current Active Pharmaceutical Inventory" and "Current Expired Pharmaceutical Inventory".
- **Ui.printActiveItemDetails()** (lines 328–349) — Renders active batches with totals and stock status.
- **Ui.printExpiredItemDetails()** (lines 351–364) — Renders expired batches per item (no totals/status since they're expired).

---

## 5. Partial Withdraw Fix (PEDFixes branch)

**Commit:** `9f44496` — "Added partial withdraw"

### What changed
- **Before (master):** `withdraw()` threw `MediStockException` if requested quantity exceeded available stock (all-or-nothing).
- **After (PEDFixes):** `withdraw()` returns `int` — the actual amount withdrawn. If stock is insufficient, it withdraws everything available and reports the shortfall.

### Files modified
- **InventoryItem.withdraw()** — Removed exception, returns `quantity - remaining`.
- **WithdrawCommand.execute()** — Captures returned `withdrawnQuantity`, passes both requested and actual to UI.
- **Ui.printWithdraw()** — Updated signature; shows "Please top up at least X more" when partial.
- **InventoryItemTest.java** — Changed test expectations from `assertThrows` to verifying returned withdrawn quantity. Renamed test methods accordingly.

---

## Documentation Contributions

- **User Guide:** Added `remove-expired` command section and updated `list` output examples for active/expired display.
- **Developer Guide:** Wrote sections for `withdraw`, `remove-expired`, and automatic expiry detection. Created PlantUML sequence diagrams for all three.
- **PPP:** `docs/team/manudagur87.md`

## Related PRs
#29 (withdraw), #36 #37 (withdraw tests), #57 #58 #59 (expired medicine), #68 (DG), #70 (UG), #92 (PPP)
