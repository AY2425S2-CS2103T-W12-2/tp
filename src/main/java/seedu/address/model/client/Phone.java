package seedu.address.model.client;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.AddClientCommand;

/**
 * Represents a Client's phone number in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidPhone(String)}
 */
public class Phone {

    public static final String MESSAGE_CONSTRAINTS =
            "The phone number given is invalid!\n"
                    + String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    "Phone numbers should be of the format +[international code] [whitespace] [number].\n"
                            + "If no international code is provided, the phone number will start with +65.\n"
                            + "Do not include whitespace in the international code.\n\n"
                            + "The international code should be 1-3 digits.\n"
                            + "The number should be at most 13 digits.");

    public static final String VALIDATION_REGEX = "^(\\+?\\d{1,3} )?(\\d\\s?){3,13}$";
    public final String value;

    /**
     * Constructs a {@code Phone}.
     *
     * @param phone A valid phone number.
     */
    public Phone(String phone) {
        requireNonNull(phone);
        String processedPhone = processPhone(phone);
        checkArgument(isValidPhone(processedPhone), MESSAGE_CONSTRAINTS);
        value = processPhone(phone);
    }

    /**
     * Processes the phone number to ensure it has an international code.
     * If no international code is provided, it defaults to +65.
     *
     * @param phone The input phone number.
     * @return The processed phone number with an international code.
     */
    private String processPhone(String phone) {
        if (phone.equals("-")) {
            return phone;
        }

        phone = phone.trim().replaceAll("\\s+", " ");

        if (phone.startsWith("+")) {
            String[] parts = phone.split(" ", 2);
            if (parts.length < 2) {
                return phone;
            }

            String countryCode = parts[0];
            String numberPart = parts[1].replaceAll("\\s+", "");
            return countryCode + " " + numberPart;
        } else {
            String numberPart = phone.replaceAll("\\s+", "");
            return "+65 " + numberPart;
        }
    }

    /**
     * Returns true if a given string is a valid phone number.
     */
    public static boolean isValidPhone(String test) {
        return test.matches(VALIDATION_REGEX) || test.equals("-");
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Phone)) {
            return false;
        }

        Phone otherPhone = (Phone) other;
        return value.equals(otherPhone.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
