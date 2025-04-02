package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.client.Client;

/**
 * Deletes clients identified using their displayed indices from the address book.
 */
public class DeleteClientCommand extends Command {

    public static final String COMMAND_WORD = "deleteclient";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes existing clients identified by the user-inputted index numbers. "
            + "The index numbers must be based on the displayed client list.\n"
            + "Parameters: INDEX [INDEX]... (each INDEX must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1 2 3";

    public static final String MESSAGE_DELETE_CLIENT_SUCCESS = "Deleted Clients: %1$s";

    private final List<Index> targetIndices;

    public DeleteClientCommand(List<Index> targetIndices) {
        this.targetIndices = targetIndices;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Client> lastShownList = model.getFilteredClientList();
        List<Client> deletedClients = new ArrayList<>();

        // Check if all indices are valid
        for (Index index : targetIndices) {
            if (index.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_CLIENT_DISPLAYED_INDEX);
            }
        }

        // Delete clients in reverse order to maintain correct indices
        for (int i = targetIndices.size() - 1; i >= 0; i--) {
            Index index = targetIndices.get(i);
            Client clientToDelete = lastShownList.get(index.getZeroBased());
            model.deleteClient(clientToDelete);
            deletedClients.add(0, clientToDelete); // Add to beginning to maintain order
        }

        // Format the success message with all deleted clients
        StringBuilder deletedClientsMessage = new StringBuilder();
        for (int i = 0; i < deletedClients.size(); i++) {
            deletedClientsMessage.append(Messages.format(deletedClients.get(i)));
            if (i < deletedClients.size() - 1) {
                deletedClientsMessage.append(", ");
            }
        }

        return new CommandResult(String.format(MESSAGE_DELETE_CLIENT_SUCCESS, deletedClientsMessage.toString()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteClientCommand)) {
            return false;
        }

        DeleteClientCommand otherDeleteCommand = (DeleteClientCommand) other;
        return targetIndices.equals(otherDeleteCommand.targetIndices);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndices", targetIndices)
                .toString();
    }
}
