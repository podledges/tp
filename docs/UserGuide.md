# **User Guide**
## Quick Start

{Give steps to get started quickly}

1. Ensure that you have **Java 17** or above installed. <br> 
    ***Mac users*** : Ensure you have the precise JDK version prescribed [here](https://se-education.org/guides/tutorials/javaInstallationMac.html)
2. Down the latest version of `MediStock` from [here](https://github.com/AY2526S2-CS2113-W10-2/tp/releases).

## Features 

> **Command Format**
> * Words in `UPPER_CASE` are parameters to be supplied by the user.
> * flags before parameters such as `u/` must be keyed in as well. <br>
>
> **EXPIRY_DATE must** be formatted as **YYYY-MM-DD**. <br>
> **Dosage** should be included in **NAME**. *(if applicable)*

### Creating a new Medication Class: `create`
Creates a new medication entry in the inventory so its stock can be tracked.
* **Format:** `create n/NAME u/UNIT min/THRESHOLD`
* **Example:** `create n/Paracetamol 500mg u/Tablets min/250`
* **Example Output:**
	
    ```text
    Product created: Paracetamol 500mg (Tablets)
    Minimum threshold: 250
    ```

### Editing a Medication Class: `edit`
Edits an existing medication entry in the inventory. You can update its name, unit, minimum threshold, or any combination of these.
* **Format:** `edit o/OLD_NAME [n/NEW_NAME] [u/NEW_UNIT] [min/NEW_THRESHOLD]`
* **Example:** `edit o/Paracetamol 500mg n/Paracetamol 650mg`
* **Example Output:**

    ```text
    Product updated: Paracetamol 500mg -> Paracetamol 650mg (Tablets)
    Minimum threshold: 250
    ```
  
### Listing all Medications: `list`
List all items in the inventory with their corresponding stock quantity, earliest expiry       
date and stock health status.
* **Format:** `list`
* **Example Output:**

    ```text
    ____________________________________________________________
    Current Active Pharmaceutical Inventory:
    1. Paracetamol 500mg (Min: 250)
        Batch 1: 300 Tablets, Exp: 2030-09-30
        Total: 300 Tablets
        Status: Healthy

    2. Vyvanse 70mg (Min: 50)
        Batch 1: 60 Tablets, Exp: 2028-06-07
        Total: 60 Tablets
        Status: Critical
    ____________________________________________________________
    Current Expired Pharmaceutical Inventory:
    3. Amoxicillin 250mg
        Batch 1: 100 Capsules, Exp: 2024-01-15
    ____________________________________________________________
    ```

### Deleting a Pharmaceutical: `delete`
Removes a task from your list permanently.
* **Format:** `delete n/NAME` or `delete i/INDEX`
* **Example:** `delete n/Vyvanse 70mg` or `delete i/2`
* **Example Output:**

    ```text 
    ____________________________________________________________
    The following Pharmaceutical has been deleted   
    2. Vyvanse 70mg
     Total: 60 Tablets
     Earliest Exp: 2028-06-07
     Status: Critical
    ____________________________________________________________
    ```
  
### Adding an Item Batch: `batch`
Adds a batch of a specific item to the inventory. 

* **Format:** `batch n/NAME q/QUANTITY d/EXPIRY_DATE` <br>
* **Example:** `batch n/Vyvanse 70mg q/200 d/2028-06-07`
* **Example Output:**

    ```text
    Batch of 200 Tablets Vyvanse 70mg, expiring on 2028-06-07 
    was successfully added to the inventory!
    ____________________________________________________________
    Stock of Vyvanse is now:
      2. Vyvanse 70mg
      Total: 260 Tablets
      Earliest Exp: 2028-06-07
      Status: Healthy
    ____________________________________________________________
    ```
  
### Withdrawing Medication from the Inventory: `withdraw`, `wtd`
Withdraws a quantity of the keyed in medication from the database. 
* **Format:** `withdraw n/NAME q/QUANTITY`  ***or*** <br>
  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  `wtd n/NAME q/QUANTITY`
* **Example:** `wtd Paracetamol 500mg q/50`
* **Example Output:**

    ```text
    50 Tablets of Paracetamol has withdrawn from the Inventory 
    ____________________________________________________________
    Stock of Paracetamol is now:
      1. Paracetamol 500mg
      Total: 250 Tablets
      Earliest Exp: 2030-09-30
      Status: Crticial
    ____________________________________________________________  
    ```
  
### Removing Expired Batches: `remove-expired`
Removes all expired batches from the inventory. Can target all items or a specific item by name.
* **Format:** `remove-expired` or `remove-expired n/NAME`
* **Example (all items):** `remove-expired`
* **Example Output:**

    ```text
    ____________________________________________________________
    Removed 3 expired batch(es) from all items.
    ____________________________________________________________
    ```

* **Example (specific item):** `remove-expired n/Amoxicillin 250mg`
* **Example Output:**

    ```text
    ____________________________________________________________
    Removed 1 expired batch(es) from Amoxicillin 250mg.
    ____________________________________________________________
    ```

* If no expired batches are found:

    ```text
    ____________________________________________________________
    No expired batches found.
    ____________________________________________________________
    ```

### Quitting the Program: `exit`, `quit`
Saves the inventory and safely exits from the application. <br>

* Both `quit` and `exit` can be used to quit the application.
* Example output of `exit`: 

    ```text
    ____________________________________________________________
    Inventory saved. 
    Thank you for using MediStock, have a nice day!`
    ____________________________________________________________
    ```

## Saving the Data
-- to be added--

## Editing the data file
– to be added –

## FAQ

**Q**: - to be added - 

**A**: – to be added –

## Known Issues
– to be added –

### Command summary
The following table summarizes the available commands:

| Action                 | Format                                  | Example                              |
|:-----------------------|:----------------------------------------|:-------------------------------------|
| **CREATE ITEM**        | `create n/NAME u/UNIT min/THRESHOLD`    | `create n/Vyvanse u/70mg min/50`     |
| **LIST INVENTORY**     | `list`                                  | `list`                               |
| **DELETE ITEM**        | `delete n/NAME` or `delete i/INDEX`     | `delete n/Vyvanse u/70mg`            |
| **ADD BATCH OF ITEMS** | `batch n/NAME q/QUANTITY d/EXPIRY_DATE` | `batch n/Vyvanse q/200 d/2028-06-07` |
| **WITHDRAW ITEMS**     | `wtd n/NAME  q/QUANTITY`                | `wtd n/Vyvanse 70mg q/50`            |
| **REMOVE EXPIRED**     | `remove-expired` or `remove-expired n/NAME` | `remove-expired n/Amoxicillin 250mg` |
| **QUIT**               | `exit` or `quit`                        | `exit`                               |

> **EXPIRY_DATE must** be formatted as **YYYY-MM-DD**. <br>
> **Dosage** should be included in **NAME**. *(if applicable)*
