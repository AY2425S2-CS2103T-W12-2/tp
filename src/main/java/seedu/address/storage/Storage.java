package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.ReadOnlyWealthVault;
import seedu.address.model.UserPrefs;

/**
 * API of the Storage component
 */
public interface Storage extends WealthVaultStorage, UserPrefsStorage {

    @Override
    Optional<UserPrefs> readUserPrefs() throws DataLoadingException;

    @Override
    void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException;

    @Override
    Path getWealthVaultFilePath();

    @Override
    Optional<ReadOnlyWealthVault> readWealthVault() throws DataLoadingException;

    @Override
    void saveWealthVault(ReadOnlyWealthVault wealthVault) throws IOException;

}
