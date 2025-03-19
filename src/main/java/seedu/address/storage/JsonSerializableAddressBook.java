package seedu.address.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.ReadOnlyWealthVault;
import seedu.address.model.WealthVault;
import seedu.address.model.client.Client;
import seedu.address.model.person.Person;

/**
 * An Immutable WealthVault that is serializable to JSON format.
 */
@JsonRootName(value = "WealthVault")
class JsonSerializableWealthVault {

    public static final String MESSAGE_DUPLICATE_PERSON = "Persons list contains duplicate person(s).";
    public static final String MESSAGE_DUPLICATE_CLIENT = "Client list contains duplicate client(s).";

    private final List<JsonAdaptedPerson> persons = new ArrayList<>();
    private final List<JsonAdaptedClient> clients = new ArrayList<>();

    /**
     * Constructs a {@code JsonSerializableWealthVault} with the given clients.
     */
    @JsonCreator
    public JsonSerializableWealthVault(@JsonProperty("persons") List<JsonAdaptedPerson> persons,
                                       @JsonProperty("clients") List<JsonAdaptedClient> clients) {
        this.persons.addAll(persons);
        this.clients.addAll(clients);
    }

    /**
     * Converts a given {@code ReadOnlyWealthVault} into this class for Jackson use.
     *
     * @param source future changes to this will not affect the created {@code JsonSerializableWealthVault}.
     */
    public JsonSerializableWealthVault(ReadOnlyWealthVault source) {
        persons.addAll(source.getPersonList().stream().map(JsonAdaptedPerson::new).collect(Collectors.toList()));
        clients.addAll(source.getClientList().stream().map(JsonAdaptedClient::new).collect(Collectors.toList()));
    }

    /**
     * Converts this address book into the model's {@code WealthVault} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public WealthVault toModelType() throws IllegalValueException {
        WealthVault wealthVault = new WealthVault();
        for (JsonAdaptedPerson jsonAdaptedPerson : persons) {
            Person person = jsonAdaptedPerson.toModelType();
            if (wealthVault.hasPerson(person)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_PERSON);
            }
            wealthVault.addPerson(person);
        }
        for (JsonAdaptedClient jsonAdaptedClient : clients) {
            Client client = jsonAdaptedClient.toModelType();
            if (wealthVault.hasClient(client)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_CLIENT);
            }
            wealthVault.addClient(client);
        }
        return wealthVault;
    }

}
