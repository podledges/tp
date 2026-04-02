# Manu Dagur - Project Portfolio Page

## Overview
MediStock is a desktop inventory management application for clinics and pharmacies. It is optimized for users who prefer working with a Command Line Interface (CLI), and supports tracking medicine items, batches, expiry dates, withdrawals, and command history.

I contributed across implementation, testing, and documentation. My main areas were the `withdraw` feature, the expired medicine detection and removal system (`remove-expired` and automatic expiry detection), and the corresponding User Guide and Developer Guide sections.

## Summary of Contributions

### Code Contributed
[RepoSense link](https://nus-cs2113-ay2526-s2.github.io/tp-dashboard/?search=manudagur87&breakdown=true&sort=groupTitle%20dsc&sortWithin=title&since=2026-02-20T00%3A00%3A00&timeframe=commit&mergegroup=&groupSelect=groupByRepos&checkedFileTypes=docs~functional-code~test-code~other&filteredFileName=)

### Project Management
I contributed to project milestones by delivering feature work, tests, and documentation updates across `v1.0` and `v2.0`.

- Opened and merged pull requests for the withdraw feature (#29), withdraw tests (#36, #37), expired medicine functionality (#57, #58, #59), Developer Guide updates (#68), and User Guide updates (#70).

### Enhancements Implemented
I implemented or substantially contributed to the following areas:

- Implemented the `withdraw` feature end-to-end, including `WithdrawCommand`, parser support for validating `n/` and `q/` parameters, the withdrawal logic in `InventoryItem` that depletes from earliest-expiring batches first, and the corresponding UI output.
- Designed and implemented the expired medicine system, including:
  - Adding an `isExpired` flag to the `Batch` class and updating `InventoryItem` and `Parser` to support expiry tracking.
  - Implementing automatic expiry detection (`sortAndMarkExpiredBatches()`) that flags expired batches during routine operations such as `withdraw`, `batch`, `list`, and `remove-expired`, ensuring users are always warned about expired stock.
  - Implementing the `remove-expired` command end-to-end, including `RemoveExpiredCommand`, parser support, inventory-level and item-level removal logic, and UI output. The command supports both removing all expired batches across the inventory and targeting a specific item by name.
- Updated the `Ui` class to display active and expired inventory separately in the `list` output, and added UI methods for withdraw and remove-expired feedback.
- Improved batch handling by adding `Comparator`-based sorting by expiry date and validation for withdraw quantities in `Batch`.

### Testing Contributions
I added tests to cover the withdraw feature:

- Wrote JUnit tests for `InventoryItem` withdraw behavior, covering successful withdrawals, insufficient stock, and batch depletion order.
- Wrote JUnit tests for withdraw parser validation, covering missing parameters, invalid quantities, and correct format enforcement.

### Documentation Contributions

#### Contributions to the User Guide
- Updated the `list` command output example to reflect the new active/expired inventory display format.
- Added the `remove-expired` command section, documenting both the all-items and specific-item variants with format, examples, and expected output.
- Added the `remove-expired` entry to the command summary table.

#### Contributions to the Developer Guide
- Wrote the Developer Guide section for the `withdraw` feature, documenting the command format, step-by-step behaviour, failure cases and messages, and logging.
- Wrote the Developer Guide section for the `remove-expired` feature, documenting both the all-items and specific-item variants, behaviour, and failure cases.
- Wrote the Developer Guide section for automatic expiry detection, documenting the trigger points, internal behaviour, and design rationale.
- Created PlantUML source files and generated sequence diagrams for the `withdraw`, `remove-expired`, and automatic expiry detection features.
