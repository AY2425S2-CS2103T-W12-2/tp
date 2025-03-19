package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.model.ReadOnlyWealthVault;

/**
 * Represents a storage for {@link seedu.address.model.WealthVault}.
 */
public interface WealthVaultStorage {

    /**
     * Returns the file path of the data file.
     */
    Path getWealthVaultFilePath();

    /**
     * Returns WealthVault data as a {@link ReadOnlyWealthVault}.
     * Returns {@code Optional.empty()} if storage file is not found.
     *
     * @throws DataLoadingException if loading the data from storage failed.
     */
    Optional<ReadOnlyWealthVault> readWealthVault() throws DataLoadingException;

    /**
     * @see #getWealthVaultFilePath()
     */
    Optional<ReadOnlyWealthVault> readWealthVault(Path filePath) throws DataLoadingException;

    /**
     * Saves the given {@link ReadOnlyWealthVault} to the storage.
     * @param wealthVault cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void saveWealthVault(ReadOnlyWealthVault wealthVault) throws IOException;

    /**
     * @see #saveWealthVault(ReadOnlyWealthVault)
     */
    void saveWealthVault(ReadOnlyWealthVault wealthVault, Path filePath) throws IOException;

}
