package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.HOON;
import static seedu.address.testutil.TypicalPersons.IDA;
import static seedu.address.testutil.TypicalPersons.getTypicalWealthVault;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.model.ReadOnlyWealthVault;
import seedu.address.model.WealthVault;

public class JsonWealthVaultStorageTest {
    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonWealthVaultStorageTest");

    @TempDir
    public Path testFolder;

    @Test
    public void readWealthVault_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> readWealthVault(null));
    }

    private java.util.Optional<ReadOnlyWealthVault> readWealthVault(String filePath) throws Exception {
        return new JsonWealthVaultStorage(Paths.get(filePath)).readWealthVault(addToTestDataPathIfNotNull(filePath));
    }

    private Path addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null
                ? TEST_DATA_FOLDER.resolve(prefsFileInTestDataFolder)
                : null;
    }

    @Test
    public void read_missingFile_emptyResult() throws Exception {
        assertFalse(readWealthVault("NonExistentFile.json").isPresent());
    }

    @Test
    public void read_notJsonFormat_exceptionThrown() {
        assertThrows(DataLoadingException.class, () -> readWealthVault("notJsonFormatWealthVault.json"));
    }

    @Test
    public void readWealthVault_invalidPersonWealthVault_throwDataLoadingException() {
        assertThrows(DataLoadingException.class, () -> readWealthVault("invalidPersonWealthVault.json"));
    }

    @Test
    public void readWealthVault_invalidAndValidPersonWealthVault_throwDataLoadingException() {
        assertThrows(DataLoadingException.class, () -> readWealthVault("invalidAndValidPersonWealthVault.json"));
    }

    @Test
    public void readAndSaveWealthVault_allInOrder_success() throws Exception {
        Path filePath = testFolder.resolve("TempWealthVault.json");
        WealthVault original = getTypicalWealthVault();
        JsonWealthVaultStorage jsonWealthVaultStorage = new JsonWealthVaultStorage(filePath);

        // Save in new file and read back
        jsonWealthVaultStorage.saveWealthVault(original, filePath);
        ReadOnlyWealthVault readBack = jsonWealthVaultStorage.readWealthVault(filePath).get();
        assertEquals(original, new WealthVault(readBack));

        // Modify data, overwrite exiting file, and read back
        original.addPerson(HOON);
        original.removePerson(ALICE);
        jsonWealthVaultStorage.saveWealthVault(original, filePath);
        readBack = jsonWealthVaultStorage.readWealthVault(filePath).get();
        assertEquals(original, new WealthVault(readBack));

        // Save and read without specifying file path
        original.addPerson(IDA);
        jsonWealthVaultStorage.saveWealthVault(original); // file path not specified
        readBack = jsonWealthVaultStorage.readWealthVault().get(); // file path not specified
        assertEquals(original, new WealthVault(readBack));

    }

    @Test
    public void saveWealthVault_nullWealthVault_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> saveWealthVault(null, "SomeFile.json"));
    }

    /**
     * Saves {@code WealthVault} at the specified {@code filePath}.
     */
    private void saveWealthVault(ReadOnlyWealthVault wealthVault, String filePath) {
        try {
            new JsonWealthVaultStorage(Paths.get(filePath))
                    .saveWealthVault(wealthVault, addToTestDataPathIfNotNull(filePath));
        } catch (IOException ioe) {
            throw new AssertionError("There should not be an error writing to the file.", ioe);
        }
    }

    @Test
    public void saveWealthVault_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> saveWealthVault(new WealthVault(), null));
    }
}
