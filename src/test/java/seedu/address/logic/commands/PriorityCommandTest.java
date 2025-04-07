package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_PRIORITY;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalClients.getTypicalAddressBook;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_CLIENT;
import static seedu.address.testutil.TypicalIndexes.INDEX_FOURTH_CLIENT;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_CLIENT;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.client.Client;
import seedu.address.testutil.ClientBuilder;

public class PriorityCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    private void execute_togglePriorityUnfilteredList(Index clientIndex, String... tags) {
        Client client = model.getFilteredClientList().get(clientIndex.getZeroBased());
        Client priorityClient = new ClientBuilder(client).withTags(tags).build();

        PriorityCommand priorityCommand = new PriorityCommand(List.of(clientIndex));

        String expectedMessage = String.format(PriorityCommand.MESSAGE_PRIORITY_CLIENT_SUCCESS,
                Messages.format(priorityClient));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setClient(client, priorityClient);

        assertCommandSuccess(priorityCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_toggleOnPriorityUnfilteredList() {
        execute_togglePriorityUnfilteredList(INDEX_FIRST_CLIENT, VALID_TAG_FRIEND, VALID_TAG_PRIORITY);
    }

    @Test
    public void execute_toggleOffPriorityUnfilteredList() {
        execute_togglePriorityUnfilteredList(INDEX_FOURTH_CLIENT, VALID_TAG_FRIEND);
    }


    @Test
    public void execute_invalidIndex_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredClientList().size() + 1);
        PriorityCommand priorityCommand = new PriorityCommand(List.of(outOfBoundIndex));

        assertCommandFailure(priorityCommand, model, Messages.MESSAGE_INVALID_CLIENT_DISPLAYED_INDEX
            + String.format(MESSAGE_INVALID_COMMAND_FORMAT, PriorityCommand.MESSAGE_USAGE));
    }

    @Test
    public void equals() {
        final PriorityCommand standardCommand = new PriorityCommand(List.of(INDEX_FIRST_CLIENT));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new PriorityCommand(List.of(INDEX_SECOND_CLIENT))));
    }
}
