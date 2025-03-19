package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.getTypicalWealthVault;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.client.Client;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.testutil.PersonBuilder;

public class WealthVaultTest {

    private final WealthVault wealthVault = new WealthVault();

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), wealthVault.getPersonList());
    }

    @Test
    public void resetData_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> wealthVault.resetData(null));
    }

    @Test
    public void resetData_withValidReadOnlyWealthVault_replacesData() {
        WealthVault newData = getTypicalWealthVault();
        wealthVault.resetData(newData);
        assertEquals(newData, wealthVault);
    }

    @Test
    public void resetData_withDuplicatePersons_throwsDuplicatePersonException() {
        // Two persons with the same identity fields
        Person editedAlice = new PersonBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND)
                .build();
        List<Person> newPersons = Arrays.asList(ALICE, editedAlice);
        WealthVaultStub newData = new WealthVaultStub(newPersons);

        assertThrows(DuplicatePersonException.class, () -> wealthVault.resetData(newData));
    }

    @Test
    public void hasPerson_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> wealthVault.hasPerson(null));
    }

    @Test
    public void hasPerson_personNotInWealthVault_returnsFalse() {
        assertFalse(wealthVault.hasPerson(ALICE));
    }

    @Test
    public void hasPerson_personInWealthVault_returnsTrue() {
        wealthVault.addPerson(ALICE);
        assertTrue(wealthVault.hasPerson(ALICE));
    }

    @Test
    public void hasPerson_personWithSameIdentityFieldsInWealthVault_returnsTrue() {
        wealthVault.addPerson(ALICE);
        Person editedAlice = new PersonBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND)
                .build();
        assertTrue(wealthVault.hasPerson(editedAlice));
    }

    @Test
    public void getPersonList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> wealthVault.getPersonList().remove(0));
    }

    @Test
    public void toStringMethod() {
        String expected = WealthVault.class.getCanonicalName() + "{persons=" + wealthVault.getPersonList()
                + ", clients=" + wealthVault.getClientList() + "}";
        assertEquals(expected, wealthVault.toString());
    }

    /**
     * A stub ReadOnlyWealthVault whose persons list can violate interface constraints.
     */
    private static class WealthVaultStub implements ReadOnlyWealthVault {
        private final ObservableList<Person> persons = FXCollections.observableArrayList();
        private final ObservableList<Client> clients = FXCollections.observableArrayList();

        WealthVaultStub(Collection<Person> persons) {
            this.persons.setAll(persons);
        }

        @Override
        public ObservableList<Person> getPersonList() {
            return persons;
        }

        @Override
        public ObservableList<Client> getClientList() {
            return clients;
        }
    }

}
