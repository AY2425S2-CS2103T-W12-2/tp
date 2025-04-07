package seedu.address.model.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class PhoneTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Phone(null));
    }

    @Test
    public void constructor_invalidPhone_throwsIllegalArgumentException() {
        String invalidPhone = "";
        assertThrows(IllegalArgumentException.class, () -> new Phone(invalidPhone));
    }

    @Test
    public void isValidPhone() {
        // null phone number
        assertThrows(NullPointerException.class, () -> Phone.isValidPhone(null));

        // invalid phone numbers
        assertFalse(Phone.isValidPhone("")); // empty string
        assertFalse(Phone.isValidPhone(" ")); // spaces only
        assertFalse(Phone.isValidPhone("91")); // less than 3 numbers
        assertFalse(Phone.isValidPhone("phone")); // non-numeric
        assertFalse(Phone.isValidPhone("9011p041")); // alphabets within digits
        assertFalse(Phone.isValidPhone("42938420331212")); // more than 13 digits
        assertFalse(Phone.isValidPhone("+1234 1234567891234")); // too many digits after international code
        assertFalse(Phone.isValidPhone("+123456789")); // international code too long
        assertFalse(Phone.isValidPhone("+12 123456789012345")); // too many digits after international code
        assertFalse(Phone.isValidPhone("+123456789")); // International code too long.
        assertFalse(Phone.isValidPhone("+123456789")); // International code too long.
        assertFalse(Phone.isValidPhone("+1234 12345678901234")); // International code too long, number too long.
        assertFalse(Phone.isValidPhone("+1 a123456789")); // alpha in international code.
        assertFalse(Phone.isValidPhone("+12 a123456789")); // alpha in number.

        // valid phone numbers
        assertTrue(Phone.isValidPhone("911")); // exactly 3 numbers
        assertTrue(Phone.isValidPhone("93121534"));
        assertTrue(Phone.isValidPhone("429384203312")); // exactly 13 numbers
        assertTrue(Phone.isValidPhone("+1 1234567890"));
        assertTrue(Phone.isValidPhone("+12 1234567890"));
        assertTrue(Phone.isValidPhone("+123 1234567890"));
        assertTrue(Phone.isValidPhone("+65 12345678"));
        assertTrue(Phone.isValidPhone("12345678"));
        assertTrue(Phone.isValidPhone("-"));
    }

    @Test
    public void equals() {
        Phone phone = new Phone("999");
        Phone phoneOptional = new Phone("-");

        // same values -> returns true
        assertTrue(phone.equals(new Phone("999")));
        assertTrue(phoneOptional.equals(new Phone("-")));

        // same object -> returns true
        assertTrue(phone.equals(phone));
        assertTrue(phoneOptional.equals(phoneOptional));

        // null -> returns false
        assertFalse(phone.equals(null));
        assertFalse(phoneOptional.equals(null));

        // different types -> returns false
        assertFalse(phone.equals(5.0f));
        assertFalse(phoneOptional.equals(5.5f));

        // different values -> returns false
        assertFalse(phone.equals(new Phone("995")));
        assertFalse(phoneOptional.equals(new Phone("999")));
    }

    @Test
    public void toStringMethod() {
        Phone phone = new Phone("+1 1234567890");
        Phone phoneOptional = new Phone("-");

        assertEquals("+1 1234567890", phone.toString());
        assertEquals("-", phoneOptional.toString());
    }

    @Test
    public void processPhone_countryCodeOnly_throwsException() {
        // If we assume that a valid phone number should have digits after the country code,
        // this should be an invalid case and throw an exception.
        String input = "+65"; // No actual phone number part
        assertThrows(IllegalArgumentException.class, () -> new Phone(input));
    }
}
