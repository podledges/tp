# Ayden van Etten - Project Portfolio Page

## Overview
MediStock is a desktop inventory management application for clinics and pharmacies. It is optimized for users who prefer working with a Command Line Interface (CLI), and supports tracking medicine items, batches, expiry dates, withdrawals, and command history.

I contributed heavily to the backend codebase structure, particularly within the Parser and Storage components. My main areas of responsibility included implementing the core `batch` feature, designing the persistent local storage architecture, refining UI printing methods, and establishing the initial project documentation and branding.
I contributed across implementation, testing, and documentation. My main areas were the `batch` command, the persistent storage system, and backend parsing improvements. I also led the v1.0 User Guide, authored Developer Guide sections for storage and batch features, and wrote targeted JUnit tests for both. On the project management side,
kept the group chat alive and the team aligned;pushing for our regular meeting. Also contributed heavily to the backend codebase structure, particularly within the Parser, Ui and oversaw Batch related features.

### Code Contributed
[RepoSense link](https://nus-cs2113-ay2526-s2.github.io/tp-dashboard/?search=podledges&sort=groupTitle&sortWithin=title&timeframe=commit&mergegroup=&groupSelect=groupByRepos&breakdown=true&checkedFileTypes=docs~functional-code~test-code~other&since=2026-02-20T00%3A00%3A00&filteredFileName=)

### Project Management
I took ownership of several foundational project management responsibilities from the start of the project:
* Spearheaded the v1.0 iteration of the User Guide, establishing the initial structure, formatting, and standardizing the documentation style for the team.
* Submitted required deliverables consistently in earlier weeks, helping the team stay on track with module deadlines.
* Regularly initiated and pushed for team meetings during our scheduled time slots, ensuring the team maintained consistent communication and momentum throughout the project.
* Designed the official MediStock logo, establishing a consistent visual identity for the application and team.
* Contributed to the creation and assignment of GitHub Issues throughout the project to keep work organised and distributed.

### Enhancements Implemented
I implemented or substantially contributed to the following areas:
* **Batch Management Feature:** Engineered the `batch` command end-to-end. This enabled the addition of stock with specific quantities and expiry dates to existing inventory items. It required implementing robust parsing logic, input validation, and ensuring the inventory correctly tracks and sorts batches by expiration.
* **Persistent Storage System:** Designed and implemented the local storage architecture to automatically save and load inventory data. 
* **Confirmation Dialog:** Implemented a general use message confirmation dialog that prompts the user for confirmation when adding an expired batch.
* **Backend Architecture:** Refined the `Parser` class to handle multi-argument user inputs and map them to their respective command objects, creating a helper function to do so.
* **UI Improvements:** Designed and Implemented essential printing methods within the `Ui` class to ensure clean and readable terminal outputs for inventory modifications, consistent with the UserGuide.

### Testing Contributions
* Wrote JUnit tests for BatchCommand, verifying both successful execution and appropriate exception-throwing behaviour for missing items.
* Wrote JUnit tests for Storage, covering round-trip save/load integrity, file corruption detection, and initializeInventory behaviour.
* Identified and designed regression tests for a non-obvious edge case in batch ID tracking: verifying that nextBatchNumber is correctly persisted and restored even after all batches for an item have been fully withdrawn, and that a cancelled batch confirmation does not incorrectly advance the ID counter.

### Documentation Contributions
* Spearheaded the v1.0 User Guide, authoring the initial structure, formatting conventions, and documentation style that the team built upon. This included documenting all v1.0 commands with their formats, parameters, usage examples, and expected outputs.
* Actively pushed teammates to review each other's UML diagrams, and drove collaboration to identify and eliminate redundant or duplicated diagram elements across the Developer Guide, improving overall consistency.
* Authored the Local Storage Feature sections in the User and Developer Guide, and made minor formatting improvments throughout both.

### Review and Integration Contributions
Beyond direct feature work, I helped maintain the integrity of the codebase:
- Reviewed work from teammates through pull requests and within more general and casual conversation.
- Attempted to find bugs and review all the UML diagrams in the developer guide. 
- Pushed for more consistent peer review of pull requests across the team. ***Succeeding*** with our peer review of each other's heavy PRs created for v1.0, v2.0 and v2.1.
- Triaged and closed a large volume of issues after the Practical Exam Dry Run (PE-D), systematically working through the issues tab to fix issues that did not apply to one specific feature implementation. 

## Contributions to the User Guide
My User Guide contributions focused on establishing the document's foundation and detailing my specific features:
- Led the first draft of the User Guide, documenting all v1.0 commands, their expected outputs, the initial product overview and command summary table..
- Added a Q&A regarding the purpose of Batch Number to the User Guide, providing an explanation on a key detail of our software
- Authored usage instructions, formatting rules, and expected failure cases for the batch command.
- Documented the automatic data saving mechanisms, file locations, and warnings regarding manual file editing.
- Brainstormed potential issues to include in the Known Issues section of the User Guide.

## Contributions to the Developer Guide
I focused my Developer Guide contributions on breaking down the core architecture and execution logic of my features to make them easily understandable for future developers:
- Designed a UML Class Diagram showing how the ```Storage``` class interacts with the ```Storable``` interface to persist ```InventoryItem``` and ```Batch``` objects without tight coupling, and also a sequence diagram for both the Saving and Initialization Sequences.
- **Batch Execution Sequence:** Mapped out the complete lifecycle of the `batch` command from user input to final storage, creating a UML Sequence Diagram tracing object creation, method calls, and interactions across the Parser, Inventory, UI, and Storage.
- Created a UML Sequence Diagram mapping the full lifecycle of the batch command from user input through Parser, Inventory, UI, and Storage.
- Drove the team to cross-review diagrams and eliminate redundancies for a more cohesive guide.
- Announced changes made that might affect the UML diagrams of others. 

## Contributions to team-based tasks (beyond individual responsibilities)
- **Peer support and mentorship:** Offered direct help to teammates privately when it felt right to do so, and had also very swiftly replied whenever a teammate had a question, keeping team progress unblocked outside  scheduled meeting hours.
- **Repository setup:** Created the team organization fork, configured the repository, and assigned all teammates as owners, ensuring the team could begin development without any onboarding friction.
- **Team cohesion:** Consistently initiated and enforced regularity of team meetings during our scheduled weekly sessions, and contributed ideas for features during discussions — including the suggestion to track rather than automatically remove expired medications, which shaped a core product decision.

