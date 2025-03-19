package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.JsonUtil;
import seedu.address.model.WealthVault;
import seedu.address.testutil.TypicalPersons;

public class JsonSerializableWealthVaultTest {

    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonSerializableWealthVaultTest");
    private static final Path TYPICAL_PERSONS_FILE = TEST_DATA_FOLDER.resolve("typicalPersonsWealthVault.json");
    private static final Path INVALID_PERSON_FILE = TEST_DATA_FOLDER.resolve("invalidPersonWealthVault.json");
    private static final Path DUPLICATE_PERSON_FILE = TEST_DATA_FOLDER.resolve("duplicatePersonWealthVault.json");

    @Test
    public void toModelType_typicalPersonsFile_success() throws Exception {
        JsonSerializableWealthVault dataFromFile = JsonUtil.readJsonFile(TYPICAL_PERSONS_FILE,
                JsonSerializableWealthVault.class).get();
        WealthVault wealthVaultFromFile = dataFromFile.toModelType();
        WealthVault typicalPersonsWealthVault = TypicalPersons.getTypicalWealthVault();
        assertEquals(wealthVaultFromFile, typicalPersonsWealthVault);
    }

    @Test
    public void toModelType_invalidPersonFile_throwsIllegalValueException() throws Exception {
        JsonSerializableWealthVault dataFromFile = JsonUtil.readJsonFile(INVALID_PERSON_FILE,
                JsonSerializableWealthVault.class).get();
        assertThrows(IllegalValueException.class, dataFromFile::toModelType);
    }

    @Test
    public void toModelType_duplicatePersons_throwsIllegalValueException() throws Exception {
        JsonSerializableWealthVault dataFromFile = JsonUtil.readJsonFile(DUPLICATE_PERSON_FILE,
                JsonSerializableWealthVault.class).get();
        assertThrows(IllegalValueException.class, JsonSerializableWealthVault.MESSAGE_DUPLICATE_PERSON,
                dataFromFile::toModelType);
    }

}
