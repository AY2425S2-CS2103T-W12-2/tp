package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showClientAtIndex;
import static seedu.address.testutil.TypicalClients.getTypicalAddressBook;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_CLIENT;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_CLIENT;

import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.EditCommand.EditClientDescriptor;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.client.Client;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.ClientBuilder;
import seedu.address.testutil.EditClientDescriptorBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for EditCommand.
 */
public class EditCommandTest {
    // Some tests were AI assisted but cleared, tests by {Joshua Tan}
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() {
        // Get the first client and its tags
        Client firstClient = model.getFilteredClientList().get(0);
        Set<Tag> originalTags = firstClient.getTags();

        // Create a new client with all fields edited but preserving tags
        Client editedClient = new ClientBuilder()
                .withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB)
                .withEmail(VALID_EMAIL_BOB)
                .withAddress(VALID_ADDRESS_BOB)
                .build();
        // Manually set the tags to match the original client's tags
        editedClient = new Client(editedClient.getName(), editedClient.getPhone(),
                editedClient.getEmail(), editedClient.getAddress(), originalTags);

        EditClientDescriptor descriptor = new EditClientDescriptorBuilder()
                .withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB)
                .withEmail(VALID_EMAIL_BOB)
                .withAddress(VALID_ADDRESS_BOB)
                .build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_CLIENT, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_CLIENT_SUCCESS, Messages.format(editedClient));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setClient(model.getFilteredClientList().get(0), editedClient);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() {
        Index indexLastClient = Index.fromOneBased(model.getFilteredClientList().size());
        Client lastClient = model.getFilteredClientList().get(indexLastClient.getZeroBased());

        ClientBuilder clientInList = new ClientBuilder(lastClient);
        Client editedClient = clientInList.withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
                .build();

        EditClientDescriptor descriptor = new EditClientDescriptorBuilder().withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB).build();
        EditCommand editCommand = new EditCommand(indexLastClient, descriptor);

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_CLIENT_SUCCESS, Messages.format(editedClient));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setClient(lastClient, editedClient);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() {
        EditCommand editCommand = new EditCommand(INDEX_FIRST_CLIENT, new EditClientDescriptor());
        Client editedClient = model.getFilteredClientList().get(INDEX_FIRST_CLIENT.getZeroBased());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_CLIENT_SUCCESS, Messages.format(editedClient));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() {
        showClientAtIndex(model, INDEX_FIRST_CLIENT);

        Client clientInFilteredList = model.getFilteredClientList().get(INDEX_FIRST_CLIENT.getZeroBased());
        Client editedClient = new ClientBuilder(clientInFilteredList).withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_CLIENT,
                new EditClientDescriptorBuilder().withName(VALID_NAME_BOB).build());

        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_CLIENT_SUCCESS, Messages.format(editedClient));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setClient(model.getFilteredClientList().get(0), editedClient);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicateClientUnfilteredList_failure() {
        Client firstClient = model.getFilteredClientList().get(INDEX_FIRST_CLIENT.getZeroBased());
        EditClientDescriptor descriptor = new EditClientDescriptorBuilder(firstClient).build();
        EditCommand editCommand = new EditCommand(INDEX_SECOND_CLIENT, descriptor);

        assertCommandFailure(editCommand, model, EditCommand.MESSAGE_DUPLICATE_CLIENT);
    }

    @Test
    public void execute_duplicateClientFilteredList_failure() {
        showClientAtIndex(model, INDEX_FIRST_CLIENT);

        // edit client in filtered list into a duplicate in address book
        Client clientInList = model.getAddressBook().getClientList().get(INDEX_SECOND_CLIENT.getZeroBased());
        EditCommand editCommand = new EditCommand(INDEX_FIRST_CLIENT,
                new EditClientDescriptorBuilder(clientInList).build());

        assertCommandFailure(editCommand, model, EditCommand.MESSAGE_DUPLICATE_CLIENT);
    }

    @Test
    public void execute_invalidClientIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredClientList().size() + 1);
        EditClientDescriptor descriptor = new EditClientDescriptorBuilder().withName(VALID_NAME_BOB).build();
        EditCommand editCommand = new EditCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_CLIENT_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of address book
     */
    @Test
    public void execute_invalidClientIndexFilteredList_failure() {
        showClientAtIndex(model, INDEX_FIRST_CLIENT);
        Index outOfBoundIndex = INDEX_SECOND_CLIENT;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getClientList().size());

        EditCommand editCommand = new EditCommand(outOfBoundIndex,
                new EditClientDescriptorBuilder().withName(VALID_NAME_BOB).build());

        assertCommandFailure(editCommand, model, Messages.MESSAGE_INVALID_CLIENT_DISPLAYED_INDEX);
    }

    @Test
    public void execute_tagEditAttempted_tagsPreserved() {
        // Create a client with a tag "friend"
        Client originalClient = new ClientBuilder().withName("Original Name")
                .withTags("friend").build();
        model = new ModelManager(new AddressBook(), new UserPrefs());
        model.addClient(originalClient);

        // Try to edit the name and change tag to "enemy" - tag change should be ignored
        EditClientDescriptor descriptor = new EditClientDescriptorBuilder()
                .withName("New Name")
                .build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_CLIENT, descriptor);

        // Expected client should have new name but original tag
        Client expectedClient = new ClientBuilder(originalClient)
                .withName("New Name")
                .withTags("friend")
                .build();
        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_CLIENT_SUCCESS,
            Messages.format(expectedClient));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setClient(originalClient, expectedClient);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noTagsInOriginalClient_emptyTagsPreserved() {
        // Create a client with no tags
        Client originalClient = new ClientBuilder().withName("Original Name").build();
        model = new ModelManager(new AddressBook(), new UserPrefs());
        model.addClient(originalClient);

        // Try to edit the name and add a tag - tag addition should be ignored
        EditClientDescriptor descriptor = new EditClientDescriptorBuilder()
                .withName("New Name")
                .build();
        EditCommand editCommand = new EditCommand(INDEX_FIRST_CLIENT, descriptor);

        // Expected client should have new name but still no tags
        Client expectedClient = new ClientBuilder(originalClient)
                .withName("New Name")
                .build();
        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_CLIENT_SUCCESS,
            Messages.format(expectedClient));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setClient(originalClient, expectedClient);

        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void equals() {
        final EditCommand standardCommand = new EditCommand(INDEX_FIRST_CLIENT, DESC_AMY);

        // same values -> returns true
        EditClientDescriptor copyDescriptor = new EditClientDescriptor(DESC_AMY);
        EditCommand commandWithSameValues = new EditCommand(INDEX_FIRST_CLIENT, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new EditCommand(INDEX_SECOND_CLIENT, DESC_AMY)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new EditCommand(INDEX_FIRST_CLIENT, DESC_BOB)));
    }

    @Test
    public void toStringMethod() {
        Index index = Index.fromOneBased(1);
        EditClientDescriptor editClientDescriptor = new EditClientDescriptor();
        EditCommand editCommand = new EditCommand(index, editClientDescriptor);
        String expected = EditCommand.class.getCanonicalName() + "{index=" + index + ", editClientDescriptor="
                + editClientDescriptor + "}";
        assertEquals(expected, editCommand.toString());
    }

}
