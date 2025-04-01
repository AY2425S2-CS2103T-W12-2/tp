package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.model.Model;

/**
 * Sorts all clients on the GUI in priority order for the user.
 */
public class SortPriorityCommand extends Command {
    public static final String COMMAND_WORD = "sortpriority";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Sorts all clients in WealthVault by priority.";
    public static final String MESSAGE_SORTED_SUCCESS = "Clients sorted in priority order.";

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.sortClientsByPriority();
        return new CommandResult(MESSAGE_SORTED_SUCCESS);
    }
}
