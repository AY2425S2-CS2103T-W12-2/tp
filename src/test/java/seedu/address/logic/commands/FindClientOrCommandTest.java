package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_CLIENTS_LISTED_OVERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalClients.CARL;
import static seedu.address.testutil.TypicalClients.ELLE;
import static seedu.address.testutil.TypicalClients.FIONA;
import static seedu.address.testutil.TypicalClients.getTypicalAddressBook;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.client.ContainsKeywordsPredicate;

/**
 * Contains integration tests (interaction with the Model) for {@code FindClientOrCommand}.
 */
public class FindClientOrCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void equals() {
        ContainsKeywordsPredicate firstPredicate =
                new ContainsKeywordsPredicate(Collections.singletonList("first"));
        ContainsKeywordsPredicate secondPredicate =
                new ContainsKeywordsPredicate(Collections.singletonList("second"));

        FindClientOrCommand findFirstCommand = new FindClientOrCommand(firstPredicate);
        FindClientOrCommand findSecondCommand = new FindClientOrCommand(secondPredicate);

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> returns true
        FindClientOrCommand findFirstCommandCopy = new FindClientOrCommand(firstPredicate);
        assertTrue(findFirstCommand.equals(findFirstCommandCopy));

        // different types -> returns false
        assertFalse(findFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findFirstCommand.equals(null));

        // different client -> returns false
        assertFalse(findFirstCommand.equals(findSecondCommand));
    }

    @Test
    public void execute_zeroKeywords_noClientFound() {
        String expectedMessage = String.format(MESSAGE_CLIENTS_LISTED_OVERVIEW, 0);
        ContainsKeywordsPredicate predicate = preparePredicate(" ");
        FindClientOrCommand command = new FindClientOrCommand(predicate);
        expectedModel.updateFilteredClientList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredClientList());
    }

    @Test
    public void execute_multipleKeywords_multipleClientsFound() {
        String expectedMessage = String.format(MESSAGE_CLIENTS_LISTED_OVERVIEW, 3);
        ContainsKeywordsPredicate predicate = preparePredicate("Kurz Elle Kunz");
        FindClientOrCommand command = new FindClientOrCommand(predicate);
        expectedModel.updateFilteredClientList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(CARL, ELLE, FIONA), model.getFilteredClientList());
    }

    @Test
    public void toStringMethod() {
        ContainsKeywordsPredicate predicate = new ContainsKeywordsPredicate(Arrays.asList("keyword"));
        FindClientOrCommand findClientCommand = new FindClientOrCommand(predicate);
        String expected = FindClientOrCommand.class.getCanonicalName() + "{predicate=" + predicate + "}";
        assertEquals(expected, findClientCommand.toString());
    }

    /**
     * Parses {@code userInput} into a {@code NameContainsKeywordsPredicate}.
     */
    private ContainsKeywordsPredicate preparePredicate(String userInput) {
        return new ContainsKeywordsPredicate(Arrays.asList(userInput.split("\\s+")));
    }
}
