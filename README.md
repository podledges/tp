# MediStock

Overview
MediStock is a command-line inventory system for pharmacists and clinic staff to manage medicine stock by batch and expiry date.

Prerequisites
JDK 17 (use the exact version), update Intellij to the most recent version.

Manual Set-up

1. **Ensure Intellij JDK 17 is defined as an SDK**, as described [here](https://www.jetbrains.com/help/idea/sdk.html#set-up-jdk) -- this step is not needed if you have used JDK 17 in a previous Intellij project.
1. **Import the project _as a Gradle project_**, as described [here](https://se-education.org/guides/tutorials/intellijImportGradleProject.html).
1. **Verify the setup**: After the importing is complete, locate the `src/main/java/seedu/duke/Duke.java` file, right-click it, and choose `Run Duke.main()`. If the setup is correct, you should see something like the below:
 

Simple Set-up [Recommended] (To be completed)
1. Ensure you have Java 17 or above installed in your Computer.
	Mac users: Ensure you have the precise JDK version prescribed here.
2. Download the latest .jar file from here.
3. Copy the file to the folder you want to use as the home folder for your AddressBook.
4. Open a command terminal, cd into the folder you put the jar file in, and use the java -jar addressbook.jar command to run the application.
A GUI similar to the below should appear in a few seconds. Note how the app contains some sample data.

Features
Creating a new Medication Class: create
	Creates a new medication product in the database to be monitored.

	Format:  		create n/<NAME> u/<UNIT>
	Example:	
create n/Paracetamol 500mg u/tablets
	Example Output:
Product created: Paracetamol 500mg (tablets)

Listing all Medications: list
List all products with its corresponding current quantity, earliest expiring       
date and stock health status.

	Follows First Expiry First Out (FEFO) logic, displays expiry of earliest expiry 
only.

	Format:				list
	Example Output: 	Paracetamol 500mg
  						Total: 350 tablets
 		 				Earliest Exp: 2030-09-30
   						Status: Healthy

						Vyvanse 70mg
   						Total: 40 pieces
						Earliest Exp: 2026-03-10
						Status: Low Stock

Deleting a Medication: delete
Delete a medication completely from the database, using the two methods. 

	Format: 		delete n/<NAME> 
					delete i/<INDEX>
	Example:
					delete Paracetamol q/200 exp/2026-12-31
					delete 1
	Example Output: 
					Deleted product:
					Paracetamol 500mg

Adding a Medication Batch: batch
Adds a batch of medical stock.

	Format: 		batch n/<NAME> q/<QUANTITY> exp/<EXPIRY_DATE>

	Example:
					batch Paracetamol q/200 exp/2026-12-31
					Example Output: 
					Added batch to Paracetamol 500mg
					Quantity: 200
					Expiry: 2026-12-31

Withdrawing Medication from the System: withdraw, wtd
Withdraws a quantity of the keyed in medication from the database. 

	Format: 		wtd PRODUCT NAME q/QUANTITY
					withdraw PRODUCT NAME q/QUANTITY
				(Both withdraw and wtd can be used as command words in the CLI)
	Example:
					wtd Paracetamol q/50
					Example Output: 
					Withdrawn 50 tablets.
					Remaining total: 300 tablets

Quitting the Program: exit, quit
Type quit in the CLI to end the program.

	Format: 	 	exit

Saving the data
– to be added – 

Editing the data file
– to be added – 

FAQ
– to be added – 

Known issues
– to be added – 


Command summary

Action
Format
ADD
create n/NAME u/UNIT
LIST
list
DELETE
delete n/<NAME> 
delete i/<INDEX> 
BATCH
batch n/<NAME> q/<QUANTITY> exp/<EXPIRY_DATE>
WITHDRAW
withdraw PRODUCT NAME q/QUANTITY
wtd PRODUCT NAME q/QUANTITY
QUIT
quit or exit




