package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalClients.ALICE;
import static seedu.address.testutil.TypicalClients.BOB;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_CLIENT;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_CLIENT;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.EditCommand.EditClientDescriptor;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.client.Client;
import seedu.address.testutil.ClientBuilder;
import seedu.address.testutil.EditClientDescriptorBuilder;
import seedu.address.testutil.ModelStub;

/**
 * Contains unit tests for {@code UpdateClientCommand}.
 */
public class UpdateClientCommandTest {
    // Some tests were AI assisted but cleared, tests by {Joshua Tan}
    @Test
    public void constructor_nullDescriptor_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new UpdateClientCommand(INDEX_FIRST_CLIENT, null));
    }

    @Test
    public void constructor_nullIndex_throwsNullPointerException() {
        EditClientDescriptor descriptor = new EditClientDescriptorBuilder().withPhone("91234567").build();
        assertThrows(NullPointerException.class, () -> new UpdateClientCommand(null, descriptor));
    }

    @Test
    public void execute_clientAcceptedByModel_updateSuccessful() throws Exception {
        Client originalClient = new ClientBuilder().build();

        // Only update phone, email, and address, not name or tags
        EditClientDescriptor descriptor = new EditClientDescriptorBuilder()
                .withPhone("91234567")
                .withEmail("updated@example.com")
                .withAddress("Updated Address")
                .build();

        UpdateClientCommand updateCommand = new UpdateClientCommand(INDEX_FIRST_CLIENT, descriptor);
        ModelStubForUpdate modelStub = new ModelStubForUpdate(originalClient);

        CommandResult result = updateCommand.execute(modelStub);

        Client expectedClient = new ClientBuilder(originalClient)
                .withPhone("91234567")
                .withEmail("updated@example.com")
                .withAddress("Updated Address")
                .build();

        assertEquals(expectedClient, modelStub.clientUpdated);

        // Use the full format from Messages.format(client)
        StringBuilder expectedMessageBuilder = new StringBuilder();
        expectedMessageBuilder.append("Updated Client: ")
                .append(expectedClient.getName())
                .append("; Phone: ")
                .append(expectedClient.getPhone())
                .append("; Email: ")
                .append(expectedClient.getEmail())
                .append("; Address: ")
                .append(expectedClient.getAddress())
                .append("; Tags: ");
        expectedClient.getTags().forEach(expectedMessageBuilder::append);

        assertEquals(expectedMessageBuilder.toString(), result.getFeedbackToUser());
    }

    @Test
    public void execute_singleFieldUpdate_success() throws Exception {
        Client originalClient = new ClientBuilder()
                .withPhone("12345678")
                .withEmail("original@example.com")
                .withAddress("Original Address")
                .build();

        // Test update phone only
        EditClientDescriptor phoneDescriptor = new EditClientDescriptorBuilder()
                .withPhone("87654321")
                .build();
        UpdateClientCommand updatePhoneCommand = new UpdateClientCommand(INDEX_FIRST_CLIENT, phoneDescriptor);
        ModelStubForUpdate phoneModelStub = new ModelStubForUpdate(originalClient);
        updatePhoneCommand.execute(phoneModelStub);
        Client expectedPhoneClient = new ClientBuilder(originalClient).withPhone("87654321").build();
        assertEquals(expectedPhoneClient, phoneModelStub.clientUpdated);

        // Test update email only
        EditClientDescriptor emailDescriptor = new EditClientDescriptorBuilder()
                .withEmail("new@example.com")
                .build();
        UpdateClientCommand updateEmailCommand = new UpdateClientCommand(INDEX_FIRST_CLIENT, emailDescriptor);
        ModelStubForUpdate emailModelStub = new ModelStubForUpdate(originalClient);
        updateEmailCommand.execute(emailModelStub);
        Client expectedEmailClient = new ClientBuilder(originalClient).withEmail("new@example.com").build();
        assertEquals(expectedEmailClient, emailModelStub.clientUpdated);

        // Test update address only
        EditClientDescriptor addressDescriptor = new EditClientDescriptorBuilder()
                .withAddress("New Address")
                .build();
        UpdateClientCommand updateAddressCommand = new UpdateClientCommand(INDEX_FIRST_CLIENT, addressDescriptor);
        ModelStubForUpdate addressModelStub = new ModelStubForUpdate(originalClient);
        updateAddressCommand.execute(addressModelStub);
        Client expectedAddressClient = new ClientBuilder(originalClient).withAddress("New Address").build();
        assertEquals(expectedAddressClient, addressModelStub.clientUpdated);
    }

    @Test
    public void execute_invalidIndex_throwsCommandException() {
        EditClientDescriptor descriptor = new EditClientDescriptorBuilder().withPhone("91234567").build();
        UpdateClientCommand updateCommand = new UpdateClientCommand(INDEX_SECOND_CLIENT, descriptor);
        ModelStubForInvalidIndex modelStub = new ModelStubForInvalidIndex();

        assertThrows(CommandException.class,
            Messages.MESSAGE_INVALID_CLIENT_DISPLAYED_INDEX, () -> updateCommand.execute(modelStub));
    }

    @Test
    public void execute_noFieldsSpecified_throwsCommandException() {
        Client client = new ClientBuilder().build();
        EditClientDescriptor descriptor = new EditClientDescriptor();
        UpdateClientCommand updateCommand = new UpdateClientCommand(INDEX_FIRST_CLIENT, descriptor);
        ModelStubForUpdate modelStub = new ModelStubForUpdate(client);

        assertThrows(CommandException.class,
                UpdateClientCommand.MESSAGE_NOT_UPDATED, () -> updateCommand.execute(modelStub));
    }

    @Test
    public void execute_duplicateClient_throwsCommandException() {
        Client firstClient = new ClientBuilder().withPhone("12345678").build();
        Client secondClient = new ClientBuilder().withPhone("87654321").build();

        EditClientDescriptor descriptor = new EditClientDescriptorBuilder()
                .withPhone("87654321")
                .build();

        UpdateClientCommand updateCommand = new UpdateClientCommand(INDEX_FIRST_CLIENT, descriptor);

        ModelStubForDuplicate modelStub = new ModelStubForDuplicate(Arrays.asList(firstClient, secondClient));

        assertThrows(CommandException.class,
            UpdateClientCommand.MESSAGE_DUPLICATE_CLIENT, () -> updateCommand.execute(modelStub));
    }

    @Test
    public void equals() {
        // Create descriptors that only modify contact information
        EditClientDescriptor aliceDescriptor = new EditClientDescriptorBuilder()
                .withPhone(ALICE.getPhone().value)
                .withEmail(ALICE.getEmail().value)
                .withAddress(ALICE.getAddress().value)
                .build();

        EditClientDescriptor bobDescriptor = new EditClientDescriptorBuilder()
                .withPhone(BOB.getPhone().value)
                .withEmail(BOB.getEmail().value)
                .withAddress(BOB.getAddress().value)
                .build();

        UpdateClientCommand updateAliceCommand = new UpdateClientCommand(INDEX_FIRST_CLIENT, aliceDescriptor);
        UpdateClientCommand updateBobCommand = new UpdateClientCommand(INDEX_FIRST_CLIENT, bobDescriptor);

        // same object -> returns true
        assertTrue(updateAliceCommand.equals(updateAliceCommand));

        // same values -> returns true
        UpdateClientCommand updateAliceCommandCopy = new UpdateClientCommand(INDEX_FIRST_CLIENT, aliceDescriptor);
        assertTrue(updateAliceCommand.equals(updateAliceCommandCopy));

        // different types -> returns false
        assertFalse(updateAliceCommand.equals(1));

        // null -> returns false
        assertFalse(updateAliceCommand.equals(null));

        // different client -> returns false
        assertFalse(updateAliceCommand.equals(updateBobCommand));

        // different index -> returns false
        UpdateClientCommand updateAliceCommandDiffIndex =
                new UpdateClientCommand(INDEX_SECOND_CLIENT, aliceDescriptor);
        assertFalse(updateAliceCommand.equals(updateAliceCommandDiffIndex));
    }

    /**
     * A Model stub that correctly handles the update operation.
     */
    private static class ModelStubForUpdate extends ModelStub {
        private final Client originalClient;
        private Client clientUpdated;

        ModelStubForUpdate(Client client) {
            requireNonNull(client);
            this.originalClient = client;
        }

        @Override
        public boolean hasClient(Client client) {
            return false; // Always return false to avoid duplicate check failure
        }

        @Override
        public ObservableList<Client> getFilteredClientList() {
            return FXCollections.observableArrayList(originalClient);
        }

        @Override
        public void setClient(Client target, Client editedClient) {
            clientUpdated = editedClient;
        }

        @Override
        public void updateFilteredClientList(Predicate<Client> predicate) {
            // Do nothing - this needs to be overridden to prevent the default error
        }
    }

    /**
     * A Model stub for testing duplicate client scenarios.
     */
    private static class ModelStubForDuplicate extends ModelStub {
        private final List<Client> clients;

        ModelStubForDuplicate(List<Client> clients) {
            this.clients = new ArrayList<>(clients);
        }

        @Override
        public boolean hasClient(Client client) {
            // Return true for any client that isn't the first one
            // This will trigger the duplicate exception
            return clients.stream()
                    .skip(1) // Skip the first client
                    .anyMatch(c -> c.isSameClient(client));
        }

        @Override
        public ObservableList<Client> getFilteredClientList() {
            return FXCollections.observableArrayList(clients);
        }

        @Override
        public void updateFilteredClientList(Predicate<Client> predicate) {
            // Do nothing - this needs to be overridden to prevent the default error
        }
    }

    /**
     * A Model stub for testing invalid index scenarios.
     */
    private static class ModelStubForInvalidIndex extends ModelStub {
        @Override
        public ObservableList<Client> getFilteredClientList() {
            // Return a list with only one client, so INDEX_SECOND_CLIENT will be invalid
            return FXCollections.observableArrayList(new ClientBuilder().build());
        }
    }
}
