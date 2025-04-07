package seedu.address.model.tag;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class TagTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Tag(null));
    }

    @Test
    public void constructor_invalidTagName_throwsIllegalArgumentException() {
        String invalidTagName = "";
        assertThrows(IllegalArgumentException.class, () -> new Tag(invalidTagName));
    }

    @Test
    public void isValidTagName() {
        // null tag name
        assertThrows(NullPointerException.class, () -> Tag.isValidTagName(null));
    }

    @Test
    public void isValidTagName_validTags_returnsTrue() {
        assertTrue(Tag.isValidTagName("ValidTag123"));
        assertTrue(Tag.isValidTagName("Another_Valid-Tag!"));
        assertTrue(Tag.isValidTagName("12345"));
        assertTrue(Tag.isValidTagName("Tag with spaces"));
    }

    @Test
    public void isValidTagName_invalidTags_returnsFalse() {
        assertFalse(Tag.isValidTagName("")); // Empty string
        assertFalse(Tag.isValidTagName("   ")); // Only spaces
        assertFalse(Tag.isValidTagName("Invalid@Tag#")); // Contains invalid symbols
        assertFalse(Tag.isValidTagName("a".repeat(151))); // Exceeds max length
    }
}
