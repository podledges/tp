# Developer Guide

## Table of Contents
- [Acknowledgements](#acknowledgements)
- [Design](#design)
    - [Architecture](#architecture)
    - [UI Component](#ui-component)
    - [Parser Component](#parser-component)
    - [Command Component](#command-component)
    - [Inventory Component](#inventory-component)
    - [Storage Component](#storage-component)
- [Implementation](#implementation)
    - [Feature: Create Item](#feature-create-item)
    - [Feature: Add Batch](#feature-add-batch)
    - [Feature: Withdraw Stock](#feature-withdraw-stock)
    - [Feature: Delete Item by Name](#feature-delete-item-by-name)
    - [Feature: Delete Item by Index](#feature-delete-item-by-index)
    - [Feature: List Inventory](#feature-list-inventory)
    - [Feature: Find Item](#feature-find-item)
    - [Feature: Remove Expired Batches](#feature-remove-expired-batches)
    - [Feature: Automatic Expiry Detection](#feature-automatic-expiry-detection)
    - [Feature: Low Stock Warning](#feature-low-stock-warning)
    - [Feature: Help Command](#feature-help-command)
    - [Feature: Exit Command](#feature-exit-command)
    - [Feature: Data Storage](#feature-data-storage)
- [Product scope](#product-scope)
    - [Target user profile](#target-user-profile)
    - [Value proposition](#value-proposition)
- [User Stories](#user-stories)

***
## Acknowledgements

This developer guide was inspired by 
(https://se-education.org/addressbook-level4/DeveloperGuide.html#design)

## Design

{Describe the design and implementation of the product. Use UML diagrams and short code snippets where applicable.}

## Implementation

### Feature: Create Item

### Feature: Add Batch

### Feature: Withdraw Stock

### Feature: Delete Item by Name

### Feature: Delete Item by Index

### Feature: List Inventory

![ListCommand_ClassDiagram](diagrams/ListCommand_ClassDiagram.png)
![ListCommand_SequenceDiagram](diagrams/ListCommand_SequenceDiagram.png)

**Purpose:** Display all inventory items maintained in memory, separated into active and expired batches.

**Command word:** `list`

**Format:**
```
list
```

Prints a comprehensive view of the entire pharmaceutical inventory, divided into two sections:
1. Active batches (non-expired items)
2. Expired batches (items past expiry date)

Each item displays:
- Index number (1-based enumeration)
- Item name and minimum threshold
- All batches with batch number, quantity, unit, and expiry date
- Total active quantity
- Stock status (Critical/Healthy)

If the inventory is empty, prints "Your inventory is empty."

**Behaviour:**
1. Checks if inventory size is 0; if so, displays empty message
2. Iterates through `inventory.getActiveBatches()` and prints each item via `printActiveItemDetails()`
3. Iterates through `inventory.getExpiredBatches()` and prints each item via `printExpiredItemDetails()`
4. For each item, calls `item.sortAndMarkExpiredBatches()` to ensure batches are properly categorized
5. Displays batch-level details including batch number, quantity, unit, and expiry date

**Failure cases & messages:**
- None (arguments are ignored)
- If inventory is empty: "Your inventory is empty."
- If no active batches: "No active batches found."
- If no expired batches: "No expired batches found."

**Logging:**
- INFO on command entry/exit
- FINE for batch iteration

### Feature: Find Item

**Purpose:** Search for inventory items by keyword and display matching results.

**Command word:** `find`

**Format:**
```
find <keyword>
```

![FindCommand_SequenceDiagram](diagrams/FindCommand_SequenceDiagram.png)

Searches all inventory items for names containing the specified keyword (case-insensitive) and displays matching items with full details including active and expired batches.

**Behaviour:**
1. Parser validates that keyword is not empty; throws `MediStockException` if missing
2. Calls `inventory.findItem(keyword)` which:
   - Normalizes keyword to lowercase
   - Filters items using `item.getName().toLowerCase().contains(keyword)`
   - Returns list of matching `InventoryItem` objects
3. Displays results via `ui.showFindList()`:
   - If no matches: prints "No matches found!"
   - If matches found: prints "Here are the matching items in your inventory:" followed by detailed item information
4. For each matched item, displays:
   - Index number, name, and minimum threshold
   - Active batches (batch number, quantity, unit, expiry date)
   - Expired batches (batch number, quantity, unit, expiry date)
   - Total active quantity
   - Stock status (Critical/Healthy)

**Failure cases & messages:**
- Missing keyword: "Missing name of the item you want to find."
- No matches found: "No matches found!"

**Logging:**
- INFO on command entry/exit with keyword
- FINE for search result count

### Feature: Remove Expired Batches

### Feature: Automatic Expiry Detection

### Feature: Low Stock Warning

### Feature: Help Command

![HepCommand_SequenceDiagram](diagrams/HelpCommand_SequenceDiagram.png)

**Purpose:** Display a list of all available commands with their formats to assist users.

**Command word:** `help`

**Format:**
```
help
```

Prints an enumerated list of all available commands with their syntax formats.

**Behaviour:**
1. Calls `ui.printCommandList()` which displays:
   - "Available commands:" header
   - Numbered list of command names
   - Format strings for commands that require parameters

**Failure cases & messages:**
- None (arguments are ignored)

**Logging:**
- INFO on command entry/exit (to be implemented)

### Feature: Exit Command

![ExitCommand_SequenceDiagram](diagrams/HelpCommand_SequenceDiagram.png)

**Purpose:** Gracefully terminate the MediStock application.

**Command word:** `exit` or `quit`

**Format:**
```
exit
```
or
```
quit
```

Displays a farewell message and terminates the application.

**Behaviour:**
1. Calls `ui.printExitMessage()` which displays:
   - "Inventory saved"
   - "Thank you for using MediStock, have a nice day!"
2. The main loop sets `isRunning = false` and calls `exit()` method
3. `System.exit(0)` terminates the JVM


### Feature: Data Storage



## Product scope
### Target user profile

- Pharmacists and pharmacy technicians managing medicine inventory in small to mid-sized clinics or pharmacies.
- Users who prefer a CLI over a GUI for faster data entry during busy periods.
- Users who need to track batch-level details such as expiry dates and quantities.
- Users comfortable with typing commands and who value speed over visual interfaces.
- Users who need to ensure compliance with expiry regulations and avoid dispensing expired medicine.

### Value proposition

MediStock helps pharmacy staff manage pharmaceutical inventory faster than a typical GUI application.
It tracks medicines at the batch level, automatically flags and warns about expired stock during routine operations,
and prevents expired batches from being withdrawn. This reduces the risk of dispensing expired medicine,
simplifies stock-taking, and helps maintain minimum stock thresholds so that critical items are always available.

## User Stories

|Version| As a ... | I want to ... | So that I can ...|
|--------|----------|---------------|------------------|
|v1.0|new user|see usage instructions|refer to them when I forget how to use the application|
|v2.0|user|find a to-do item by name|locate a to-do without having to go through the entire list|

## Non-Functional Requirements

{Give non-functional requirements}

## Glossary

* *glossary item* - Definition

## Instructions for manual testing

{Give instructions on how to do a manual product testing e.g., how to load sample data to be used for testing}
