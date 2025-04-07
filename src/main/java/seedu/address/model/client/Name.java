package seedu.address.model.client;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.AddClientCommand;



/**
 * Represents a Client's name in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidName(String)}
 */
public class Name {

    public static final String MESSAGE_CONSTRAINTS =
            "The name given is invalid!\n"
            + String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    "The name given either contains invalid symbols, or is more than 150 characters long.\n\n")
            + AddClientCommand.MESSAGE_USAGE;

    public static final String VALIDATION_REGEX = "[\\p{L}\\p{N}]+([ '-/@]+[\\p{L}\\p{N}]+)*";

    public final String fullName;

    /**
     * Constructs a {@code Name}.
     *
     * @param name A valid name.
     */
    public Name(String name) {
        requireNonNull(name);
        String normalizedName = normalizeName(name);
        checkArgument(isValidName(normalizedName),
                MESSAGE_CONSTRAINTS);
        fullName = normalizedName;
    }

    /**
     * Returns true if a given string is a valid name.
     */
    public static boolean isValidName(String test) {
        String nameWithoutWhitespace = test.replaceAll("\\s+", "");
        return test.matches(VALIDATION_REGEX) && nameWithoutWhitespace.length() <= 150;
    }

    /**
     * Processes the name to deal with casing and whitespace
     *
     * @param name The input name
     * @return The processed name
     */
    private String normalizeName(String name) {
        name = name.replaceAll("([,.@])(?!\\s)", "$1 ");
        name = name.replaceAll("(?<!\\s)@", " @ ");
        name = name.replaceAll("\\s+", " ");

        String[] parts = name.trim().split(" ");
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

        name = result.toString().trim();
        name = name.replaceAll("(?i)s/o", "s/o");
        name = name.replaceAll("(?i)d/o", "d/o");
        name = name.replaceAll(" \\.", "\\.");
        name = name.replaceAll(" ,", ",");
        name = name.replaceAll("\\s+", " ");
        return name.trim();
    }

    @Override
    public String toString() {
        return fullName;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Name)) {
            return false;
        }

        Name otherName = (Name) other;
        return fullName.equals(otherName.fullName);
    }

    @Override
    public int hashCode() {
        return fullName.hashCode();
    }
}
