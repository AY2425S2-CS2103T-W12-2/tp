package seedu.address.model.client;

import java.util.List;

import seedu.address.commons.util.StringUtil;

/**
 * Tests that a {@code Client}'s {@code Name} matches any of the keywords given.
 */
public class NameContainsKeywordsPredicate extends AbstractContainsKeywordsPredicate {

    public NameContainsKeywordsPredicate(List<String> keywords) {
        super(keywords);
    }

    @Override
    public boolean test(Client client) {
        return keywords.stream()
                .anyMatch(keyword -> StringUtil.containsWordIgnoreCase(client.getName().fullName, keyword));
    }
}
