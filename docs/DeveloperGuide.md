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

Tools that helped with the creation of the MediStocks logo: <br>
(https://www.asciiart.eu/text-to-ascii-art) <br>
(https://www.asciiart.eu/image-to-ascii)

## Design

{Describe the design and implementation of the product. Use UML diagrams and short code snippets where applicable.}

## Implementation

### Feature: Create Item

### Feature: Add Batch
#<SEQUENCE DIAGRAM> 
**Purpose:** Add a new batch of stock to an existing medication or inventory item,
tracking its specific quantity and expiry date.

**Command Word:** `batch`
**Format:**
```
batch n/<name> q/<quantity> d/<expiryDate>
```
Finds the item by name in the inventory, organizes existing batches to flag expired ones, 
generates a new batch with the specified quantity and expiry date, and appends it to the item's record.
Finally, it updates the storage file and command history.

**Behaviour:**
1. Parses the user input using prepareBatch to extract the item name, quantity, and expiry date.
2. Validates that all prefixes `(n/, q/, d/)` are present and in the correct sequential order.
3. Ensures the quantity is a positive integer. and expiry date matches the `YYYY-MM-DD` format
4. Instantiates a new `BatchCommand` with the extracted parameters
5. Calls `BatchCommand.executre()`, which also checks if the item exists in the inventory.
6. Calls `item.sortAndMarkExpiredBatches()` to organize the item's current stock
7. Calculates the new batch number and instanties the `Batch` object 
8. Calls `item.addBatch(newBatch)` to attach it to the inventory item
9. Calls `ui.printBatch()` to display the success mesage and updated stock details
10. Records the addition in the command history and saves the new batch to storage via `storage.savetoFile()`.

**Failure cases & messages:**
- If any prefix is missing: "Invalid batch format. Format: batch n/NAME q/QUANTITY d/EXPIRY_DATE(YYYY-MM-DD)"
- If prefixes are out of order: "Ensure the arguments are in the correct order:"
- If quantity is not a number or is empty: "Invalid Quantity. Please enter a positive whole number for the quantity"
- If quantity is not a number or is empty: "Invalid quantity. Please enter a positive whole number for the quantity"
- If expiry data format is incorrect: "Invalid expiry date. Please use a valid format (e.g., YYYY-MM-DD)."
### Feature: Withdraw Stock

![WithdrawCommand_SequenceDiagram](diagrams/WithdrawCommandSequenceDiagram.png)

**Purpose:** Withdraw a specified quantity of an item from the inventory, depleting from the earliest-expiring batches first.

**Command word:** `withdraw`

**Format:**
```
withdraw n/<name> q/<quantity>
```

Finds the item by name, sorts its batches by expiry date (earliest first), marks any expired batches, then deducts the requested quantity from the remaining non-expired batches. Batches that reach zero quantity are removed. If the withdrawal spans multiple batches, earlier batches are fully depleted before moving to the next.

**Behaviour:**
1. Parses the user input to extract the item name and quantity.
2. Validates that the name and quantity are not empty, that quantity is a valid positive integer, and that `n/` appears before `q/`.
3. Calls `inventory.getItem(name)` to retrieve the corresponding `InventoryItem`.
4. Calls `item.withdraw(quantity)` which:
   - Sorts batches by expiry date (earliest first) and marks expired batches.
   - Checks that total available (non-expired) quantity is sufficient.
   - Deducts from non-expired batches in order, removing fully depleted batches.
5. Calls `ui.printWithdraw(quantity, item)` to display the withdrawn amount and the updated stock.
6. Records the withdrawal in the command history.

**Failure cases & messages:**
- If `n/` or `q/` is missing: "Invalid withdraw format. Format: withdraw n/NAME q/QUANTITY"
- If `n/` does not appear before `q/`: "Use correct format: Format: withdraw n/NAME q/QUANTITY"
- If name or quantity is empty: "Name and quantity must not be empty."
- If quantity is not a valid number: "Quantity must be a valid number."
- If quantity is zero or negative: "Quantity must be greater than 0."
- If item does not exist in inventory: "Product not found: \<name\>"
- If insufficient non-expired stock: "Insufficient stock for \<name\>. Available: \<available\>, Requested: \<quantity\>"

**Logging:**
- WARNING when attempting to get a non-existent item.
- FINE on successful item retrieval.

### Feature: Delete Item by Name

![DeleteCommandName_SequenceDiagram](diagrams/DeleteCommandName_SequenceDiagram.png)

**Purpose:** Delete an entire type of item from the inventory using the item's name.

**Command word:** `delete`

**Format:**
```
delete n/<name>
```

Finds the desired item using the input name given by the user and removes it from the inventory.

If the name of the item does not match any of the items in the inventory, prints "Product not found".

**Behaviour:**
1. Parses the user input to get name of desired item.
2. Calls `items.containsKey(<name>)` to check if an item of the given name exists in the inventory.
3. If it exists, retrieve the corresponding InventoryItem with that name and remove it from the inventory.
4. For the deleted item, calls `ui.printDelete(deletedItem)` to print the deleted item.
5. Inform the user that the item has been deleted successfully.

**Failure cases & messages:**
- If name does not exist in inventory: "Product not found: "
- If input format is invalid: "Invalid delete format. Format: delete 'n/NAME'"

**Logging:**
- INFO on command entry/exit and deleted item.

### Feature: Delete Item by Index

![DeleteCommandIndex_SequenceDiagram](diagrams/DeleteCommandIndex_SequenceDiagram.png)

**Purpose:** Delete an entire type of item from the inventory using the item's index in the inventory.

**Command word:** `delete`

**Format:**
```
delete i/<index>
```

Iterate through the inventory to obtain the name of the item at the index specified by the user. Finds the desired item 
using the obtained name and removes it from the inventory.

If the index is out of bounds, prints "Index entered out of bounds! Valid indices: ". If the index is invalid 
(ie not a number), prints "Index must be a valid number."

**Behaviour:**
1. Parses the user input to get the index.
2. Ensures that the input index is a positive integer that is within the size of the inventory.
3. Iterates through the inventory to retrieve the key of item at the index.
4. Retrieve the corresponding InventoryItem with the key and remove it from the inventory.
5. For the deleted item, calls `ui.printDelete(deletedItem)` to print the deleted item.
6. Inform the user that the item has been deleted successfully.

**Failure cases & messages:**
- If index out of bounds: "Index entered out of bounds! Valid indices: "
- If input format is invalid: "Invalid delete format. Format: delete 'i/INDEX'"
- If index is not an integer: "Index must be a valid number."

**Logging:**
- INFO on command entry/exit and deleted item.

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

![RemoveExpiredCommand_SequenceDiagram](diagrams/RemoveExpiredCommandSequenceDiagram.png)

**Purpose:** Manually remove all expired batches from the inventory, either across all items or for a specific item by name.

**Command word:** `remove-expired`

**Format:**
```
remove-expired
```
or
```
remove-expired n/<name>
```

When run without arguments, iterates through every item in the inventory, sorts batches by expiry date, marks expired ones, and removes them. When run with `n/<name>`, removes expired batches only from the specified item.

**Behaviour:**
1. Parser checks if the input is `remove-expired` (no arguments) or `remove-expired n/<name>`.
2. If no name is provided, creates a `RemoveExpiredCommand` with `name = null`.
3. If a name is provided, validates that the name is not empty and creates a `RemoveExpiredCommand` with the given name.
4. During execution:
   - **Without name:** Calls `inventory.removeAllExpiredBatches()` which iterates through all items, calling `item.removeExpiredBatches()` on each. Each item sorts its batches by expiry date, marks expired batches, collects them, and removes them. Returns the total count of removed batches. Calls `ui.printRemoveExpired(count)` to display the result.
   - **With name:** Calls `inventory.hasItem(name)` to check the item exists, then `inventory.getItem(name)` to retrieve it. Calls `item.removeExpiredBatches()` to remove expired batches from that specific item. Calls `ui.printRemoveExpired(name, count)` to display the result.

**Failure cases & messages:**
- If `n/` is present but name is empty: "Name must not be empty."
- If item does not exist in inventory: "Product not found: \<name\>"
- If no expired batches found (all items): "No expired batches found."
- If no expired batches found (specific item): "No expired batches found for \<name\>."

**Logging:**
- WARNING when attempting to get a non-existent item.

### Feature: Automatic Expiry Detection

![AutomaticExpiryDetection_SequenceDiagram](diagrams/AutomaticExpiryDetectionSequenceDiagram.png)

**Purpose:** Automatically detect and flag expired batches whenever inventory data is accessed, ensuring users are always warned about expired stock without needing to run a manual command.

This is not a user-invoked command. It is an internal mechanism triggered automatically during the execution of other commands.

**Trigger points:**
- `withdraw` command — before deducting stock
- `batch` command — after adding a new batch
- `list` command — when printing item details
- `remove-expired` command — before collecting expired batches

**Behaviour:**
1. When any of the above commands execute, `item.sortAndMarkExpiredBatches()` is called on the relevant `InventoryItem`.
2. `sortAndMarkExpiredBatches()` performs the following:
   - Sorts all batches by expiry date (earliest first) using `Comparator.comparing(Batch::getExpiryDate)`.
   - Iterates through each batch and checks if its expiry date is before today (`LocalDate.now()`).
   - If a batch is expired and not already marked, calls `batch.markExpired()` to set its `isExpired` flag to `true`.
   - Prints a warning message for each expired batch: "Please remove expired batch \<batchNumber\> (expired: \<expiryDate\>)".
3. Other methods such as `getQuantity()`, `getBatchQuantity()`, and `getActiveBatches()` filter out expired batches (where `isExpired == true`), ensuring expired stock is excluded from active totals and displays.

**Design rationale:**
Rather than requiring the user to manually check for expired stock, this mechanism ensures that expired batches are surfaced as warnings during routine operations (listing, withdrawing, adding batches). This reduces the risk of dispensing expired medicine.

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
