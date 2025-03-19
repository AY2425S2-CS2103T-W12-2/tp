package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

/**
 * Marks a client as a priority
 */
public class PriorityCommand extends Command {

    public static final String COMMAND_WORD = "priority";

    public static final String MESSAGE_PRIORITY_PERSON_SUCCESS = "Toggle Priority of Person: %1$s";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Marks the person identified by the index number used in the displayed person list as a priority.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_ARGUMENTS = "Index: %1$d";

    private final Index index;

    private Set<Tag> tags;

    /**
     * @param index of the person in the filtered person list to mark as priority
     */
    public PriorityCommand(Index index) {
        requireNonNull(index);
        this.index = index;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        Person personToPrioritise = getPersonFromIndex(lastShownList, index);
        Person priorityPerson = togglePriorityTag(personToPrioritise);

        model.setPerson(personToPrioritise, priorityPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_PRIORITY_PERSON_SUCCESS, Messages.format(priorityPerson)));
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToEdit}
     * With a new tag called priority
     */
    private static Person togglePriorityTag(Person personToEdit) throws CommandException {
        assert personToEdit != null;

        // Creates a mutable set
        Set<Tag> tags = new HashSet<>(personToEdit.getTags());

        // Toggles priority
        if (!isPriority(tags)) {
            tags.add(new Tag("Priority"));
        } else {
            tags = tags.stream()
                    .filter(tag -> !tag.isEqualTo("Priority"))
                    .collect(Collectors.toSet());
        }

        return new Person(
                personToEdit.getName(),
                personToEdit.getPhone(),
                personToEdit.getEmail(),
                personToEdit.getAddress(),
                tags
        );
    }

    /**
     * Returns whether a tag with tagName "Priority" exists in a list of tags
     *
     * @param tags the list of tags
     */
    private static boolean isPriority(Set<Tag> tags) {
        return tags.stream().anyMatch(t -> t.isEqualTo("Priority"));
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
