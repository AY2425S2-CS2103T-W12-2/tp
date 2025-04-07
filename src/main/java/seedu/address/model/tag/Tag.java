package seedu.address.model.tag;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.AddClientCommand;

/**
 * Represents a Tag in the address book.
 * Guarantees: immutable; name is valid as declared in {@link #isValidTagName(String)}
 */
public class Tag {

    public static final String MESSAGE_CONSTRAINTS =
            "At least 1 policy tag given is invalid or more than 150 characters long!\n"
                    + String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    AddClientCommand.MESSAGE_USAGE);
    public static final String VALIDATION_REGEX = "^[\\p{Alnum} .,'~*@%\\-_!?\\+\\*\\$\\[\\]()\"]+$";
    public final String tagName;

    /**
     * Constructs a {@code Tag}.
     *
     * @param tagName A valid tag name.
     */
    public Tag(String tagName) {
        requireNonNull(tagName);
        String normalizedTag = normalizeTag(tagName);
        checkArgument(isValidTagName(normalizedTag), MESSAGE_CONSTRAINTS);
        this.tagName = normalizedTag;
    }

    /**
     * Returns true if a given string is a valid tag name.
     */
    public static boolean isValidTagName(String test) {
        String tagWithoutWhitespace = test.replaceAll("\\s+", "");
        return tagWithoutWhitespace.matches(VALIDATION_REGEX) && tagWithoutWhitespace.length() <= 150;
    }


    /**
     * Processes the tag to modify it stylistically
     *
     * @param tag The input tag
     * @return The processed tag
     */
    private String normalizeTag(String tag) {
        tag = tag.replaceAll("\\s+", " ").trim();

        String[] parts = tag.trim().split(" ");
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

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Tag)) {
            return false;
        }

        Tag otherTag = (Tag) other;
        return tagName.equals(otherTag.tagName);
    }

    @Override
    public int hashCode() {
        return tagName.hashCode();
    }

    /**
     * Format state as text for viewing.
     */
    public String toString() {
        return '[' + tagName + ']';
    }

    public boolean isEqualTo(String string) {
        return tagName.equals(string);
    }
}

