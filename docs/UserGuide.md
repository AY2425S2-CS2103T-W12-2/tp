---
  layout: default.md
  title: "User Guide"
  pageNav: 3
---

# WealthVault User Guide

WealthVault is a **desktop app for managing contacts, optimized for use via a  Line Interface** (CLI) while still having the benefits of a Graphical User Interface (GUI). If you can type fast, WealthVault can get your contact management tasks done faster than traditional GUI apps.

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## Quick start

1. Ensure you have Java `17` or above installed in your Computer.<br>
   **Mac users:** Ensure you have the precise JDK version prescribed [here](https://se-education.org/guides/tutorials/javaInstallationMac.html).

1. Download the latest `.jar` file from [here](https://github.com/AY2425S2-CS2103T-W12-2/tp/releases/tag/v1.3).

1. Copy the file to the folder you want to use as the _home folder_ for your application.

1. Open a command terminal, `cd` into the folder you put the jar file in, and use the `java -jar wealthvault.jar` command to run the application.<br>
   A GUI similar to the below should appear in a few seconds. Note how the app contains some sample data.<br>
   ![Ui](images/Ui.png)

1. Type the command in the command box and press Enter to execute it. e.g. typing **`help`** and pressing Enter will open the help window.<br>
   Some example commands you can try:

   * `list` : Lists all contacts.

   * `addclient n/John Doe p/98765432 e/johnd@example.com a/John street, block 123, #01-01` : Adds a contact named `John Doe` to the application.

   * `deleteclient 3` : Deletes the 3rd contact shown in the current list.

   * `clear` : Deletes all contacts.

   * `exit` : Exits the app.

1. Refer to the [Features](#features) below for details of each command.

--------------------------------------------------------------------------------------------------------------------

## Features

<box type="info" seamless>

**Notes about the command format:**<br>

* Words in `UPPER_CASE` are the parameters to be supplied by the user.<br>
  e.g. in `addclient n/NAME`, `NAME` is a parameter which can be used as `addclient n/John Doe`.

* Items in square brackets are optional.<br>
  e.g `n/NAME [t/POLICY_TAG]` can be used as `n/John Doe t/friend` or as `n/John Doe`.

* Items with `…`​ after them can be used multiple times including zero times.<br>
  e.g. `[t/POLICY_TAG]…​` can be used as ` ` (i.e. 0 times), `t/friend`, `t/friend t/family` etc.

* Parameters can be in any order.<br>
  e.g. if the command specifies `n/NAME p/PHONE_NUMBER`, `p/PHONE_NUMBER n/NAME` is also acceptable.

* Extraneous parameters for commands that do not take in parameters (such as `help`, `list`, `exit` and `clear`) will be ignored.<br>
  e.g. if the command specifies `help 123`, it will be interpreted as `help`.

* If you are using a PDF version of this document, be careful when copying and pasting commands that span multiple lines as space characters surrounding line-breaks may be omitted when copied over to the application.
</box>

### Viewing help : `help`

Shows a message explaning how to access the help page.

![help message](images/helpMessage.png)

Format: `help`

..............!!!


### Listing all persons : `list`

Shows a list of all persons in the address book.

Format: `list`

### Editing a person : `edit`

Edits an existing person in the address book. This command allows changing any client details including name and tags.

Format: `edit INDEX [n/NAME] [p/PHONE] [e/EMAIL] [a/ADDRESS] [t/POLICY_TAG]…​`

* Edits the person at the specified `INDEX`. The index refers to the index number shown in the displayed person list. The index **must be a positive integer** 1, 2, 3, …​
* At least one of the optional fields must be provided.
* Existing values will be updated to the input values.
* Phone number can vary in length, therefore there is no restriction placed on it
* Name can contain 

Examples:
*  `edit 1 p/91234567 e/johndoe@example.com` Edits the phone number and email address of the 1st person to be `91234567` and `johndoe@example.com` respectively.
*  `edit 2 n/Betsy Crower t/` Edits the name of the 2nd person to be `Betsy Crower` and clears all existing tags.

### Updating contact information : `update`

Updates only the contact information (phone, email, address) of an existing person in the address book. Unlike the `edit` command, this command cannot change the client's name or tags.

Format: `update INDEX [p/PHONE] [e/EMAIL] [a/ADDRESS]`

* Updates the person at the specified `INDEX`. The index refers to the index number shown in the displayed person list. The index **must be a positive integer** 1, 2, 3, …​
* At least one of the optional fields must be provided.
* Existing values will be updated to the input values.
* Name and tags cannot be modified using this command. Use the `edit` command instead.

Examples:
*  `update 1 p/91234567 e/johndoe@example.com` Updates the phone number and email address of the 1st person to be `91234567` and `johndoe@example.com` respectively.
*  `update 2 a/Clementi Ave 6` Updates only the address of the 2nd person to be `Clementi Ave 6`.

### Locating persons by name: `findclient`

Finds persons whose names contain any of the given keywords.

Format: `findclient KEYWORD [MORE_KEYWORDS]`

* The search is case-insensitive. e.g `hans` will match `Hans`
* The order of the keywords does not matter. e.g. `Hans Bo` will match `Bo Hans`
* Only the name is searched.
* Only full words will be matched e.g. `Han` will not match `Hans`
* Persons matching at least one keyword will be returned (i.e. `OR` search).
  e.g. `Hans Bo` will return `Hans Gruber`, `Bo Yang`

Examples:
* `findclient John` returns `john` and `John Doe`
* `findclient alex david` returns `Alex Yeoh`, `David Li`<br>
  ![result for 'find alex david'](images/findAlexDavidResult.png)

### Locating persons by name and tag: `findany`

Finds persons whose name or tag contain any of the given keywords.

Format: `findany KEYWORD [MORE_KEYWORDS]`

* The search is case-insensitive. e.g `hans` will match `Hans`
* The order of the keywords does not matter. e.g. `Hans Bo` will match `Bo Hans`
* Both the name and the tag is searched.
* Only full words will be matched e.g. `Han` will not match `Hans`
* Persons matching at least one keyword will be returned (i.e. `OR` search).
  e.g. `Hans Bo` will return `Hans Gruber`, `Bo Yang`

Examples:
* `findany John` returns `john` and `John Doe`
* `findany Jo priority` returns only clients whose name or tags matches either `priority` or `Jo` attached<br>
  ![result for 'find alex david'](images/findclientorAlexDavidPriority.png)

Potential Errors:

Errors           | Reason             |Fixes
-----------------|--------------------|------------------------
"Field is empty" | This error is thrown when no keywords were provided with the `findany` command. | To fix this error, simply supply some keywords

### Locating specific persons by name and tag: `findclientand`

Finds persons whose name or tag contain all of the given keywords.

Format: `findall KEYWORD [MORE_KEYWORDS]`

* The search is case-insensitive. e.g `hans` will match `Hans`
* The order of the keywords does not matter. e.g. `Hans Bo` will match `Bo Hans`
* Both the name and the tag is searched.
* Only full words will be matched e.g. `Han` will not match `Hans`
* Persons matching at least one keyword will be returned (i.e. `OR` search).
  e.g. `Hans Bo` will return `Hans Gruber`, `Bo Yang`

Examples:
* `findall John Doe` would return `John Doe` but not `Jane Doe` or `John Snow`
* `findall ng priority` returns only clients whose name or tags matches both `priority` and `ng` attached<br>
  ![result for 'find alex david'](images/findclientandFriendsPriority.png)

Potential Errors:

Errors           | Reason             |Fixes
-----------------|--------------------|------------------------
"Field is empty" | This error is thrown when no keywords were provided with the `findany` command. | To fix this error, simply supply some keywords

### Deleting a person : `deleteclient`

Deletes the specified person from the address book.

Format: `deleteclient INDEX`

* Deletes the person at the specified `INDEX`.
* The index refers to the index number shown in the displayed person list.
* The index **must be a positive integer** 1, 2, 3, …​

Examples:
* `list` followed by `deleteclient 2` deletes the 2nd person in the address book.
* `findclient Betsy` followed by `deleteclient 1` deletes the 1st person in the results of the `findclient` command.

### Deleting a policy: `deletepolicy`

Deletes the specified policy from the address book.

Format: `deletepolicy INDEX`

* Deletes the policy at the specified `INDEX`.
* The index refers to the index number shown in the displayed policy list.
* The index **must be a positive integer** 1, 2, 3, …​

Examples:
* `listpolicy` followed by `deletepolicy 2` deletes the 2nd policy in the address book.
* `findpolicy Home` followed by `deletepolicy 1` deletes the 1st policy in the results of the `findpolicy` command.


### Prioritising a person: `priority`

Toggles the priority of specified person from the application as indicated with a `Priority` tag.

Format: `priority INDEX [MORE INDEX]`

* Adds a "Priority" tag to the specified `INDEX` if such a tag isn't attached to the person
* Removes the "Priority" tag of the specified `INDEX` if such a tag is already attached to the person
* The index **must be a positive integer** 1, 2, 3, …​

Examples:
* `list` followed by `priority 1 3` adds a priority tag to the 1st person and 3rd person in the list if the person is yet to be attached with a "Priority" tag.
  Before           |After
  -----------------|--------------------
  ![before priority command](images/priorityCommand1.png) | ![after 'priority 1 3'](images/priorityCommand2.png)

* `list` followed by `priority 3` removes a priority tag from the 3rd person in the list if the person is attached with a "Priority" tag.
  Before           |After
  -----------------|--------------------
  ![before priority command](images/priorityCommand2.png) | ![after 'priority 3'](images/priorityCommand3.png)

### Clearing all entries : `clear`

Clears all entries from the address book.

Format: `clear`

### Exiting the program : `exit`

Exits the program.

Format: `exit`

### Saving the data

WealthVault data are saved in the hard disk automatically after any command that changes the data. There is no need to save manually.

### Editing the data file

WealthVault data are saved automatically as a JSON file `[JAR file location]/data/addressbook.json`. Advanced users are welcome to update data directly by editing that data file.

<box type="warning" seamless>

**Caution:**
If your changes to the data file makes its format invalid, WealthVault will discard all data and start with an empty data file at the next run.  Hence, it is recommended to take a backup of the file before editing it.<br>
Furthermore, certain edits can cause the WealthVault to behave in unexpected ways (e.g., if a value entered is outside the acceptable range). Therefore, edit the data file only if you are confident that you can update it correctly.
</box>

### Archiving data files `[coming in v2.0]`

_Details coming soon ..._

--------------------------------------------------------------------------------------------------------------------

## FAQ

**Q**: How do I transfer my data to another Computer?<br>
**A**: Install the app in the other computer and overwrite the empty data file it creates with the file that contains the data of your previous WealthVault home folder.

--------------------------------------------------------------------------------------------------------------------

## Known issues

1. **When using multiple screens**, if you move the application to a secondary screen, and later switch to using only the primary screen, the GUI will open off-screen. The remedy is to delete the `preferences.json` file created by the application before running the application again.
2. **If you minimize the Help Window** and then run the `help` command (or use the `Help` menu, or the keyboard shortcut `F1`) again, the original Help Window will remain minimized, and no new Help Window will appear. The remedy is to manually restore the minimized Help Window.

--------------------------------------------------------------------------------------------------------------------

## Command summary

| Action            | Format, Examples                                                                                                                                                                        |
|------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Add Client**     | `addclient n/NAME p/PHONE_NUMBER e/EMAIL a/ADDRESS [t/POLICY_TAG]…​` <br> e.g., `addclient n/James Ho p/22224444 e/jamesho@example.com a/123, Clementi Rd, 1234665 t/friend t/colleague` |
| **Add Policy**     | `addpolicy INDEX t/POLICY_TAG ` <br> e.g., `addpolicy 1 t/Health Insurance`                                                                                                             |
| **Clear**         | `clear`                                                                                                                                                                                 |
| **Delete Client**  | `deleteclient INDEX`<br> e.g., `deleteclient 3`                                                                                                                                         |
| **Delete Policy**  | `deletepolicy INDEX t/POLICY_TAG`<br> e.g., `deletepolicy 2 t/Health Insurance`                                                                                                         |
| **Edit**          | `edit INDEX [n/NAME] [p/PHONE_NUMBER] [e/EMAIL] [a/ADDRESS] [t/POLICY_TAG]…​`<br> e.g.,`edit 2 n/James Lee e/jameslee@example.com`                                                      |
| **Update**        | `update INDEX [p/PHONE_NUMBER] [e/EMAIL] [a/ADDRESS]`<br> e.g.,`update 2 p/91234567 e/jameslee@example.com`                                                                            |
| **Find**          | `findclient KEYWORD [MORE_KEYWORDS]`<br> e.g., `findclient James Jake`                                                                                                                  |
| **Find (Or)**     | `findclientor KEYWORD [MORE_KEYWORDS]`<br> e.g., `findclientor James Jake`                                                                                                              |
| **Find (And)**    | `findclientand KEYWORD [MORE_KEYWORDS]`<br> e.g., `findclientand James Jake`                                                                                                            |
| **Priority**      | `priority INDEX`<br> e.g.,`priority 1`                                                                                                                                                  |
| **List**          | `list`                                                                                                                                                                                  |
| **Help**          | `help`                                                                                                                                                                                  |


