---
  layout: default.md
  title: "Developer Guide"
  pageNav: 3
---
<style>
body {
    font-size: 1em !important;
}
</style>

# WealthVault Developer Guide

<!-- * Table of Contents -->
<page-nav-print />

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

---

## **Design**

### Architecture

<puml src="diagrams/ArchitectureDiagram.puml" width="280" />

The **_Architecture Diagram_** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.

- At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
- At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

- [**`UI`**](#ui-component): The UI of the App.
- [**`Logic`**](#logic-component): The command executor.
- [**`Model`**](#model-component): Holds the data of the App in memory.
- [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The _Sequence Diagram_ below shows how the components interact with each other for the scenario where the user issues the command `delc 1`.

<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="574" />

Each of the four main components (also shown in the diagram above),

- defines its _API_ in an `interface` with the same name as the Component.
- implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<puml src="diagrams/ComponentManagers.puml" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

<puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component"/>

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `ClientListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

- executes user commands using the `Logic` component.
- listens for changes to `Model` data so that the UI can be updated with the modified data.
- keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
- depends on some classes in the `Model` component, as it displays `Client` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<puml src="diagrams/LogicClassDiagram.puml" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

<puml src="diagrams/DeleteSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `delete 1` Command" />

<box type="info" seamless>

**Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</box>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a client).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<puml src="diagrams/ParserClasses.puml" width="600"/>

How the parsing works:

- When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
- All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component

**API** : [`Model.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/model/Model.java)

<puml src="diagrams/ModelClassDiagram.puml" width="450" />

The `Model` component,

- stores WealthVault data i.e., all `Client` objects (which are contained in a `UniqueClientList` object).
- stores the currently 'selected' `Client` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Client>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
- stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
- does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<box type="info" seamless>

**Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `AddressBook`, which `Client` references. This allows `AddressBook` to only require one `Tag` object per unique tag, instead of each `Client` needing their own `Tag` objects.<br>

<puml src="diagrams/BetterModelClassDiagram.puml" width="450" />

</box>

### Storage component

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<puml src="diagrams/StorageClassDiagram.puml" width="550" />

The `Storage` component,

- can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
- inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
- depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

---

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### Add Client

The `addc` command adds a client from WealthVault based on the specified index.

The Class Diagram of a Client can be [seen in the diagram above](#model-component).

#### Implementation

The `addc` command adds a client at a given index. The following sequence diagrams  
illustrate how this process flows through the logic component:

<puml src="diagrams/AddClientSequenceDiagram-Logic.puml" width="650" />

The following sequence diagram illustrates how the `AddClientCommand` class interacts with  
model components to add a client to WealthVault:

<puml src="diagrams/AddClientSequenceDiagram-Client.puml" width="600" />

As seen above, the `execute` method adds the `Client` to WealthVault. If the index is invalid, an error is thrown.

### Add Policy

The `addp` command adds a policy (tag) to a client's list of policies.

The Class Diagram of a Client can be [seen in the diagram above](#model-component).

#### Implementation

The `addp` command adds a policy to the client at the given index.
The following sequence diagrams illustrate how this process flows through the logic component:

<puml src="diagrams/AddPolicySequenceDiagram.puml" width="650" />

As seen above, the `execute` method adds the `Policy` to the client at the specified index. If the index is invalid, an error is thrown.

### Delete Client

The `delc` command deletes a client from WealthVault based on the specified index.

The Class Diagram of a Client can be [seen in the diagram above](#model-component).

#### Implementation

The `delc` command deletes a client at a given index. The following sequence diagrams  
illustrate how this process flows through the logic component:

<puml src="diagrams/DeleteClientSequenceDiagram-Logic.puml" width="650" />

The following sequence diagram illustrates how the `DeleteClientCommand` class interacts with model components to delete a client from WealthVault:

<puml src="diagrams/DeleteClientSequenceDiagram-Client.puml" width="600" />

As seen above, the `execute` method retrieves the `Client` at the specified index and deletes it from WealthVault. If the index is invalid, an error is thrown.

### Delete Multiple Clients

The `deleteclientmult` command deletes multiple clients from WealthVault based on the specified indices.

#### Implementation

The `deleteclientmult` command deletes multiple clients at given indices. The following sequence diagrams illustrate how this process flows through the logic component:

<puml src="diagrams/DeleteClientMultSequenceDiagram.puml" width="650" />

As seen above, the `execute` method retrieves each `Client` at the specified indices and deletes them from WealthVault. If any index is invalid, an error is thrown.

#### Design Considerations

**Aspect: How to specify multiple indices:**

- **Alternative 1 (current choice):** Use `i/` prefix for each index

  - Pros: Clear and explicit format, reduces input errors
  - Cons: Slightly more verbose

- **Alternative 2:** Use space-separated indices
  - Pros: More concise
  - Cons: Higher chance of input errors, less explicit

The current implementation uses Alternative 1 as it provides better clarity and reduces the likelihood of user input errors.

### Delete Policy

The `delp` command deletes a policy (tag) from a client's list of policies.

The Class Diagram of a Client can be [seen in the diagram above](#model-component).

#### Implementation

The `delp` command deletes a policy from the client at the given index.  
The following sequence diagrams illustrate how this process flows through the logic component:

<puml src="diagrams/DeletePolicySequenceDiagram.puml" width="650" />

As seen above, the `execute` method retrieves the `Policy` from the client at the specified index and deletes it from the corresponding `Client` object. If the index is invalid, an error is thrown.

### Priority feature

#### Priority Tag

The priority command is built around the `PriorityTag` class, which extends `Tag`.
The PriorityTag constructor does not accept arguments, ensuring that its tagName
is always set to "Priority."

The Class Diagram of a Client can be [seen in the diagram above](#model-component).

#### Implementation

The `PriorityCommand` toggles a client’s priority status. The following sequence diagrams
illustrate how this process flows through the logic component:

<puml src="diagrams/PrioritySequenceDiagram-Logic.puml" width="650" />

The following sequence diagrams illustrate how the `PriorityCommand` class interacts with
model components to toggle the priority of the user

<puml src="diagrams/PrioritySequenceDiagram-Client.puml" width="600" />

As seen above, the `togglePriority` method creates a new `Client` object
with updated priority. If the `Client` does not have a `PriorityTag`,
`togglePriority` will create a new `Client` with a `PriorityTag` object.
Else, it will filter the `PriorityTag` out of `tags`.
Its operation is modeled below:

<puml src="diagrams/TogglePrioritySequenceDiagram1.puml" width="750" />

<puml src="diagrams/TogglePrioritySequenceDiagram2.puml" width="550" />

### Find-Any Find-All

#### Implementation

As the `findany` and `findall` command performs very similarly. We thought that it made 
the most sense for `FindClientOrCommand` and `FindClientAndClient` to be sibling classes 
(in this case extending from `AbstractFindClientCommand`). The main difference between
the two is that they use different predicates `ContainsAnyKeywordsPredicate` and `ContainsAnyKeywordsPredicate`
which inherits from `AbstractContainsKeywordsPredicate`
Below is a class diagram to demonstrate the relationship of the 3 classes.

<puml src="diagrams/FindCommandsClassDiagram.puml" width="550" />

The following sequence diagram illustrates how the `AbstractFindClientCommand` and hence the
`FindClientOrCommand` and `FindClientAndCommand` class interacts with  
model components to get a filtered list from WealthVault:

<puml src="diagrams/AbstractFindClientCommandSequenceDiagram.puml" width="750" />

### Update Client feature

The `update` command allows users to modify only the contact information (phone, email, address) of a client, without changing their name or tags.

#### Implementation

The `update` command updates a client's contact information at a given index. Unlike the `edit` command which allows changing all client fields, the `update` command is specifically designed to only modify contact information fields (phone, email, address), preserving the client's name and tags.

The `UpdateClientCommand` extends `Command` directly, rather than extending `EditCommand`, to enforce these restrictions. However, it still reuses the `EditClientDescriptor` class to store field values.

The restrictions are implemented in two key places:

1. In the `UpdateClientCommandParser`:

   ```java
   // Only tokenize phone, email, and address prefixes
   ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args,
           PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS);
   ```

2. In the `createUpdatedClient` method of `UpdateClientCommand`:
   ```java
   return new Client(
           clientToUpdate.getName(),      // Preserve original name
           updatedPhone,
           updatedEmail,
           updatedAddress,
           clientToUpdate.getTags()       // Preserve original tags
   );
   ```

This design allows for a more specialized update command that can be used when users only need to update contact information without risking accidental changes to a client's identity (name) or categorization (tags).

### Sort Priority feature

#### Implementation

The sort priority command allows users to sort clients by their priority status. The following sequence diagram
illustrates how this process flows through the logic component:

<puml src="diagrams/SortPrioritySequenceDiagram.puml" width="650" />

The `SortPriorityCommand` extends the abstract `Command` class and works with the `Model` component to sort clients.
When executed, it calls `Model#sortClientsByPriority()` which internally uses the `UniqueClientList` to sort clients
based on their priority status.

The sorting is implemented in the `UniqueClientList` class, where clients with priority (those with a `PriorityTag`)
are placed before non-priority clients. This is achieved through a comparator that sorts based on the client's
`getPriority()` value.

The command returns a `CommandResult` with a success message once the sorting is complete, and the UI automatically
updates to show the new sorted order of clients.

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

**Note:** WealthVault will be referred to generically as Address Book in this section. 

The proposed undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

- `VersionedAddressBook#commit()` — Saves the current address book state in its history.
- `VersionedAddressBook#undo()` — Restores the previous address book state from its history.
- `VersionedAddressBook#redo()` — Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

<puml src="diagrams/UndoRedoState0.puml" alt="UndoRedoState0" />

Step 2. The user executes `delc 5` command to delete the 5th client in WealthVault. The `delc` command calls `Model#commitAddressBook()`, causing the modified state of the address book after the `delc 5` command executes to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

<puml src="diagrams/UndoRedoState1.puml" alt="UndoRedoState1" />

Step 3. The user executes `addc n/David …​` to add a new client. The `addc` command also calls `Model#commitAddressBook()`, causing another modified address book state to be saved into the `addressBookStateList`.

<puml src="diagrams/UndoRedoState2.puml" alt="UndoRedoState2" />

<box type="info" seamless>

**Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the address book state will not be saved into the `addressBookStateList`.

</box>

Step 4. The user now decides that adding the client was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

<puml src="diagrams/UndoRedoState3.puml" alt="UndoRedoState3" />

<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</box>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

<puml src="diagrams/UndoSequenceDiagram-Logic.puml" alt="UndoSequenceDiagram-Logic" />

<box type="info" seamless>

**Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</box>

Similarly, how an undo operation goes through the `Model` component is shown below:

<puml src="diagrams/UndoSequenceDiagram-Model.puml" alt="UndoSequenceDiagram-Model" />

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the address book to that state.

<box type="info" seamless>

**Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest address book state, then there are no undone AddressBook states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</box>

Step 5. The user then decides to execute the command `list`. Commands that do not modify Address Book, such as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

<puml src="diagrams/UndoRedoState4.puml" alt="UndoRedoState4" />

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all address book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `addc n/David …​` command. This is the behavior that most modern desktop applications follow.

<puml src="diagrams/UndoRedoState5.puml" alt="UndoRedoState5" />

The following activity diagram summarizes what happens when a user executes a new command:

<puml src="diagrams/CommitActivityDiagram.puml" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

- **Alternative 1 (current choice):** Saves the entire address book.

  - Pros: Easy to implement.
  - Cons: May have performance issues in terms of memory usage.

- **Alternative 2:** Individual command knows how to undo/redo by
  itself.
  - Pros: Will use less memory (e.g. for `delc`, just save the client being deleted).
  - Cons: We must ensure that the implementation of each individual command are correct.

_{more aspects and alternatives to be added}_

### \[Proposed\] Data archiving

_{Explain here how the data archiving feature will be implemented}_

---

## **Documentation, logging, testing, configuration, dev-ops**

- [Documentation guide](Documentation.md)
- [Testing guide](Testing.md)
- [Logging guide](Logging.md)
- [Configuration guide](Configuration.md)
- [DevOps guide](DevOps.md)

---

## **Appendix: Requirements**

### Product scope

**Target user profile**: Financial advisors

- Managing a significant number of client contacts
- Requires a one-stop platform to track and update client's financial details
- Experiences difficulties to keep track of client's financial needs across multiple platforms
- Easy-to-use, fast and organized interface to maintain efficiency in their work
- Keen to provide fast and efficient service to customers

**Value proposition**: Manage clients and their financial needs faster than a typical mouse/GUI driven app

### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​           | I want to …​                                            | So that I can…​                                      |
| -------- | ----------------- |---------------------------------------------------------|------------------------------------------------------|
| `* * *`  | Financial Advisor | Add a new client with contact details                   | Keep track of my clients                             |
| `* * *`  | Financial Advisor | Read client’s contact details                           | View them when necessary                             |
| `* * *`  | Financial Advisor | Delete a client’s record                                | Remove outdated or incorrect information             |
| `* *`    | Financial Advisor | Edit a client’s contact details                         | Update them when necessary                           |
| `* *`    | Financial Advisor | Search for a client by name                             | Quickly find their details                           |
| `* *`    | Financial Advisor | Sort clients in alphabetical order                      | Quickly find their details                           |
| `* *`    | Financial Advisor | Mark client's priority status                           | Quickly identify and attend to high-priority clients |
| `* *`    | Financial Advisor | Sort client by priority status                          | Quickly identify and attend to high-priority clients |
| `* *`    | Financial Advisor | Filter clients by location                              | Easily find clients within a specific region         |
| `*`      | Financial Advisor | Store multiple contact numbers for a client             | Have alternative ways to reach them                  |
| `*`      | Financial Advisor | Store multiple addresses for a client                   | Keep track of their home and office locations        |
| `*`      | Financial Advisor | Categorize clients based on communication preferences   | Contact them in their preferred way                  |
| `*`      | Financial Advisor | Send an email to a client directly from the application | Communicate with them efficiently                    |
| `*`      | Financial Advisor | Initiate a phone call to a client from the application  | Reach them easily                                    |
| `*`      | Financial Advisor | Set reminders for following up with clients             | Not miss important meetings                          |
| `*`      | Financial Advisor | Receive notifications about upcoming meetings           | Prepare in advance                                   |
| `*`      | Financial Advisor | Schedule recurring reminders for periodic check-ins     | Maintain regular contact                             |

_{More to be added}_

### Use cases

(For all use cases below, the **System** is the `WealthVault` and the **Actor** is the `user`, unless specified otherwise)

### UC01 - Add Client

#### Main Success Scenario (MSS):

1. User chooses to add a new client.
2. WealthVault requests client details.
3. User enters the required client details.
4. WealthVault verifies the details.
5. WealthVault adds the client to WealthVault and confirms successful addition.
6. **Use case ends.**

#### Extensions:

- **3a. WealthVault detects an error in the entered details.**

  - 3a1. WealthVault requests for the correct data.
  - 3a2. User enters new data.
  - Steps 3a1-3a2 are repeated until the data entered are correct.
  - **Use case resumes from step 4.**

- **4a. WealthVault detects that the client already exists.**

  - 4a1. WealthVault notifies the user of the duplicate entry.
  - 4a2. User chooses to either modify details or cancel the operation.
  - If modifying, **use case resumes from step 3.**
  - If canceling, **use case ends.**

- **\*a. At any time, User chooses to cancel the process.**
  - \*a1. WealthVault requests to confirm the cancellation.
  - \*a2. User confirms the cancellation.
  - **Use case ends.**

### UC02 - Delete Client

#### Main Success Scenario (MSS):

1. User chooses to delete a client.
2. WealthVault requests the client’s details for deletion.
3. User enters the required details.
4. User confirms the deletion by clicking "enter".
5. WealthVault deletes the client from WealthVault and confirms successful deletion. 
6. **Use case ends.**

#### Extensions:

- **3a. WealthVault detects an error in the entered details.**

  - 3a1. WealthVault requests for the correct data.
  - 3a2. User enters new data.
  - Steps 3a1-3a2 are repeated until the data entered are correct.
  - **Use case resumes from step 4.**

- **3b. The client does not exist in WealthVault.**

  - 3b1. WealthVault notifies the user that the client is not found.
  - 3b2. User chooses to either retry or cancel the operation.
  - If retrying, **use case resumes from step 2.**
  - If canceling, **use case ends.**

- **\*a. At any time, User chooses to cancel the process.**
  - \*a1. WealthVault requests to confirm the cancellation.
  - \*a2. User confirms the cancellation.
  - **Use case ends.**

_{More to be added}_

### Non-Functional Requirements

1.  Should work on any _mainstream OS_ as long as it has Java `17` or above installed.
2.  Should be able to hold up to 1000 clients without a noticeable sluggishness in performance for typical usage.
3.  A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.

_{More to be added}_

### Glossary

- **API (Application Programming Interface)**: A set of protocols and tools that allow different software components to communicate and interact with each other. In the context of WealthVault, it defines how various system components exchange data and functionality.

- **Client**: An individual who is a financial advisor’s contact and recipient of financial guidance or services.

- **Financial Advisor**: A primary user of the application, responsible for managing client financial data, providing investment advice, and overseeing financial planning.

- **Financial Needs**: A client’s specific financial goals or requirements, including investment planning, retirement savings, risk management, and debt reduction strategies.

- **GUI (Graphical User Interface)**: The visual interface of the application that enables users to interact with the system through graphical elements such as buttons, menus, and forms.

- **Mainstream OS**: Windows, Linux, Unix, MacOS

- **PlantUML**: A tool used for creating UML (Unified Modeling Language) diagrams, such as sequence diagrams and class diagrams, to document system design and architecture.

- **Private Contact Detail**: A contact detail that is not meant to be shared with others.

---

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<box type="info" seamless>

**Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more _exploratory_ testing.

</box>

### Launch and shutdown

1. Initial launch
   1. Download the jar file and copy into an empty folder
   2. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

2. Saving window preferences
   1. Resize the window to an optimum size. Move the window to a different location. Close the window.
   2. Re-launch the app by double-clicking the jar file.<br>
      Expected: The most recent window size and location is retained.

### Adding a client
1. Adding a client while all clients are being shown
2. Prerequisites: List all clients using the `list` command. Multiple clients in the list.
3. Test case: `addc n/David Ong p/93012489 e/davidong@example.com a/Block 111 Sengkang Central t/Life Policy`<br>
   Expected: New contact is added to the bottom of the list. Details of the added contact shown in the status message.
4. Test case: `addc 0`<br>
   Expected: No client is added. Error details shown in the status message.
5. Other incorrect addc commands to try: `addc`, `addc x`, `...`<br>
   Expected: Similar to previous.

### Adding a policy
1. Adding a policy while all clients are being shown
2. Prerequisites: List all clients using the `list` command. Multiple clients in the list.
3. Test case: `addp 1 t/Health Insurance`<br>
   Expected: "Health Insurance" policy is added to the first contact of the list. Details of the added policy shown in the status message.
4. Test case: `addp 0`<br>
   Expected: No policy is added. Error details shown in the status message.
5. Other incorrect addp commands to try: `addp`, `addp x`, `...` (where x is larger than the list size)<br>
   Expected: Similar to previous.

### Deleting a client
1. Deleting a client while all clients are being shown
2. Prerequisites: List all clients using the `list` command. Multiple clients in the list.
3. Test case: `delc 1`<br>
   Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message. 
4. Test case: `delc 0`<br>
   Expected: No client is deleted. Error details shown in the status message. 
5. Other incorrect delc commands to try: `delc`, `delc x`, `...` (where x is larger than the list size)<br>
   Expected: Similar to previous.

### Deleting multiple clients
1. Deleting multiple clients while all clients are being shown
2. Prerequisites: List all clients using the `list` command. Multiple clients in the list.
3. Test case: `deleteclientmult i/1 i/2`<br>
   Expected: First and second contact are deleted from the list. Details of the deleted contacts shown in the status message.
4. Test case: `deleteclientmult i/2`<br>
   Expected: No client is deleted. Error details shown in the status message.
5. Other incorrect deleteclientmult commands to try: `deleteclientmult`, `deleteclientmult i/1 i/x` (where x is larger than the list size)<br>
   Expected: Similar to previous.

### Deleting a policy
1. Deleting a policy while all clients are being shown
2. Prerequisites: List all clients using the `list` command. Multiple clients in the list.
3. Test case: `delp 1 t/Health Insurance`<br>
   Expected: "Health Insurance" policy is deleted from the first contact of the list. Details of the deleted policy shown in the status message.
4. Test case: `delp 0`<br>
   Expected: No policy is deleted. Error details shown in the status message.
5. Other incorrect delp commands to try: `delp`, `delp x`, `...` (where x is larger than the list size)<br>
       Expected: Similar to previous.

### Searching for Clients

#### Find Any
1. Find a client with any of the keywords matching the tags or names
2. Prerequisites: List all clients using the `list` command. Multiple clients in the list.
3. Test case: `findany Alex Priority`<br>
   Expected: Shows all clients with the name or tags matching either (or both) "Alex" or "Priority" (case-insensitve)
4. Test case: `findany`<br>
   Expected: No client shown. Error details shown in the status message.

#### Find All
1. Find a client with any of the keywords matching the tags or names
2. Prerequisites: List all clients using the `list` command. Multiple clients in the list.
3. Test case: `findany Alex friends`<br>
   Expected: Shows all clients with the name or tags matching both "Alex" or "friends" (case-insensitve)
4. Test case: `findall`<br>
   Expected: No client shown. Error details shown in the status message.

### Toggling Priority
1. Toggles the priority (as shown by the `Priority` tag) of a client while all clients are being shown
2. Prerequisite 1: Put the jar file in a new folder as the test below is for sample data.
3. Prerequisite 2: List all clients using the `list` command. Multiple clients in the list.
3. Test case: `priority 1`<br>
   Expected: First contact (Alex) has a Priority tag added to his card. A success message is shown in the status box.
4. Test case: `priority 1` (again)<br>
   Expected: First contact (Alex) has a Priority tag removed from his card. A success message is shown in the status box.
5. Some incorrect commands to try: `priority`, `priority x`, `...` (where x is larger than the list size)<br>
   Expected: Similar to `addc` and `delc`.

## **Planned Enhancements**
**Team Size:** 5

1. **Case-sensitivity of prefixes:** The current command format only allows prefixes in lowercase
   (e.g. `a/`, `e/`, `n/`, `p/`, `t/`, and not `A/`, `E/`, `N/`, `P/`, `T/`). We plan to consider allowing prefixes to be case-insensitive to make it more convenient for users (e.g.
   let `t/` and `T/` both be considered valid prefixes).


2. **Input validation for policy tags (part 1):** We currently impose restrictions on policy tags to disallow certain symbols (`/` etc).
   However, the restriction might have been excessive. For instance, the user might want to create a policy tag with the name `Policy A/B`,
   which is likely a valid policy name in practice but is disallowed in our current setup. We plan to review the list of symbols allowed,
   to strike a balance between disallowing invalid input and maintaining flexibility for user-defined inputs.


3. **Input validation for policy tags (part 2):** We currently capitalise the first character of every word in the policy tag (e.g. `addp 1 t/policy A` results in `Policy A` policy tag
   being added to the client at index 1, rather than `policy A`), in order to prevent duplicates (e.g. `policy A` and `Policy A` might be the same policy).
   However, this restriction may not always align with user expectations or preferences. For instance, the user might want to create a policy tag named `WholeLife Plan` rather than `Wholelife Plan`.
   We plan to revisit our approach to formatting of policy tag names, to strike a balance between disallowing invalid input and maintaining flexibility for user-defined inputs.


4. **Input validation for phone numbers:** We currently do not check for the validity of phone numbers based on the country code.
   For instance, `+65 12345` is considered despite it being necessary for Singapore phone numbers (country indicated by `+65`) to
   be 8 digits long. We plan to include such checks when validating user input for phone numbers.
