package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.ReadOnlyWealthVault;
import seedu.address.model.UserPrefs;

/**
 * Manages storage of WealthVault data in local storage.
 */
public class StorageManager implements Storage {

    private static final Logger logger = LogsCenter.getLogger(StorageManager.class);
    private WealthVaultStorage wealthVaultStorage;
    private UserPrefsStorage userPrefsStorage;

    /**
     * Creates a {@code StorageManager} with the given {@code WealthVaultStorage} and {@code UserPrefStorage}.
     */
    public StorageManager(WealthVaultStorage wealthVaultStorage, UserPrefsStorage userPrefsStorage) {
        this.wealthVaultStorage = wealthVaultStorage;
        this.userPrefsStorage = userPrefsStorage;
    }

    // ================ UserPrefs methods ==============================

    @Override
    public Path getUserPrefsFilePath() {
        return userPrefsStorage.getUserPrefsFilePath();
    }

    @Override
    public Optional<UserPrefs> readUserPrefs() throws DataLoadingException {
        return userPrefsStorage.readUserPrefs();
    }

    @Override
    public void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException {
        userPrefsStorage.saveUserPrefs(userPrefs);
    }


    // ================ WealthVault methods ==============================

    @Override
    public Path getWealthVaultFilePath() {
        return wealthVaultStorage.getWealthVaultFilePath();
    }

    @Override
    public Optional<ReadOnlyWealthVault> readWealthVault() throws DataLoadingException {
        return readWealthVault(wealthVaultStorage.getWealthVaultFilePath());
    }

    @Override
    public Optional<ReadOnlyWealthVault> readWealthVault(Path filePath) throws DataLoadingException {
        logger.fine("Attempting to read data from file: " + filePath);
        return wealthVaultStorage.readWealthVault(filePath);
    }

    @Override
    public void saveWealthVault(ReadOnlyWealthVault wealthVault) throws IOException {
        saveWealthVault(wealthVault, wealthVaultStorage.getWealthVaultFilePath());
    }

    @Override
    public void saveWealthVault(ReadOnlyWealthVault wealthVault, Path filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        wealthVaultStorage.saveWealthVault(wealthVault, filePath);
    }

}
