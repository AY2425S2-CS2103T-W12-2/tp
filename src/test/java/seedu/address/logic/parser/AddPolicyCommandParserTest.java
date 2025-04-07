package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_COMPULSORY_FIELD_MISSING;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.VALID_INDEX_NOT_PROVIDED;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_CLIENT;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_CLIENT;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.AddPolicyCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.Tag;

public class AddPolicyCommandParserTest {

    private AddPolicyCommandParser parser = new AddPolicyCommandParser();

    @Test
    public void parse_allFieldsPresent_success() throws ParseException {
        Set<Tag> policiesToAdd = new HashSet<>();
        policiesToAdd.add(new Tag("Health"));
        policiesToAdd.add(new Tag("Life"));

        assertParseSuccess(parser, "1 " + PREFIX_TAG + "Health " + PREFIX_TAG + "Life",
                new AddPolicyCommand(INDEX_FIRST_CLIENT, policiesToAdd));
    }

    @Test
    public void parse_multipleTags_success() throws ParseException {
        Set<Tag> policiesToAdd = new HashSet<>();
        policiesToAdd.add(new Tag("Health"));
        policiesToAdd.add(new Tag("Life"));
        policiesToAdd.add(new Tag("Travel"));

        assertParseSuccess(parser, "1 " + PREFIX_TAG + "Health " + PREFIX_TAG + "Life " + PREFIX_TAG + "Travel",
                new AddPolicyCommand(INDEX_FIRST_CLIENT, policiesToAdd));
    }

    @Test
    public void parse_missingPolicy_throwsParseException() {
        assertParseFailure(parser, "1",
                MESSAGE_COMPULSORY_FIELD_MISSING
                        + String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddPolicyCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidClientIndex_throwsParseException() {
        assertParseFailure(parser, "abc " + PREFIX_TAG + "Health",
                Messages.VALID_INDEX_NOT_PROVIDED
                        + String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddPolicyCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_nonEmptyPreamble_throwsParseException() {
        assertParseFailure(parser, "ExtraText 1 " + PREFIX_TAG + "Health",
                Messages.VALID_INDEX_NOT_PROVIDED
                        + String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddPolicyCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_emptyCommand_throwsParseException() {
        assertParseFailure(parser, "", VALID_INDEX_NOT_PROVIDED
                + String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddPolicyCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validClientWithSinglePolicy_success() throws ParseException {
        Set<Tag> policiesToAdd = new HashSet<>();
        policiesToAdd.add(new Tag("Health"));

        assertParseSuccess(parser, "2 " + PREFIX_TAG + "Health",
                new AddPolicyCommand(INDEX_SECOND_CLIENT, policiesToAdd));
    }

    @Test
    public void parse_invalidPolicyTag_throwsParseException() {
        assertParseFailure(parser, "1 " + PREFIX_TAG + "  ",
                AddPolicyCommandParser.INVALID_POLICY_PROVIDED
                        + String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddPolicyCommandParser.INVALID_POLICY_FEATURES)
                        + "\n"
                        + AddPolicyCommand.MESSAGE_USAGE);
    }
}
