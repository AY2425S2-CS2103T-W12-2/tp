package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_CLIENTS_LISTED_OVERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalClients.getTypicalAddressBook;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.client.ContainsAllKeywordsPredicate;

/**
 * Contains integration tests (interaction with the Model) for {@code FindClientAndCommand}.
 */
public class FindClientAndCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void equals() {
        ContainsAllKeywordsPredicate firstPredicate =
                new ContainsAllKeywordsPredicate(Collections.singletonList("first"));
        ContainsAllKeywordsPredicate secondPredicate =
                new ContainsAllKeywordsPredicate(Collections.singletonList("second"));

        FindClientAndCommand findFirstCommand = new FindClientAndCommand(firstPredicate);
        FindClientAndCommand findSecondCommand = new FindClientAndCommand(secondPredicate);

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> returns true
        FindClientAndCommand findFirstCommandCopy = new FindClientAndCommand(firstPredicate);
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
        ContainsAllKeywordsPredicate predicate = preparePredicate(" ");
        FindClientAndCommand command = new FindClientAndCommand(predicate);
        expectedModel.updateFilteredClientList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredClientList());
    }

    @Test
    public void execute_multipleKeywords_multipleClientsFound() {
        String expectedMessage = String.format(MESSAGE_CLIENTS_LISTED_OVERVIEW, 0);
        ContainsAllKeywordsPredicate predicate = preparePredicate("Kurz Elle Kunz");
        FindClientAndCommand command = new FindClientAndCommand(predicate);
        expectedModel.updateFilteredClientList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void toStringMethod() {
        ContainsAllKeywordsPredicate predicate = new ContainsAllKeywordsPredicate(Arrays.asList("keyword"));
        FindClientAndCommand findClientCommand = new FindClientAndCommand(predicate);
        String expected = FindClientAndCommand.class.getCanonicalName() + "{predicate=" + predicate + "}";
        assertEquals(expected, findClientCommand.toString());
    }

    /**
     * Parses {@code userInput} into a {@code NameContainsAllKeywordsPredicate}.
     */
    private ContainsAllKeywordsPredicate preparePredicate(String userInput) {
        return new ContainsAllKeywordsPredicate(Arrays.asList(userInput.split("\\s+")));
    }
}
