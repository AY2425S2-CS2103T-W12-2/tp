package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.client.Client;
import seedu.address.model.person.Person;

/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final WealthVault wealthVault;
    private final UserPrefs userPrefs;
    private final FilteredList<Person> filteredPersons;

    private final FilteredList<Client> filteredClients;

    /**
     * Initializes a ModelManager with the given WealthVault and userPrefs.
     */
    public ModelManager(ReadOnlyWealthVault wealthVault, ReadOnlyUserPrefs userPrefs) {
        requireAllNonNull(wealthVault, userPrefs);

        logger.fine("Initializing with address book: " + wealthVault + " and user prefs " + userPrefs);

        this.wealthVault = new WealthVault(wealthVault);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredPersons = new FilteredList<>(this.wealthVault.getPersonList());
        filteredClients = new FilteredList<>(this.wealthVault.getClientList());
    }

    public ModelManager() {
        this(new WealthVault(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getWealthVaultFilePath() {
        return userPrefs.getWealthVaultFilePath();
    }

    @Override
    public void setWealthVaultPath(Path wealthVaultFilePath) {
        requireNonNull(wealthVaultFilePath);
        userPrefs.setWealthVaultFilePath(wealthVaultFilePath);
    }

    //=========== WealthVault ================================================================================

    @Override
    public void setWealthVault(ReadOnlyWealthVault wealthVault) {
        this.wealthVault.resetData(wealthVault);
    }

    @Override
    public ReadOnlyWealthVault getWealthVault() {
        return wealthVault;
    }

    @Override
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return wealthVault.hasPerson(person);
    }

    @Override
    public boolean hasClient(Client client) {
        requireNonNull(client);
        return wealthVault.hasClient(client);
    }

    @Override
    public void deletePerson(Person target) {
        wealthVault.removePerson(target);
    }

    @Override
    public void deleteClient(Client target) {
        wealthVault.removeClient(target);
    }

    @Override
    public void addPerson(Person person) {
        wealthVault.addPerson(person);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    @Override
    public void addClient(Client client) {
        wealthVault.addClient(client);
        updateFilteredClientList(PREDICATE_SHOW_ALL_CLIENTS);
    }

    @Override
    public void setPerson(Person target, Person editedPerson) {
        requireAllNonNull(target, editedPerson);

        wealthVault.setPerson(target, editedPerson);
    }

    @Override
    public void setClient(Client target, Client editedClient) {
        requireAllNonNull(target, editedClient);

        wealthVault.setClient(target, editedClient);
    }

    //=========== Filtered Person List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the internal list of
     * {@code versionedWealthVault}
     */
    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return filteredPersons;
    }

    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the internal list of
     * {@code versionedWealthVault}
     */
    @Override
    public ObservableList<Client> getFilteredClientList() {
        return filteredClients;
    }

    @Override
    public void updateFilteredPersonList(Predicate<Person> predicate) {
        requireNonNull(predicate);
        filteredPersons.setPredicate(predicate);
    }

    @Override
    public void updateFilteredClientList(Predicate<Client> predicate) {
        requireNonNull(predicate);
        filteredClients.setPredicate(predicate);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ModelManager)) {
            return false;
        }

        ModelManager otherModelManager = (ModelManager) other;

        return wealthVault.equals(otherModelManager.wealthVault)
                && userPrefs.equals(otherModelManager.userPrefs)
                && filteredPersons.equals(otherModelManager.filteredPersons);
    }

}
