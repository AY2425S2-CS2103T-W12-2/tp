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
 * Deletes multiple clients identified using their displayed index from the address book.
 */
public class DeleteClientMultCommand extends Command {

    public static final String COMMAND_WORD = "deleteclientmult";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes multiple clients identified by their index numbers used in the displayed client list.\n"
            + "Parameters: i/INDEX i/INDEX [i/INDEX]... (must be at least 2 unique positive indices)\n"
            + "Example: " + COMMAND_WORD + " i/1 i/2 i/3";

    public static final String MESSAGE_DELETE_CLIENT_SUCCESS = "Deleted Clients: %1$s";
    public static final String MESSAGE_MINIMUM_INDICES =
            "DeleteClientMultCommand requires at least 2 indices to delete.";
    public static final String MESSAGE_DUPLICATE_INDICES = "Duplicate indices are not allowed.";
    public static final String MESSAGE_INVALID_FORMAT =
            "Invalid command format! Each index must be prefixed with 'i/' and must be a positive integer.";
    private final List<Index> targetIndices;

    public DeleteClientMultCommand(List<Index> targetIndices) {
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
        if (!(other instanceof DeleteClientMultCommand)) {
            return false;
        }

        DeleteClientMultCommand otherDeleteCommand = (DeleteClientMultCommand) other;
        return targetIndices.equals(otherDeleteCommand.targetIndices);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndices", targetIndices)
                .toString();
    }
}
