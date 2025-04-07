package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.ArrayList;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeleteClientMultCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new DeleteClientMultCommand object
 */
public class DeleteClientMultCommandParser implements Parser<DeleteClientMultCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteClientMultCommand
     * and returns a DeleteClientMultCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeleteClientMultCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteClientMultCommand.MESSAGE_USAGE));
        }

        String[] indexStrings = trimmedArgs.split("\\s+");
        List<Index> indices = new ArrayList<>();

        for (String indexString : indexStrings) {
            if (!indexString.startsWith("i/")) {
                throw new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteClientMultCommand.MESSAGE_USAGE));
            }
            try {
                Index index = ParserUtil.parseIndex(indexString.substring(2));
                indices.add(index);
            } catch (ParseException pe) {
                throw new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteClientMultCommand.MESSAGE_USAGE), pe);
            }
        }

        return new DeleteClientMultCommand(indices);
    }
}
