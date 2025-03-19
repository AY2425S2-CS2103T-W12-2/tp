package seedu.address.storage;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.FileUtil;
import seedu.address.commons.util.JsonUtil;
import seedu.address.model.ReadOnlyWealthVault;

/**
 * A class to access WealthVault data stored as a json file on the hard disk.
 */
public class JsonWealthVaultStorage implements WealthVaultStorage {

    private static final Logger logger = LogsCenter.getLogger(JsonWealthVaultStorage.class);

    private Path filePath;

    public JsonWealthVaultStorage(Path filePath) {
        this.filePath = filePath;
    }

    public Path getWealthVaultFilePath() {
        return filePath;
    }

    @Override
    public Optional<ReadOnlyWealthVault> readWealthVault() throws DataLoadingException {
        return readWealthVault(filePath);
    }

    /**
     * Similar to {@link #readWealthVault()}.
     *
     * @param filePath location of the data. Cannot be null.
     * @throws DataLoadingException if loading the data from storage failed.
     */
    public Optional<ReadOnlyWealthVault> readWealthVault(Path filePath) throws DataLoadingException {
        requireNonNull(filePath);

        Optional<JsonSerializableWealthVault> jsonWealthVault = JsonUtil.readJsonFile(
                filePath, JsonSerializableWealthVault.class);
        if (!jsonWealthVault.isPresent()) {
            return Optional.empty();
        }

        try {
            return Optional.of(jsonWealthVault.get().toModelType());
        } catch (IllegalValueException ive) {
            logger.info("Illegal values found in " + filePath + ": " + ive.getMessage());
            throw new DataLoadingException(ive);
        }
    }

    @Override
    public void saveWealthVault(ReadOnlyWealthVault wealthVault) throws IOException {
        saveWealthVault(wealthVault, filePath);
    }

    /**
     * Similar to {@link #saveWealthVault(ReadOnlyWealthVault)}.
     *
     * @param filePath location of the data. Cannot be null.
     */
    public void saveWealthVault(ReadOnlyWealthVault wealthVault, Path filePath) throws IOException {
        requireNonNull(wealthVault);
        requireNonNull(filePath);

        FileUtil.createIfMissing(filePath);
        JsonUtil.saveJsonFile(new JsonSerializableWealthVault(wealthVault), filePath);
    }

}
