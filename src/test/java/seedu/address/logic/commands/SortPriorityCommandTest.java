package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalClients.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

/**
 * Contains integration tests (interaction with the Model) and unit tests for SortPriorityCommand.
 */
public class SortPriorityCommandTest {

    private Model model;
    private Model expectedModel;
    private SortPriorityCommand sortPriorityCommand;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        sortPriorityCommand = new SortPriorityCommand();
    }

    /**
     * Tests the execution of the SortPriorityCommand with all clients.
     */
    @Test
    public void execute_sort_allClients() {
        expectedModel.sortClientsByPriority();
        assertCommandSuccess(sortPriorityCommand, model, SortPriorityCommand.MESSAGE_SORTED_SUCCESS, expectedModel);
    }

}
