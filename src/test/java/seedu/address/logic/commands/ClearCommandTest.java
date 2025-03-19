package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalWealthVault;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.WealthVault;

public class ClearCommandTest {

    @Test
    public void execute_emptyWealthVault_success() {
        Model model = new ModelManager();
        Model expectedModel = new ModelManager();

        assertCommandSuccess(new ClearCommand(), model, ClearCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_nonEmptyWealthVault_success() {
        Model model = new ModelManager(getTypicalWealthVault(), new UserPrefs());
        Model expectedModel = new ModelManager(getTypicalWealthVault(), new UserPrefs());
        expectedModel.setWealthVault(new WealthVault());

        assertCommandSuccess(new ClearCommand(), model, ClearCommand.MESSAGE_SUCCESS, expectedModel);
    }

}
