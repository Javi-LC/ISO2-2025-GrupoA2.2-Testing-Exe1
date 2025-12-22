package ISO2.Exe1;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import service.InputService;
import parser.ValueParser;
import exception.InputException;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Tests para App.java siguiendo técnicas formales de testing:
 * - Each-Use Coverage (1-wise)
 * - Pairwise Coverage (2-wise) 
 * - Decision Coverage (DC)
 * - MC/DC Coverage
 */
public class AppTest {

    private ByteArrayOutputStream outputStream;
    private PrintStream printStream;
    private MockInputService mockInput;
    private App app;

    @Before
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        printStream = new PrintStream(outputStream);
        mockInput = new MockInputService();
        app = new App(mockInput, printStream);
    }

    private String getOutput() {
        return outputStream.toString();
    }

    // =========================================================================
    // TESTS PARA run() Y main() - Cobertura completa del flujo
    // =========================================================================

    @Test
    public void testRunShowsWelcomeAndMenu() {
        mockInput.addIntegerResponse(2); // Exit inmediatamente
        app.run();
        String output = getOutput();
        assertTrue(output.contains("Welcome to Leap Year Checker"));
        assertTrue(output.contains("--- Menu ---"));
        assertTrue(output.contains("1. Check a Date"));
        assertTrue(output.contains("2. Exit"));
        assertTrue(output.contains("Goodbye!"));
    }

    @Test
    public void testRunProcessesOption1ThenExits() {
        mockInput.addIntegerResponse(1); // Check date
        mockInput.addIntegerResponse(15); // day
        mockInput.addIntegerResponse(6);  // month
        mockInput.addIntegerResponse(2024); // year (leap)
        mockInput.addIntegerResponse(2); // Exit
        app.run();
        String output = getOutput();
        assertTrue(output.contains("LEAP"));
        assertTrue(output.contains("Goodbye!"));
    }

    @Test
    public void testRunHandlesInputException() {
        mockInput.addException(new InputException("Test error"));
        mockInput.addIntegerResponse(2); // Exit
        app.run();
        String output = getOutput();
        assertTrue(output.contains("Error: Test error"));
    }

    @Test
    public void testRunHandlesGenericException() {
        mockInput.addException(new RuntimeException("Unexpected"));
        mockInput.addIntegerResponse(2); // Exit
        app.run();
        String output = getOutput();
        assertTrue(output.contains("Unexpected Error: Unexpected"));
    }

    @Test
    public void testRunClosesInput() {
        mockInput.addIntegerResponse(2);
        app.run();
        assertTrue(mockInput.isClosed());
    }

    @Test
    public void testRunMultipleOperations() {
        mockInput.addIntegerResponse(1); // Check date 1
        mockInput.addIntegerResponse(1);
        mockInput.addIntegerResponse(1);
        mockInput.addIntegerResponse(2000);
        mockInput.addIntegerResponse(1); // Check date 2
        mockInput.addIntegerResponse(1);
        mockInput.addIntegerResponse(1);
        mockInput.addIntegerResponse(2001);
        mockInput.addIntegerResponse(2); // Exit
        app.run();
        String output = getOutput();
        assertTrue(output.contains("LEAP"));
        assertTrue(output.contains("NOT"));
    }

    // =========================================================================
    // TESTS PARA checkDate() - Input parsing
    // =========================================================================

    @Test
    public void testCheckDateReadsInputs() throws InputException {
        mockInput.addIntegerResponse(15);
        mockInput.addIntegerResponse(6);
        mockInput.addIntegerResponse(2024);
        app.checkDate();
        String output = getOutput();
        assertTrue(output.contains("Reading Date..."));
        assertTrue(output.contains("LEAP"));
    }

    @Test
    public void testCheckDateNonLeap() throws InputException {
        mockInput.addIntegerResponse(15);
        mockInput.addIntegerResponse(6);
        mockInput.addIntegerResponse(2023);
        app.checkDate();
        assertTrue(getOutput().contains("NOT"));
    }

    @Test
    public void testCheckDateInvalidDate() throws InputException {
        mockInput.addIntegerResponse(32); // día inválido
        mockInput.addIntegerResponse(6);
        mockInput.addIntegerResponse(2024);
        app.checkDate();
        assertTrue(getOutput().contains("Date Error"));
    }

    @Test(expected = InputException.class)
    public void testCheckDatePropagatesException() throws InputException {
        mockInput.addException(new InputException("Parse error"));
        app.checkDate();
    }

    // =========================================================================
    // EACH-USE COVERAGE (1-wise) - Cada variable usada al menos una vez
    // =========================================================================

    @Test
    public void testAppInitialization() {
        assertNotNull(app);
        assertTrue(app.isRunning());
    }

    @Test
    public void testProcessChoiceOption1() throws Exception {
        mockInput.addIntegerResponse(15);
        mockInput.addIntegerResponse(6);
        mockInput.addIntegerResponse(2024);
        app.processChoice(1);
        assertTrue(getOutput().contains("LEAP"));
    }

    @Test
    public void testProcessChoiceOption2() throws Exception {
        app.processChoice(2);
        assertFalse(app.isRunning());
        assertTrue(getOutput().contains("Goodbye!"));
    }

    @Test
    public void testProcessChoiceInvalidOption() throws Exception {
        app.processChoice(99);
        assertTrue(getOutput().contains("Invalid option"));
    }

    @Test
    public void testCheckDateWithValidValues() {
        String result = app.checkDateWithValues(1, 1, 2020);
        assertNotNull(result);
        assertTrue(result.contains("2020"));
    }

    @Test
    public void testStop() {
        assertTrue(app.isRunning());
        app.stop();
        assertFalse(app.isRunning());
    }

    @Test
    public void testDefaultConstructor() {
        App defaultApp = new App();
        assertNotNull(defaultApp);
        assertTrue(defaultApp.isRunning());
    }

    // DECISION COVERAGE (DC) - Cada rama de decisión ejecutada

    @Test
    public void testSwitchCase1() throws InputException {
        mockInput.addIntegerResponse(15);
        mockInput.addIntegerResponse(3);
        mockInput.addIntegerResponse(2024);
        app.processChoice(1);
        assertTrue(getOutput().contains("LEAP"));
    }

    @Test
    public void testSwitchCase2() throws Exception {
        app.processChoice(2);
        assertFalse(app.isRunning());
    }

    @Test
    public void testSwitchDefault0() throws Exception {
        app.processChoice(0);
        assertTrue(getOutput().contains("Invalid option"));
    }

    @Test
    public void testSwitchDefault3() throws Exception {
        app.processChoice(3);
        assertTrue(getOutput().contains("Invalid option"));
    }

    @Test
    public void testSwitchDefaultNegative() throws Exception {
        app.processChoice(-1);
        assertTrue(getOutput().contains("Invalid option"));
    }

    @Test
    public void testIsLeapYearTrue() {
        String result = app.checkDateWithValues(1, 1, 2000);
        assertTrue(result.contains("LEAP"));
    }

    @Test
    public void testIsLeapYearFalse() {
        String result = app.checkDateWithValues(1, 1, 2001);
        assertTrue(result.contains("NOT"));
    }

    @Test
    public void testCatchIllegalArgumentException() {
        String result = app.checkDateWithValues(32, 1, 2020);
        assertTrue(result.contains("Date Error"));
    }

    // PAIRWISE COVERAGE (2-wise) - Combinaciones de pares de parámetros

    @Test
    public void testDayMonthCombinations() {
        int[][] cases = {
            {1,1,2020},{1,6,2020},{1,12,2020},{15,1,2020},{15,6,2020},{15,12,2020},{28,1,2020},{28,6,2020},{28,12,2020}
        };
        for (int[] c : cases) {
            String result = app.checkDateWithValues(c[0], c[1], c[2]);
            assertNotNull(result);
            assertTrue(result.contains(String.valueOf(c[2])));
        }
    }

    @Test
    public void testMonthYearCombinations() {
        int[][] cases = {
            {15,1,2000},{15,2,2000},{15,6,2004},{15,12,1900},{15,3,2001},{15,7,2023}
        };
        for (int[] c : cases) {
            String result = app.checkDateWithValues(c[0], c[1], c[2]);
            assertNotNull(result);
        }
    }

    @Test
    public void testDayYearCombinations() {
        int[][] cases = {
            {1,6,2000},{1,6,2001},{29,2,2000},{28,2,2001},{31,1,2024},{31,12,2023}
        };
        for (int[] c : cases) {
            String result = app.checkDateWithValues(c[0], c[1], c[2]);
            assertNotNull(result);
        }
    }

    // MC/DC COVERAGE - Condiciones independientes afectan decisión

    @Test
    public void testNotDivisibleBy4() {
        String result = app.checkDateWithValues(1, 1, 2001);
        assertTrue(result.contains("NOT"));
    }

    @Test
    public void testDivisibleBy4NotBy100() {
        String result = app.checkDateWithValues(1, 1, 2004);
        assertTrue(result.contains("LEAP"));
    }

    @Test
    public void testDivisibleBy100NotBy400() {
        String result = app.checkDateWithValues(1, 1, 1900);
        assertTrue(result.contains("NOT"));
    }

    @Test
    public void testDivisibleBy400() {
        String result = app.checkDateWithValues(1, 1, 2000);
        assertTrue(result.contains("LEAP"));
    }

    @Test
    public void testChoiceEquals1() throws InputException {
        mockInput.addIntegerResponse(1);
        mockInput.addIntegerResponse(1);
        mockInput.addIntegerResponse(2024);
        app.processChoice(1);
        assertNotNull(getOutput());
    }

    @Test
    public void testChoiceEquals2() throws Exception {
        app.processChoice(2);
        assertFalse(app.isRunning());
    }

    @Test
    public void testChoiceNotValid() throws Exception {
        app.processChoice(5);
        assertTrue(getOutput().contains("Invalid option"));
    }

    @Test
    public void testIsLeapTrue() {
        String result = app.checkDateWithValues(29, 2, 2024);
        assertTrue(result.contains("LEAP"));
    }

    @Test
    public void testIsLeapFalse2() {
        String result = app.checkDateWithValues(28, 2, 2023);
        assertTrue(result.contains("NOT"));
    }

    // EXCEPTION HANDLING TESTS

    @Test
    public void testInvalidDayZero() {
        String result = app.checkDateWithValues(0, 6, 2020);
        assertTrue(result.contains("Date Error"));
    }

    @Test
    public void testInvalidDay32() {
        String result = app.checkDateWithValues(32, 1, 2020);
        assertTrue(result.contains("Date Error"));
    }

    @Test
    public void testInvalidMonthZero() {
        String result = app.checkDateWithValues(15, 0, 2020);
        assertTrue(result.contains("Date Error"));
    }

    @Test
    public void testInvalidMonth13() {
        String result = app.checkDateWithValues(15, 13, 2020);
        assertTrue(result.contains("Date Error"));
    }

    @Test
    public void testInvalidYearNegative() {
        String result = app.checkDateWithValues(15, 6, -1);
        assertTrue(result.contains("Date Error"));
    }

    @Test
    public void testNegativeDay() {
        String result = app.checkDateWithValues(-1, 6, 2020);
        assertTrue(result.contains("Date Error"));
    }

    @Test
    public void testNegativeMonth() {
        String result = app.checkDateWithValues(15, -1, 2020);
        assertTrue(result.contains("Date Error"));
    }

    @Test
    public void testYearZeroIsValid() {
        String result = app.checkDateWithValues(15, 6, 0);
        assertFalse(result.contains("Date Error"));
    }

    @Test
    public void testMaxDay31() {
        String result = app.checkDateWithValues(31, 1, 2020);
        assertFalse(result.contains("Date Error"));
    }

    // BOUNDARY VALUE TESTS

    @Test
    public void testBoundaryYears() {
        int[] years = {1,4,100,400,1900,2000,2024,9999};
        for (int y : years) {
            String result = app.checkDateWithValues(1,1,y);
            assertNotNull(result);
            assertTrue(result.contains("year") || result.contains("Error"));
        }
    }

    @Test
    public void testBoundaryDays() {
        int[][] cases = {{1,1,2020},{31,1,2020},{28,2,2023},{29,2,2024},{30,4,2020},{31,12,2020}};
        for (int[] c : cases) {
            String result = app.checkDateWithValues(c[0], c[1], c[2]);
            assertNotNull(result);
            assertFalse(result.contains("Date Error"));
        }
    }

    // OUTPUT FORMAT TESTS

    @Test
    public void testLeapYearOutputFormat() {
        app.checkDateWithValues(15, 6, 2024);
        String output = getOutput();
        assertTrue(output.contains("LEAP"));
        assertTrue(output.contains("2024"));
    }

    @Test
    public void testNonLeapYearOutputFormat() {
        app.checkDateWithValues(15, 6, 2023);
        String output = getOutput();
        assertTrue(output.contains("NOT"));
    }

    @Test
    public void testGoodbyeMessage() throws Exception {
        app.processChoice(2);
        assertTrue(getOutput().contains("Goodbye!"));
    }

    @Test
    public void testInvalidOptionMessage() throws Exception {
        app.processChoice(100);
        assertTrue(getOutput().contains("Invalid option"));
    }

    // =========================================================================
    // MOCK INPUT SERVICE - Simulación completa de InputService
    // =========================================================================
    private static class MockInputService extends InputService {
        private final Queue<Object> responses = new LinkedList<>();
        private boolean closed = false;

        public MockInputService() {
            super();
        }

        public void addIntegerResponse(int value) {
            responses.add(value);
        }

        public void addStringResponse(String value) {
            responses.add(value);
        }

        public void addException(Exception e) {
            responses.add(e);
        }

        @Override
        public String readString(String prompt) throws InputException {
            if (responses.isEmpty()) {
                return "";
            }
            Object response = responses.poll();
            if (response instanceof InputException) {
                throw (InputException) response;
            }
            if (response instanceof RuntimeException) {
                throw (RuntimeException) response;
            }
            return response.toString();
        }

        @Override
        @SuppressWarnings("unchecked")
        public <T> T readWithParser(String prompt, ValueParser<T> parser) throws InputException {
            if (responses.isEmpty()) {
                throw new InputException("No more mock responses");
            }
            Object response = responses.poll();
            if (response instanceof InputException) {
                throw (InputException) response;
            }
            if (response instanceof RuntimeException) {
                throw (RuntimeException) response;
            }
            return (T) response;
        }

        @Override
        public void close() {
            closed = true;
        }

        public boolean isClosed() {
            return closed;
        }
    }
}
