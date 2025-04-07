package seedu.address.model.client;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.AddClientCommand;

/**
 * Represents a Client's address in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidAddress(String)}
 */
public class Address {

    public static final String MESSAGE_CONSTRAINTS =
            "The address given is invalid!\n"
                    + String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    "The address given is either blank, purely whitespace or more than 150 characters long.\n\n")
                    + AddClientCommand.MESSAGE_USAGE;

    /*
     * The first character of the address must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String VALIDATION_REGEX = "\\s*\\S.*\\S?\\s*|\\S+";

    public final String value;

    /**
     * Constructs an {@code Address}.
     *
     * @param address A valid address.
     */
    public Address(String address) {
        requireNonNull(address);
        String normalizedAddress = normalizeAddress(address);
        checkArgument(isValidAddress(normalizedAddress), MESSAGE_CONSTRAINTS);
        value = normalizedAddress;
    }

    /**
     * Processes the address to modify it stylistically
     *
     * @param address The input address
     * @return The processed address
     */
    private String normalizeAddress(String address) {
        address = address.replaceAll("\\s+", " ").trim();

        String[] parts = address.trim().split(" ");
        StringBuilder result = new StringBuilder();
        for (String part : parts) {
            if (part.length() > 0) {
                if (part.length() == 1) {
                    result.append(part.toUpperCase()).append(" ");
                } else {
                    result.append(part.substring(0, 1).toUpperCase())
                            .append(part.substring(1).toLowerCase()).append(" ");
                }
            }
        }
        return result.toString().trim();
    }

    /**
     * Returns true if a given string is a valid address.
     */
    public static boolean isValidAddress(String test) {
        String addressWithoutWhitespace = test.replaceAll("\\s+", "");
        return test.matches(VALIDATION_REGEX) && addressWithoutWhitespace.length() <= 150;
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
        if (!(other instanceof Address)) {
            return false;
        }

        Address otherAddress = (Address) other;
        return value.equals(otherAddress.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}

