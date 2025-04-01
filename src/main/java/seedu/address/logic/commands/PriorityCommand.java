package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_CLIENTS;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.client.Client;
import seedu.address.model.tag.PriorityTag;
import seedu.address.model.tag.Tag;

/**
 * Marks a client as a priority
 */
public class PriorityCommand extends Command {

    public static final String COMMAND_WORD = "priority";

    public static final String MESSAGE_PRIORITY_CLIENT_SUCCESS = "Toggle Priority of the Client: %1$s";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Marks the client identified by the index number used in the displayed client list as priority.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_ARGUMENTS = "Index: %1$d";

    private final Index index;

    private Set<Tag> tags;

    /**
     * @param index of the client in the filtered client list to mark as priority
     */
    public PriorityCommand(Index index) {
        requireNonNull(index);
        this.index = index;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Client> lastShownList = model.getFilteredClientList();

        Client clientToPrioritise = getClientFromIndex(lastShownList, index);
        Client priorityClient = togglePriorityTag(clientToPrioritise);

        model.setClient(clientToPrioritise, priorityClient);
        model.updateFilteredClientList(PREDICATE_SHOW_ALL_CLIENTS);

        return new CommandResult(String.format(MESSAGE_PRIORITY_CLIENT_SUCCESS, Messages.format(priorityClient)));
    }

    /**
     * Creates and returns a {@code Client} with the details of {@code ClientToEdit}
     * With a new tag called priority
     */
    private static Client togglePriorityTag(Client clientToEdit) throws CommandException {
        assert clientToEdit != null;

        // Creates a mutable set
        Set<Tag> tags = new HashSet<>(clientToEdit.getTags());

        // Toggles priority
        if (!isPriority(tags)) {
            tags.add(new PriorityTag());
        } else {
            tags = tags.stream()
                    .filter(tag -> !(tag instanceof PriorityTag))
                    .collect(Collectors.toSet());
        }

        return new Client(
                clientToEdit.getName(),
                clientToEdit.getPhone(),
                clientToEdit.getEmail(),
                clientToEdit.getAddress(),
                tags,
                true
        );
    }

    /**
     * Returns whether a tag with tagName "Priority" exists in a list of tags
     *
     * @param tags the list of tags
     */
    private static boolean isPriority(Set<Tag> tags) {
        return tags.stream().anyMatch(t -> t instanceof PriorityTag);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof PriorityCommand)) {
            return false;
        }

        PriorityCommand e = (PriorityCommand) other;
        return index.equals(e.index);
    }
}

