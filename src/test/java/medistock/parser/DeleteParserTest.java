package medistock.parser;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import medistock.command.Command;
import medistock.command.DeleteCommandName;
import medistock.command.DeleteCommandIndex;
import medistock.exception.MediStockException;
import org.junit.jupiter.api.Test;

public class DeleteParserTest {

    @Test
    void parseCommand_validDeleteName_returnsDeleteCommandName() throws MediStockException {
        String input = "delete n/Aspirin";

        Command command = Parser.parseCommand(input);
        assertInstanceOf(DeleteCommandName.class, command);
    }

    @Test
    void parseCommand_validDeleteIndex_returnsDeleteCommandIndex() throws MediStockException {
        String input = "delete i/1";

        Command command = Parser.parseCommand(input);
        assertInstanceOf(DeleteCommandIndex.class, command);
    }

    @Test
    void parseCommand_missingTag_throwsException() {
        String input = "delete no tag";

        assertThrows(MediStockException.class,
                () -> Parser.parseCommand(input));
    }

    @Test
    void parseCommand_emptyNameTag_throwsException() {
        String input = "delete n/";

        assertThrows(MediStockException.class,
                () -> Parser.parseCommand(input));
    }

    @Test
    void parseCommand_emptyIndexTag_throwsException() {
        String input = "delete i/";

        assertThrows(MediStockException.class,
                () -> Parser.parseCommand(input));
    }

    @Test
    void parseCommand_nonNumericIndex_throwsException() {
        String input = "delete i/abc";

        assertThrows(MediStockException.class,
                () -> Parser.parseCommand(input));
    }

    @Test
    void parseCommand_nonPositiveIndex_throwsException() {
        String input = "delete i/0";

        assertThrows(MediStockException.class,
                () -> Parser.parseCommand(input));
    }
}
