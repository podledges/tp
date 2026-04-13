package medistock.parser;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import medistock.command.Command;
import medistock.command.EditCommand;
import medistock.exception.MediStockException;
import org.junit.jupiter.api.Test;

public class EditParserTest {

    @Test
    void parseCommand_validEditNameOnly_returnsEditCommand() throws MediStockException {
        String input = "edit o/Aspirin n/Aspirin 500mg";

        Command command = Parser.parseCommand(input);
        assertInstanceOf(EditCommand.class, command);
    }

    @Test
    void parseCommand_validEditUnitOnly_returnsEditCommand() throws MediStockException {
        String input = "edit o/Aspirin u/Capsules";

        Command command = Parser.parseCommand(input);
        assertInstanceOf(EditCommand.class, command);
    }

    @Test
    void parseCommand_validEditMinOnly_returnsEditCommand() throws MediStockException {
        String input = "edit o/Aspirin min/20";

        Command command = Parser.parseCommand(input);
        assertInstanceOf(EditCommand.class, command);
    }

    @Test
    void parseCommand_validEditMultipleFields_returnsEditCommand() throws MediStockException {
        String input = "edit o/Aspirin n/Aspirin 500mg u/Capsules min/20";

        Command command = Parser.parseCommand(input);
        assertInstanceOf(EditCommand.class, command);
    }

    @Test
    void parseCommand_missingOldNameTag_throwsException() {
        String input = "edit n/Aspirin 500mg";

        assertThrows(MediStockException.class,
                () -> Parser.parseCommand(input));
    }

    @Test
    void parseCommand_bareEdit_throwsInvalidEditFormat() {
        String input = "edit";

        MediStockException exception = assertThrows(MediStockException.class,
                () -> Parser.parseCommand(input));
        assertTrue(exception.getMessage().startsWith("Invalid edit format."));
    }

    @Test
    void parseCommand_noFieldsProvided_throwsException() {
        String input = "edit o/Aspirin";

        assertThrows(MediStockException.class,
                () -> Parser.parseCommand(input));
    }

    @Test
    void parseCommand_emptyOldName_throwsException() {
        String input = "edit o/ n/Aspirin 500mg";

        assertThrows(MediStockException.class,
                () -> Parser.parseCommand(input));
    }

    @Test
    void parseCommand_emptyNewName_throwsException() {
        String input = "edit o/Aspirin n/ u/Capsules";

        assertThrows(MediStockException.class,
                () -> Parser.parseCommand(input));
    }

    @Test
    void parseCommand_emptyNewUnit_throwsException() {
        String input = "edit o/Aspirin u/ min/20";

        assertThrows(MediStockException.class,
                () -> Parser.parseCommand(input));
    }

    @Test
    void parseCommand_emptyNewMin_throwsException() {
        String input = "edit o/Aspirin min/";

        assertThrows(MediStockException.class,
                () -> Parser.parseCommand(input));
    }

    @Test
    void parseCommand_nonNumericMin_throwsException() {
        String input = "edit o/Aspirin min/abc";

        assertThrows(MediStockException.class,
                () -> Parser.parseCommand(input));
    }

    @Test
    void parseCommand_nonPositiveMin_throwsException() {
        String input = "edit o/Aspirin min/0";

        assertThrows(MediStockException.class,
                () -> Parser.parseCommand(input));
    }

    @Test
    void parseCommand_wrongTagOrder_throwsException() {
        String input = "edit n/Aspirin 500mg o/Aspirin";

        assertThrows(MediStockException.class,
                () -> Parser.parseCommand(input));
    }
}
