package seedu.address.model.client;

import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Abstract predicate class that checks whether a {@code Client} satisfies a keyword condition.
 */
public abstract class AbstractContainsKeywordsPredicate implements Predicate<Client> {
    protected final List<String> keywords;

    public AbstractContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof AbstractContainsKeywordsPredicate)) {
            return false;
        }

        AbstractContainsKeywordsPredicate otherPredicate = (AbstractContainsKeywordsPredicate) other;
        return keywords.equals(otherPredicate.keywords);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("keywords", keywords).toString();
    }
}
