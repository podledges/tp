# **User Guide**

**MediStock** is a command-line inventory management application for pharmacists and clinical staff
who need a fast way to track pharmaceutical stock. It helps users manage medications, monitor
batch quantities and expiry dates, remove expired stock, and review inventory changes, all through
efficient typed commands. MediStock is designed for expert users who prefer an efficient CLI workflow
when handling routine stock-management tasks.

## Quick Start

1. Ensure that you have **Java 17** or above installed.  
   Mac users should follow the Java installation guide [here](https://se-education.org/guides/tutorials/javaInstallationMac.html).
2. Download the latest version of `MediStock` from [here](https://github.com/AY2526S2-CS2113-W10-2/tp/releases).
3. Open a terminal in the folder containing the downloaded JAR file.
4. Run `java -jar MediStock-v2.1.jar`.
5. Type a command and press Enter.

## Features

> **Command Format**
> - Words in `UPPER_CASE` are values to be supplied by the user.
> - Prefixes such as `n/`, `u/`, `q/`, and `d/` must be included exactly as shown.
> - `EXPIRY_DATE` must use the format `YYYY-MM-DD`.
> - Include the dosage in `NAME` when applicable, for example `Paracetamol 500mg`.

### Creating a Medication: `create`
Creates a new medication entry so its stock can be tracked.

* **Format:** `create n/NAME u/UNIT min/THRESHOLD`
* **Example:** `create n/Paracetamol 500mg u/Tablets min/250`
* **Example Output:**

    ```text
    ____________________________________________________________
    Product created:Paracetamol 500mg (Tablets)
    Minimum threshold: 250
    ____________________________________________________________
    ```


### Editing a Medication: `edit`
Edits an existing medication entry. You can update its name, unit, minimum threshold, or any combination of these.

* **Format:** `edit o/OLD_NAME [n/NEW_NAME] [u/NEW_UNIT] [min/NEW_THRESHOLD]`
* **Example:** `edit o/Paracetamol 500mg n/Paracetamol 650mg`
* **Example Output:**

    ```text
    ____________________________________________________________
    Product updated: Paracetamol 500mg -> Paracetamol 650mg (Tablets)
    Minimum threshold: 250
    ____________________________________________________________
    ```

    

### Listing the Inventory: `list`
Shows all active and expired inventory items, together with their batch information and stock status. <br> **Healthy:** stock is equal or above the minimum threshold. <br> **Critical:** stock is below the minimum threshold.

* **Format:** `list`
* **Example Output:**

    ```text
    ____________________________________________________________
    Current Active Pharmaceutical Inventory:
    1. Paracetamol 500mg (Min: 250)
        Batch 1: 300 Tablets, Exp: 2030-09-30
        Total: 300 Tablets
        Status: Healthy

    2. Vyvanse 70mg (Min: 100)
        Batch 1: 60 Tablets, Exp: 2028-06-07
        Total: 60 Tablets
        Status: Critical
    ____________________________________________________________
    Current Expired Pharmaceutical Inventory:
    3. Amoxicillin 250mg
        Batch 1: 100 Capsules, Exp: 2024-01-15
    ____________________________________________________________
    ```
>**Disclaimer:** Inventory sorting prioritizes expiration dates over batch numbers; hence, the batch at the top of the list is always the one closest to expiring.

User-Centric (Action-Oriented) 
### Adding a Batch: `batch`
Adds a batch to an existing medication entry. 
* **Format:** `batch n/NAME q/QUANTITY d/EXPIRY_DATE`
* **Example:** `batch n/Paracetamol 500mg q/200 d/2028-06-07`
* **Example Output:**

    ```text
    Batch of 200 Paracetamol 500mg, expiring on 2028-06-07
     has been successfully to the inventory!
    ____________________________________________________________
    Current Active Pharmaceutical Inventory:
    1. Paracetamol 500mg (Min: 250)
        Batch 2: 400 Tablets, Exp: 2030-11-30
        Batch 1: 300 Tablets, Exp: 2030-09-30
        Total: 300 Tablets
        Status: Healthy
    ____________________________________________________________
    Current Expired Pharmaceutical Inventory:
    2. Vyvanse 70mg
        Batch 1: 100 Capsules, Exp: 2024-01-15
    ____________________________________________________________
    ```
> **Adding Expired Batches:** 
> If the specified expiry date is in the current past, the system will trigger a confirmation dialog to prevent accidental entries. 
> You must respond with y or a space to proceed. Inputting any other character will immediately abort the command. 
>
> **input:** ```batch n/Vyvanse 70mg q/400 d/2025-11-30```<br>
> **Example Output:** 
> ```text
> This batch has already expired (2025-11-30).
> Do you want to continue anyways? (y/n)
> ```
> 

<div style="page-break-after: always;"></div>

### Viewing Command History: `history`
Lists the history of changes in the inventory which includes `create`, `batch`, `withdraw`, `delete` and `remove-expired`.
* **Format:** `history`
* **Example Output:**

    ```text
    ____________________________________________________________
    History of Stocks:
    1. Created 'Vyvanse 70mg' of 'tablets' unit with minimum threshold of 10.
    2. Added a batch of 200 tablets of Vyvanse 70mg with expiry date 2028-06-07.
    3. Withdrawn 50 tablets of 'Vyvanse 70mg'.
    4. Deleted 'Vyvanse 70mg'.
    ____________________________________________________________
    ```
    
<div style="page-break-after: always;"></div>

### Finding a Medication: `find`
Shows medications whose names contain the given keyword.

* **Format:** `find KEYWORD`
* **Example:** `find Paracetamol`
* **Example Output:**

    ```text
    ____________________________________________________________
    Here are the matching items in your inventory:
    1. Paracetamol 500mg (Min: 250)
        Active Batches:
            Batch 1: 200 Tablets, Exp: 2028-06-07
        Total (active): 200 Tablets
        Status: Critical
    ____________________________________________________________
    ```

### Deleting a Medication Class: `delete`
Removes an entire Medication Class from the inventory permanently.
* **Format:** `delete n/NAME` or `delete i/INDEX`
* **Example:** `delete n/Paracetamol 500mg` or `delete i/2`
* **Example Output:**

    ```text 
    ____________________________________________________________
    Product deleted: Paracetamol 500mg (Tablets)
    ____________________________________________________________
    ```

### Withdrawing Medication: `withdraw`
Withdraws a quantity from an existing medication entry.

* **Format:** `withdraw n/NAME q/QUANTITY`
* **Example:** `withdraw n/Paracetamol 500mg q/50`
* If a medication has multiple batches, MediStock withdraws stock from the earliest-expiring batch first.
* Withdrawing an expired item is not possible. To remove expired stock, use `remove-expired`.
* **Example Output:**

    ```text
    Withdrawn 50 Paracetamol 500mg from inventory.
    ____________________________________________________________
    Stock of Paracetamol 500mg is now: 150 Tablets
    ____________________________________________________________
    ```

* **Example Output (insufficient stock):**

    ```text
    ____________________________________________________________
    Insufficient stock for Paracetamol 500mg. Available: 0, Requested: 50
    ____________________________________________________________
    ```

### Removing Expired Batches: `remove-expired`
Removes expired batches from all items or from a specific medication.

* **Format:** `remove-expired` or `remove-expired n/NAME`
* **Example (all items):** `remove-expired`
* **Example Output:**

    ```text
    ____________________________________________________________
    Removed 3 expired batch(es) from all items.
    ____________________________________________________________
    ```

* **Example (specific item):** `remove-expired n/Paracetamol 500mg`
* **Example Output:**

    ```text
    ____________________________________________________________
    Removed 1 expired batch(es) from Paracetamol 500mg.
    ____________________________________________________________
    ```

* If no expired batches are found for the specified medication:

    ```text
    ____________________________________________________________
    No expired batches found for Paracetamol 500mg.
    ____________________________________________________________
    ```

### Showing the Command List: `help`
Shows the built-in list of available commands.

* **Format:** `help`
* **Example Output:**

    ```text
    ____________________________________________________________
    Available commands:
    1. list
    2. create Format: create n/NAME u/UNIT min/THRESHOLD
    3. edit Format: edit o/OLD_NAME [n/NEW_NAME] [u/NEW_UNIT] [min/NEW_THRESHOLD]
    4. delete Format: delete 'n/NAME' or 'i/INDEX'
    5. batch Format: batch n/NAME q/QUANTITY d/EXPIRY_DATE(YYYY-MM-DD)
    6. withdraw Format: withdraw n/NAME q/QUANTITY
    7. find Format: find <keyword>
    8. remove-expired Format: remove-expired or remove-expired n/NAME
    9. history
    10. exit
    ____________________________________________________________
    ```

### Exiting the Program: `exit`, `quit`
Saves the inventory and exits the application.

* **Format:** `exit` or `quit`
* **Example Output:**

    ```text
    ____________________________________________________________
    Inventory saved
    Thank you for using MediStock, have a nice day!
    ____________________________________________________________
    ```

## Local Storage
* **Automatic  Saving:** Any changes made to the inventory (such as adding medications or batches) are
immediately saved to your local storage after a command executes successfully.
* **Command History:** Your command history is also tracked and saved automatically

The following runtime files are created locally for storage:
- `data/Inventory.txt`
- `data/History.txt`
- `data/medistock.log`


>**Warning:** You may edit `data/Inventory.txt` and `data/History.txt` manually, however, do not do so while
MediStock is running. <br> 
> - *Edits made during an active session will be overwritten.* <br>
> - *If the file is manually corrupted or edited incorrectly*, MediStock may output an error message on launch or fail to load saved data properly.*  <br>

## FAQ
**Q:** Where is my data saved?  
**A:** MediStock saves its inventory, command history, and application logs in the `data/` folder, specifically in 
`data/Inventory.txt`, `data/History.txt`, and `data/medistock.log`.

**Q:** What is a batch, and why do I need the `batch` command?  
**A:** A batch is one stock entry of the same medication with its own quantity and expiry date. This is useful because 
you may receive the same medication multiple times with different expiry dates. For example, you can have two batches 
of `Paracetamol 500mg`, each with a different expiry date. MediStock keeps those batches separate and withdraws from 
the earliest-expiring batch first.'

**Q:** What is the batch number even tracking? <br>
**A:** The Batch Number (i.e **2** in Batch ```Batch 2: 60 Tablets, Exp: 2028-06-07)```, tracks the cumulative 
count of batches for an item; it increments sequentially with every new batch added and **can be reset by deleting and 
recreating the item**.

**Q:** Where can I find the index of the medical item?  
**A:** The index is the number shown beside the item in the output of the `list` command.

**Q:** Why is my stock healthy, but the withdraw command says I have insufficient stock to withdraw?
**A:** The application only allows withdrawal from non-expired batches. A `Healthy` status only means the current stock 
is above the medication’s minimum threshold. It does not guarantee that there is enough non-expired stock to fulfill 
every withdrawal request. If the available non-expired stock is less than the requested quantity, the application will 
show an insufficient stock message.

### Known Issues
- Manual edits to `data/Inventory.txt` while the application is running will be
  overwritten and lost on the next save.
- Expiry dates are not validated against the current date at input time; a user
  may enter a date that is already expired without receiving a warning.
<div style="page-break-after: always;"></div>

## Command Summary
The following table summarizes the available commands:

| Action             | Format                                                          | Example                                        |
|:-------------------|:----------------------------------------------------------------|:-----------------------------------------------|
| **CREATE ITEM**    | `create n/NAME u/UNIT min/THRESHOLD`                            | `create n/Paracetamol 500mg u/Tablets min/250` |
| **EDIT ITEM**      | `edit o/OLD_NAME [n/NEW_NAME] [u/NEW_UNIT] [min/NEW_THRESHOLD]` | `edit o/Paracetamol 500mg n/Paracetamol 650mg` |
| **LIST INVENTORY** | `list`                                                          | `list`                                         |
| **FIND ITEM**      | `find KEYWORD`                                                  | `find Paracetamol`                             |
| **DELETE ITEM**    | `delete n/NAME` or `delete i/INDEX`                             | `delete n/Paracetamol 500mg`                   |
| **ADD BATCH**      | `batch n/NAME q/QUANTITY d/EXPIRY_DATE`                         | `batch n/Paracetamol 500mg q/200 d/2028-06-07` |
| **WITHDRAW ITEM**  | `withdraw n/NAME q/QUANTITY`                                    | `withdraw n/Paracetamol 500mg q/50`            |
| **HISTORY**        | `history`                                                       | `history`                                      |
| **REMOVE EXPIRED** | `remove-expired` or `remove-expired n/NAME`                     | `remove-expired n/Paracetamol 500mg`           |
| **HELP**           | `help`                                                          | `help`                                         |
| **EXIT**           | `exit` or `quit`                                                | `exit`                                         |

> **Notes:**
> - `EXPIRY_DATE` must be formatted as `YYYY-MM-DD`.
> - Include the dosage in `NAME` when applicable.
> - For `edit`, at least one optional field must be provided.
> - Square brackets `[ ]` indicate optional parameters.
