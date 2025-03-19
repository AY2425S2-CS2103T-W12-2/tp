package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_PRIORITY;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_FOURTH_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class PriorityCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    private void execute_togglePriorityUnfilteredList(Index personIndex, String... tags) {
        Person person = model.getFilteredPersonList().get(personIndex.getZeroBased());
        Person priorityPerson = new PersonBuilder(person).withTags(tags).build();

        PriorityCommand priorityCommand = new PriorityCommand(personIndex);

        String expectedMessage = String.format(PriorityCommand.MESSAGE_PRIORITY_PERSON_SUCCESS,
                Messages.format(priorityPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(person, priorityPerson);

        assertCommandSuccess(priorityCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_toggleOnPriorityUnfilteredList() {
        execute_togglePriorityUnfilteredList(INDEX_FIRST_PERSON, VALID_TAG_FRIEND, VALID_TAG_PRIORITY);
    }

    @Test
    public void execute_toggleOffPriorityUnfilteredList() {
        execute_togglePriorityUnfilteredList(INDEX_FOURTH_PERSON, VALID_TAG_FRIEND);
    }


    @Test
    public void execute_invalidIndex_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        PriorityCommand priorityCommand = new PriorityCommand(outOfBoundIndex);

        assertCommandFailure(priorityCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        final PriorityCommand standardCommand = new PriorityCommand(INDEX_FIRST_PERSON);

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new PriorityCommand(INDEX_SECOND_PERSON)));
    }
}
