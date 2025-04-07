package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_CLIENT;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_CLIENT;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_CLIENT;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeleteClientMultCommand;

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
        // Single index
        assertParseSuccess(parser, "i/1", new DeleteClientMultCommand(Arrays.asList(INDEX_FIRST_CLIENT)));

        // Multiple indices
        List<Index> indices = Arrays.asList(INDEX_FIRST_CLIENT, INDEX_SECOND_CLIENT, INDEX_THIRD_CLIENT);
        assertParseSuccess(parser, "i/1 i/2 i/3", new DeleteClientMultCommand(indices));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        // Empty argument
        assertParseFailure(parser, "",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteClientMultCommand.MESSAGE_USAGE));

        // Missing i/ prefix
        assertParseFailure(parser, "1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteClientMultCommand.MESSAGE_USAGE));

        // Invalid index
        assertParseFailure(parser, "i/a",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteClientMultCommand.MESSAGE_USAGE));

        // Invalid index in multiple indices
        assertParseFailure(parser, "i/1 i/a i/3",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteClientMultCommand.MESSAGE_USAGE));

        // Negative index
        assertParseFailure(parser, "i/-1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteClientMultCommand.MESSAGE_USAGE));

        // Zero index
        assertParseFailure(parser, "i/0",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteClientMultCommand.MESSAGE_USAGE));

        // Missing i/ prefix in multiple indices
        assertParseFailure(parser, "i/1 2 i/3",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteClientMultCommand.MESSAGE_USAGE));
    }
}
