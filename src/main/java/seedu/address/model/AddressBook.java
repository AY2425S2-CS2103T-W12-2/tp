package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.List;

import javafx.collections.ObservableList;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.client.Client;
import seedu.address.model.client.UniqueClientList;

/**
 * Wraps all data at the address-book level
 * Duplicates are not allowed (by .isSameClient comparison)
 */
public class AddressBook implements ReadOnlyAddressBook {

    private final UniqueClientList clients;
    /*
     * The 'unusual' code block below is a non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        clients = new UniqueClientList();
    }

    public AddressBook() {}

    /**
     * Creates an AddressBook using the Clients in the {@code toBeCopied}
     */
    public AddressBook(ReadOnlyAddressBook toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the client list with {@code clients}.
     * {@code clients} must not contain duplicate clients.
     */
    public void setClients(List<Client> clients) {
        this.clients.setClients(clients);
    }

    /**
     * Resets the existing data of this {@code AddressBook} with {@code newData}.
     */
    public void resetData(ReadOnlyAddressBook newData) {
        requireNonNull(newData);

        setClients(newData.getClientList());
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

    @Override
    public void sortClients() {
        clients.sort();
    }

    public void sortClientsByPriority() {
        clients.sortByPriority();
    }

    /**
     * Returns a string representation of the address book.
     *
     * @return A string containing details of clients.
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("clients", clients)
                .toString();
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
     * @return True if the other object is an AddressBook with the same data, false otherwise.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddressBook)) {
            return false;
        }

        AddressBook otherAddressBook = (AddressBook) other;
        return clients.equals(otherAddressBook.clients) && clients.equals(otherAddressBook.clients);
    }

    /**
     * Computes the hash code for the address book.
     *
     * @return The hash code based on clients.
     */
    @Override
    public int hashCode() {
        return clients.hashCode();
    }
}
