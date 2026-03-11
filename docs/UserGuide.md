# **MediStock User Guide**
> MediStock is a command-line inventory system for pharmacists and clinical staff 
> to track and manage their pharmaceutical stock! <br>

-screenshot to be added-

## Quick Start

{Give steps to get started quickly}

1. Ensure that you have **Java 17** or above installed. <br> 
    ***Mac users*** : Ensure you have the precise JDK version prescribed [here](https://se-education.org/guides/tutorials/javaInstallationMac.html)
2. Down the latest version of `MediStock` from [here](https://github.com/AY2526S2-CS2113-W10-2/tp/releases).

## Features 

> **Command Format**
> * Words in `UPPER_CASE` are parameters to be supplied by the user.
> * flags before parameters such as `u/` must be keyed in as well.

### Creating a new Medication Class: `create`
Creates a new medication product in the database to be monitored.
* **Format:** `create n/NAME u/UNIT min/THRESHOLD`
* **Example:** `create n/Paracetamol u/500mg min/250`
* **Expected Output:**
  ```text
  Product created: Paracetamol 500mg Minimum threshold:500
  
### Listing all Medications: `list`
List all items in the inventory with their corresponding stock quantity, earliest expiry       
date and stock health status.
* **Format:** `list`
  ``` text
  Current Pharmaceutical Inventory: 
  ____________________________________________________________
  1. Paracetamol 500mg 
        Total: 300 Tablets 
        Earliest Exp: 2030-09-30
        Status: Healthy
  2. Vyvanse 70mg
        Total: 60 Tablets 
        Earliest Exp: 2028-06-07
        Status: Critical
  ____________________________________________________________```

### Deleting a Pharmaceutical: `delete`
Removes a task from your list permanently.
* **Format:** `delete n/NAME u/UNIT` or `delete i/INDEX`
* **Example:** `delete n/Vyvanse u/70mg` or `delete i/2`
    ``` text
  The following Pharmaceutical has been deleted   
  ____________________________________________________________
  2. Vyvanse 70mg
   Total: 60 Tablets
   Earliest Exp: 2028-06-07
   Status: Critical
  ____________________________________________________________

### Adding an Item Batch: `batch`
Adds a batch of a specific item to the inventory. 

* **Format:** `batch n/NAME u/UNIT q/QUANTITY` 
* **Example:** `batch n/Vyvanse u/70mg q/200`
    ``` text
  200 Tablets of Vyvanse has added to the Inventory 
  ____________________________________________________________
  Stock of Vyvanse is now:
    2. Vyvanse 70mg
    Total: 260 Tablets
    Earliest Exp: 2028-06-07
    Status: Healthy
  ____________________________________________________________

### Withdrawing Medication from the Inventory: `withdraw`, `wtd`
Withdraws a quantity of the keyed in medication from the database. 
* **Format:** `withdraw <PRODUCT_NAME> u/<UNIT> q/<QUANTITY>`  ***or*** <br>
  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  `wtd <PRODUCT_NAME> u/<UNIT> q/<QUANTITY>`
* **Example:** `wtd Paracetamol u/500mg q/50`
* **Expected Output:**
  ```text
  50 Tablets of Paracetamol has withdrawn from the Inventory 
  ____________________________________________________________
  Stock of Paracetamol is now:
    1. Paracetamol 500mg
    Total: 250 Tablets
    Earliest Exp: 2030-09-30
    Status: Crticial
  ____________________________________________________________

### Quitting the Program: `bye`
Saves the inventory and safely exits from the application. <br>

* `bye`
Example of usage: 

```text
Inventory saved. 
Thank you for using MediStock, have a nice day!`
```

## Saving the Data
-- to be added--

## Editing the data file
– to be added –

## FAQ

**Q**: What should I put for the u/UNIT flag, when adding pharmaceuticals like Plasters? 

**A**: – to be added –

## Known Issues
– to be added –

### Command summary
The following table summarizes the available commands:

| Action               | Format                                                                     | Example                          |
|:---------------------|:---------------------------------------------------------------------------|:---------------------------------|
| **CREATE ITEM** | `create n/NAME u/UNIT min/THRESHOLD`                                       | `create n/Vyvanse u/70mg min/50` |
| **LIST ITEMS** | `list`                                                                     | `list`                           |
| **DELETE ITEM** | `delete n/NAME u/UNIT` or `delete i/INDEX`                                 | `delete i/2`                     |
| **ADD ITEM BATCH** | `batch n/NAME u/UNIT q/QUANTITY`                                           | `batch n/Vyvanse u/70mg q/200`   |
| **WITHDRAW ITEM** | `wtd <PRODUCT_NAME> u/<UNIT> q/<QUANTITY>`                                 | `wtd n/Vyvanse u/70mg q/50`      |
| **QUIT** | `exit` or `quit`                                                           | `exit`                           |