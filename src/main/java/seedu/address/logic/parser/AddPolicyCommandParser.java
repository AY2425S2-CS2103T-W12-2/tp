package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_COMPULSORY_FIELD_MISSING;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Set;
import java.util.stream.Stream;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.AddPolicyCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new AddPolicyCommand object.
 */
public class AddPolicyCommandParser implements Parser<AddPolicyCommand> {

    public static final String INVALID_POLICY_PROVIDED =
            "At least 1 policy tag given is invalid! No policy tags added!\n";
    public static final String INVALID_POLICY_FEATURES =
            "The policy tag is blank, purely whitespace or more than 150 characters long.\n";


    /**
     * Parses the given {@code String} of arguments in the context of the AddPolicyCommand
     * and returns an AddPolicyCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddPolicyCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_TAG);

        if (!argMultimap.getPreamble().matches("[1-9]\\d*")) {
            throw new ParseException(Messages.VALID_INDEX_NOT_PROVIDED
                                     + String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddPolicyCommand.MESSAGE_USAGE));
        } else if (!arePrefixesPresent(argMultimap, PREFIX_TAG)) {
            throw new ParseException(MESSAGE_COMPULSORY_FIELD_MISSING
                    + String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddPolicyCommand.MESSAGE_USAGE));
        }

        try {
            // Parse the index of the client
            Index index = ParserUtil.parseIndex(argMultimap.getPreamble());

            // Parse policies
            Set<Tag> policies = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));

            // Return the AddPolicyCommand object
            return new AddPolicyCommand(index, policies);

        } catch (ParseException pe) {
            throw new ParseException(
                    INVALID_POLICY_PROVIDED
                    + String.format(MESSAGE_INVALID_COMMAND_FORMAT, INVALID_POLICY_FEATURES)
                    + "\n"
                    + AddPolicyCommand.MESSAGE_USAGE, pe);
        }
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}

