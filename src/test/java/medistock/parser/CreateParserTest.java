package medistock.parser;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import medistock.command.Command;
import medistock.command.CreateCommand;
import medistock.exception.MediStockException;
import org.junit.jupiter.api.Test;

public class CreateParserTest {

    @Test
    void parseCommand_validCreate_returnsCreateCommand() throws MediStockException {
        String input = "create n/Aspirin u/Tablets min/10";

        Command command = Parser.parseCommand(input);
        assertInstanceOf(CreateCommand.class, command);
    }

    @Test
    void parseCommand_missingNameTag_throwsException() {
        String input = "create u/Tablets min/10";

        assertThrows(MediStockException.class,
                () -> Parser.parseCommand(input));
    }

    @Test
    void parseCommand_missingUnitTag_throwsException() {
        String input = "create n/Aspirin min/10";

        assertThrows(MediStockException.class,
                () -> Parser.parseCommand(input));
    }

    @Test
    void parseCommand_missingMinTag_throwsException() {
        String input = "create n/Aspirin u/Tablets";

        assertThrows(MediStockException.class,
                () -> Parser.parseCommand(input));
    }

    @Test
    void parseCommand_wrongTagOrder_throwsException() {
        String input = "create u/Tablets n/Aspirin min/10";

        assertThrows(MediStockException.class,
                () -> Parser.parseCommand(input));
    }
}
