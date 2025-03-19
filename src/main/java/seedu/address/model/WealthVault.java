package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.List;

import javafx.collections.ObservableList;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.client.Client;
import seedu.address.model.client.UniqueClientList;
import seedu.address.model.person.Person;
import seedu.address.model.person.UniquePersonList;

/**
 * Wraps all data at the address-book level
 * Duplicates are not allowed (by .isSamePerson comparison)
 */
public class WealthVault implements ReadOnlyWealthVault {

    private final UniquePersonList persons;
    private final UniqueClientList clients;
    /*
     * The 'unusual' code block below is a non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        persons = new UniquePersonList();
        clients = new UniqueClientList();
    }

    public WealthVault() {}

    /**
     * Creates an WealthVault using the Persons in the {@code toBeCopied}
     */
    public WealthVault(ReadOnlyWealthVault toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the person list with {@code persons}.
     * {@code persons} must not contain duplicate persons.
     */
    public void setPersons(List<Person> persons) {
        this.persons.setPersons(persons);
    }

    public void setClients(List<Client> clients) {
        this.clients.setClients(clients);
    }

    /**
     * Resets the existing data of this {@code WealthVault} with {@code newData}.
     */
    public void resetData(ReadOnlyWealthVault newData) {
        requireNonNull(newData);

        setPersons(newData.getPersonList());
        setClients(newData.getClientList());
    }

    //// person-level operations

    /**
     * Returns true if a person with the same identity as {@code person} exists in the address book.
     */
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return persons.contains(person);
    }

    /**
     * Adds a person to the address book.
     * The person must not already exist in the address book.
     */
    public void addPerson(Person p) {
        persons.add(p);
    }

    /**
     * Replaces the given person {@code target} in the list with {@code editedPerson}.
     * {@code target} must exist in the address book.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the address book.
     */
    public void setPerson(Person target, Person editedPerson) {
        requireNonNull(editedPerson);

        persons.setPerson(target, editedPerson);
    }

    /**
     * Removes {@code key} from this {@code WealthVault}.
     * {@code key} must exist in the address book.
     */
    public void removePerson(Person key) {
        persons.remove(key);
    }

    //// client-level operations
    /**
     * Checks if the address book contains a specific client.
     *
     * @param client The client to check for.
     * @return True if the client exists in the address book, false otherwise.
     * @throws NullPointerException if the provided client is null.
     */
    public boolean hasClient(Client client) {
        requireNonNull(client);
        return clients.contains(client);
    }

    /**
     * Adds a client to the address book.
     *
     * @param c The client to be added.
     */
    public void addClient(Client c) {
        clients.add(c);
    }

    /**
     * Replaces an existing client with an edited client.
     *
     * @param target The client to be replaced.
     * @param editedClient The new client to replace the existing one.
     * @throws NullPointerException if the edited client is null.
     */
    public void setClient(Client target, Client editedClient) {
        requireNonNull(editedClient);
        clients.setClient(target, editedClient);
    }

    /**
     * Removes a client from the address book.
     *
     * @param key The client to be removed.
     */
    public void removeClient(Client key) {
        clients.remove(key);
    }

    /**
     * Returns a string representation of the address book.
     *
     * @return A string containing details of persons and clients.
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("persons", persons)
                .add("clients", clients)
                .toString();
    }

    /**
     * Retrieves an unmodifiable list of persons in the address book.
     *
     * @return An observable list of persons.
     */
    @Override
    public ObservableList<Person> getPersonList() {
        return persons.asUnmodifiableObservableList();
    }

    /**
     * Retrieves an unmodifiable list of clients in the address book.
     *
     * @return An observable list of clients.
     */
    @Override
    public ObservableList<Client> getClientList() {
        return clients.asUnmodifiableObservableList();
    }

    /**
     * Compares this address book to another object for equality.
     *
     * @param other The object to compare with.
     * @return True if the other object is an WealthVault with the same data, false otherwise.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof WealthVault)) {
            return false;
        }

        WealthVault otherWealthVault = (WealthVault) other;
        return persons.equals(otherWealthVault.persons) && clients.equals(otherWealthVault.clients);
    }

    /**
     * Computes the hash code for the address book.
     *
     * @return The hash code based on persons and clients.
     */
    @Override
    public int hashCode() {
        return persons.hashCode();
    }
}
