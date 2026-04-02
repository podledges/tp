# Tan-Pang - Project Portfolio Page

## Project: MediStock

MediStock is a CLI-based inventory management application for pharmacists and clinical staff to track medicines, batches, expiry dates, and stock levels. It is designed for users who prefer fast keyboard-driven workflows over GUI-based inventory systems.

My main contributions to MediStock were the `delete` and `history` commands. Where `delete` removes an entire class of 
medication and `history` displays the past changes of the MediStock inventory to the user.
### Summary of Contributions

- **Code contributed:** https://nus-cs2113-ay2526-s2.github.io/tp-dashboard/?search=tan-pang&breakdown=true&sort=groupTitle%20dsc&sortWithin=title&since=2026-02-20T00%3A00%3A00&timeframe=commit&mergegroup=&groupSelect=groupByRepos&checkedFileTypes=docs~functional-code~test-code~other&filteredFileName=

- **Enhancements implemented:**
    - Implemented the `delete` command from parser to execution, namely delete by name feature `delete n/` as well as delete by index feature `delete i/`. This includes command syntax parsing, handling of non-existing names and out-of-bounds indices.
    - Implemented the `history` command end-to-end. This includes command routing, `HistoryCommand` execution, obtaining history list and routing to UI output.
    - Implemented storage of history in `data/History.txt` text file when user exits MediStock and loading the history into a list when user starts up MediStock.
    - Added parser and execution tests for both `delete` and `history`, including invalid format cases, name not found
    and out-of-bounds index cases.

- **Contributions to the UG:**
    - Updated the `delete` command section so its wording and example output match the current interface. This includes delete by name and delete by index.
    - Added the `history` command section to document its syntax, purpose, and example output.

- **Contributions to the DG:**
    - Added the `Feature: Delete Item by Name` section to showcase the purpose, format, behaviour, failure cases and messages of delete by name feature.
    - Created the `DeleteCommandName` sequence diagram to explain the main success flow from input parsing to deleting the item.
    - Added the `Feature: Delete Item by Index` section to showcase the purpose, format, behaviour, failure cases and messages of delete by index feature.
    - Created the `DeleteCommandIndex` sequence diagram to explain the main success flow from input parsing to deleting the item.

- **Contributions to team-based tasks:**
    - Merged and updated multiple feature/documentation branches for `delete` and `history`, UG updates, and DG updates.
    - See [Implement feature/history] (https://github.com/AY2526S2-CS2113-W10-2/tp/pull/63)
    - See [Implemented delete command] (https://github.com/AY2526S2-CS2113-W10-2/tp/pull/33)
    - Ensure that project documentation for delete by name, delete by index and history features are consistent. This is achieved by updating their write-up format and diagram style to be aligned with the rest of the guide.
    - See [Developer Guide updates] (https://github.com/AY2526S2-CS2113-W10-2/tp/issues/60)

- **Review/mentoring contributions:**
    - To be filled with actual PR review links. 
