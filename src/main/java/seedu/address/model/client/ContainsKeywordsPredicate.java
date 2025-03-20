package seedu.address.model.client;

import java.util.List;

import seedu.address.commons.util.StringUtil;
/**
 * Tests that a {@code Client}'s {@code Name} or {@code Tag} matches any of the keywords given.
 */
public class ContainsKeywordsPredicate extends AbstractContainsKeywordsPredicate {

    public ContainsKeywordsPredicate(List<String> keywords) {
        super(keywords);
    }

    @Override
    public boolean test(Client client) {
        return keywords.stream()
                .anyMatch(keyword -> StringUtil.containsWordIgnoreCase(client.getName().fullName, keyword))
                || keywords.stream()
                .anyMatch(keyword -> client.getTags().stream()
                        .anyMatch(tag -> StringUtil.containsWordIgnoreCase(tag.tagName, keyword)));
    }
}
