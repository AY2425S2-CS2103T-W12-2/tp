package seedu.address.testutil;

import seedu.address.model.WealthVault;
import seedu.address.model.person.Person;

/**
 * A utility class to help with building WealthVault objects.
 * Example usage: <br>
 *     {@code WealthVault ab = new WealthVaultBuilder().withPerson("John", "Doe").build();}
 */
public class WealthVaultBuilder {

    private WealthVault wealthVault;

    public WealthVaultBuilder() {
        wealthVault = new WealthVault();
    }

    public WealthVaultBuilder(WealthVault wealthVault) {
        this.wealthVault = wealthVault;
    }

    /**
     * Adds a new {@code Person} to the {@code WealthVault} that we are building.
     */
    public WealthVaultBuilder withPerson(Person person) {
        wealthVault.addPerson(person);
        return this;
    }

    public WealthVault build() {
        return wealthVault;
    }
}
