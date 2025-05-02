package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeleteClientMultCommand;

//AI-Generated Comment, by {FooNicholas}

/**
 * As we are only doing white-box testing, our test cases do not cover path variations
 * outside of the DeleteClientMultCommand code. For example, inputs "i/1" and "i/1 abc" take the
 * same path through the DeleteClientMultCommand, and therefore we test only one of them.
 * The path variation for those two cases occur inside the ParserUtil, and
 * therefore should be covered by the ParserUtilTest.
 */
public class DeleteClientMultCommandParserTest {

    private DeleteClientMultCommandParser parser = new DeleteClientMultCommandParser();

    @Test
    public void parse_validArgs_returnsDeleteClientMultCommand() {
        List<Index> indices = List.of(Index.fromOneBased(1), Index.fromOneBased(2));
        assertParseSuccess(parser, "i/1 i/2", new DeleteClientMultCommand(indices));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        // Empty input
        assertParseFailure(parser, "", String.format("Missing index parameters."
                        + MESSAGE_INVALID_COMMAND_FORMAT,
                DeleteClientMultCommand.MESSAGE_USAGE));

        // Single index
        assertParseFailure(parser, "i/1", DeleteClientMultCommand.MESSAGE_MINIMUM_INDICES);

        // Duplicate indices
        assertParseFailure(parser, "i/1 i/1", DeleteClientMultCommand.MESSAGE_DUPLICATE_INDICES);

        // Duplicate indices mixed with valid
        assertParseFailure(parser, "i/1 i/2 i/1", DeleteClientMultCommand.MESSAGE_DUPLICATE_INDICES);

        // Invalid index format
        assertParseFailure(parser, "1 2", DeleteClientMultCommand.MESSAGE_INVALID_FORMAT);

        // Invalid index value
        assertParseFailure(parser, "i/a i/2", DeleteClientMultCommand.MESSAGE_INVALID_FORMAT);

        // Zero index
        assertParseFailure(parser, "i/0 i/1", DeleteClientMultCommand.MESSAGE_INVALID_FORMAT);
    }
}
