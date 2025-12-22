package iso2.exe1;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import service.InputService;
import parser.ValueParser;
import exception.InputException;
import io.InputReader;
import io.OutputWriter;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests para App.java siguiendo técnicas formales de testing:
 * - Each-Use Coverage (1-wise)
 * - Pairwise Coverage (2-wise) 
 * - Decision Coverage (DC)
 * - MC/DC Coverage
 */
@DisplayName("App Tests")
class AppTest {

    private ByteArrayOutputStream outputStream;
    private PrintStream printStream;
    private MockInputService mockInput;
    private App app;

    @BeforeEach
    void setUp() {
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
    @Nested
    @DisplayName("Run Method Coverage Tests")
    class RunMethodTests {

        @Test
        @DisplayName("RUN-01: run() muestra mensaje de bienvenida y menú")
        void testRunShowsWelcomeAndMenu() {
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
        @DisplayName("RUN-02: run() procesa opción 1 y luego sale")
        void testRunProcessesOption1ThenExits() {
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
        @DisplayName("RUN-03: run() maneja InputException")
        void testRunHandlesInputException() {
            mockInput.addException(new InputException("Test error"));
            mockInput.addIntegerResponse(2); // Exit
            app.run();
            String output = getOutput();
            assertTrue(output.contains("Error: Test error"));
        }

        @Test
        @DisplayName("RUN-04: run() maneja Exception genérica")
        void testRunHandlesGenericException() {
            mockInput.addException(new RuntimeException("Unexpected"));
            mockInput.addIntegerResponse(2); // Exit
            app.run();
            String output = getOutput();
            assertTrue(output.contains("Unexpected Error: Unexpected"));
        }

        @Test
        @DisplayName("RUN-05: run() cierra el input al finalizar")
        void testRunClosesInput() {
            mockInput.addIntegerResponse(2);
            app.run();
            assertTrue(mockInput.isClosed());
        }

        @Test
        @DisplayName("RUN-06: run() con múltiples operaciones")
        void testRunMultipleOperations() {
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
    }

    // =========================================================================
    // TESTS PARA checkDate() - Input parsing
    // =========================================================================
    @Nested
    @DisplayName("CheckDate Method Tests")
    class CheckDateTests {

        @Test
        @DisplayName("CD-01: checkDate() lee día, mes y año correctamente")
        void testCheckDateReadsInputs() throws InputException {
            mockInput.addIntegerResponse(15);
            mockInput.addIntegerResponse(6);
            mockInput.addIntegerResponse(2024);
            app.checkDate();
            String output = getOutput();
            assertTrue(output.contains("Reading Date..."));
            assertTrue(output.contains("LEAP"));
        }

        @Test
        @DisplayName("CD-02: checkDate() con fecha no bisiesta")
        void testCheckDateNonLeap() throws InputException {
            mockInput.addIntegerResponse(15);
            mockInput.addIntegerResponse(6);
            mockInput.addIntegerResponse(2023);
            app.checkDate();
            assertTrue(getOutput().contains("NOT"));
        }

        @Test
        @DisplayName("CD-03: checkDate() con fecha inválida")
        void testCheckDateInvalidDate() throws InputException {
            mockInput.addIntegerResponse(32); // día inválido
            mockInput.addIntegerResponse(6);
            mockInput.addIntegerResponse(2024);
            app.checkDate();
            assertTrue(getOutput().contains("Date Error"));
        }

        @Test
        @DisplayName("CD-04: checkDate() propaga InputException")
        void testCheckDatePropagatesException() {
            mockInput.addException(new InputException("Parse error"));
            assertThrows(InputException.class, () -> app.checkDate());
        }
    }

    // =========================================================================
    // EACH-USE COVERAGE (1-wise) - Cada variable usada al menos una vez
    // =========================================================================
    @Nested
    @DisplayName("Each-Use Coverage Tests")
    class EachUseCoverageTests {

        @Test
        @DisplayName("EU-01: App inicializa correctamente")
        void testAppInitialization() {
            assertNotNull(app);
            assertTrue(app.isRunning());
        }

        @Test
        @DisplayName("EU-02: processChoice con opción 1 (checkDate)")
        void testProcessChoiceOption1() throws Exception {
            mockInput.addIntegerResponse(15);
            mockInput.addIntegerResponse(6);
            mockInput.addIntegerResponse(2024);
            app.processChoice(1);
            assertTrue(getOutput().contains("LEAP"));
        }

        @Test
        @DisplayName("EU-03: processChoice con opción 2 (Exit)")
        void testProcessChoiceOption2() throws Exception {
            app.processChoice(2);
            assertFalse(app.isRunning());
            assertTrue(getOutput().contains("Goodbye!"));
        }

        @Test
        @DisplayName("EU-04: processChoice con opción inválida")
        void testProcessChoiceInvalidOption() throws Exception {
            app.processChoice(99);
            assertTrue(getOutput().contains("Invalid option"));
        }

        @Test
        @DisplayName("EU-05: checkDateWithValues con fecha válida")
        void testCheckDateWithValidValues() {
            String result = app.checkDateWithValues(1, 1, 2020);
            assertNotNull(result);
            assertTrue(result.contains("2020"));
        }

        @Test
        @DisplayName("EU-06: stop() detiene la aplicación")
        void testStop() {
            assertTrue(app.isRunning());
            app.stop();
            assertFalse(app.isRunning());
        }

        @Test
        @DisplayName("EU-07: Constructor por defecto")
        void testDefaultConstructor() {
            App defaultApp = new App();
            assertNotNull(defaultApp);
            assertTrue(defaultApp.isRunning());
        }
    }

    // =========================================================================
    // DECISION COVERAGE (DC) - Cada rama de decisión ejecutada
    // =========================================================================
    @Nested
    @DisplayName("Decision Coverage Tests")
    class DecisionCoverageTests {

        @Test
        @DisplayName("DC-01: switch case 1")
        void testSwitchCase1() throws InputException {
            mockInput.addIntegerResponse(15);
            mockInput.addIntegerResponse(3);
            mockInput.addIntegerResponse(2024);
            app.processChoice(1);
            assertTrue(getOutput().contains("LEAP"));
        }

        @Test
        @DisplayName("DC-02: switch case 2 - Exit")
        void testSwitchCase2() throws Exception {
            app.processChoice(2);
            assertFalse(app.isRunning());
        }

        @Test
        @DisplayName("DC-03: switch default - opción inválida 0")
        void testSwitchDefault0() throws Exception {
            app.processChoice(0);
            assertTrue(getOutput().contains("Invalid option"));
        }

        @Test
        @DisplayName("DC-04: switch default - opción inválida 3")
        void testSwitchDefault3() throws Exception {
            app.processChoice(3);
            assertTrue(getOutput().contains("Invalid option"));
        }

        @Test
        @DisplayName("DC-05: switch default - opción negativa")
        void testSwitchDefaultNegative() throws Exception {
            app.processChoice(-1);
            assertTrue(getOutput().contains("Invalid option"));
        }

        @Test
        @DisplayName("DC-06: isLeapYear true branch")
        void testIsLeapYearTrue() {
            String result = app.checkDateWithValues(1, 1, 2000);
            assertTrue(result.contains("LEAP"));
        }

        @Test
        @DisplayName("DC-07: isLeapYear false branch")
        void testIsLeapYearFalse() {
            String result = app.checkDateWithValues(1, 1, 2001);
            assertTrue(result.contains("NOT"));
        }

        @Test
        @DisplayName("DC-08: catch IllegalArgumentException (fecha inválida)")
        void testCatchIllegalArgumentException() {
            String result = app.checkDateWithValues(32, 1, 2020);
            assertTrue(result.contains("Date Error"));
        }
    }

    // =========================================================================
    // PAIRWISE COVERAGE (2-wise) - Combinaciones de pares de parámetros
    // =========================================================================
    @Nested
    @DisplayName("Pairwise Coverage Tests")
    class PairwiseCoverageTests {

        @ParameterizedTest(name = "PW-{index}: day={0}, month={1}, year={2}")
        @DisplayName("Pairwise: Combinaciones día-mes")
        @CsvSource({
            "1, 1, 2020",
            "1, 6, 2020",
            "1, 12, 2020",
            "15, 1, 2020",
            "15, 6, 2020",
            "15, 12, 2020",
            "28, 1, 2020",
            "28, 6, 2020",
            "28, 12, 2020"
        })
        void testDayMonthCombinations(int day, int month, int year) {
            String result = app.checkDateWithValues(day, month, year);
            assertNotNull(result);
            assertTrue(result.contains(String.valueOf(year)));
        }

        @ParameterizedTest(name = "PW-{index}: month={1}, year={2}")
        @DisplayName("Pairwise: Combinaciones mes-año")
        @CsvSource({
            "15, 1, 2000",
            "15, 2, 2000",
            "15, 6, 2004",
            "15, 12, 1900",
            "15, 3, 2001",
            "15, 7, 2023"
        })
        void testMonthYearCombinations(int day, int month, int year) {
            String result = app.checkDateWithValues(day, month, year);
            assertNotNull(result);
        }

        @ParameterizedTest(name = "PW-{index}: day={0}, year={2}")
        @DisplayName("Pairwise: Combinaciones día-año")
        @CsvSource({
            "1, 6, 2000",
            "1, 6, 2001",
            "29, 2, 2000",
            "28, 2, 2001",
            "31, 1, 2024",
            "31, 12, 2023"
        })
        void testDayYearCombinations(int day, int month, int year) {
            String result = app.checkDateWithValues(day, month, year);
            assertNotNull(result);
        }
    }

    // =========================================================================
    // MC/DC COVERAGE - Condiciones independientes afectan decisión
    // =========================================================================
    @Nested
    @DisplayName("MC/DC Coverage Tests")
    class MCDCCoverageTests {

        @Test
        @DisplayName("MCDC-01: year % 4 != 0 → NOT leap")
        void testNotDivisibleBy4() {
            String result = app.checkDateWithValues(1, 1, 2001);
            assertTrue(result.contains("NOT"));
        }

        @Test
        @DisplayName("MCDC-02: year % 4 == 0, % 100 != 0 → LEAP")
        void testDivisibleBy4NotBy100() {
            String result = app.checkDateWithValues(1, 1, 2004);
            assertTrue(result.contains("LEAP"));
        }

        @Test
        @DisplayName("MCDC-03: year % 100 == 0, % 400 != 0 → NOT leap")
        void testDivisibleBy100NotBy400() {
            String result = app.checkDateWithValues(1, 1, 1900);
            assertTrue(result.contains("NOT"));
        }

        @Test
        @DisplayName("MCDC-04: year % 400 == 0 → LEAP")
        void testDivisibleBy400() {
            String result = app.checkDateWithValues(1, 1, 2000);
            assertTrue(result.contains("LEAP"));
        }

        @Test
        @DisplayName("MCDC-05: choice == 1")
        void testChoiceEquals1() throws InputException {
            mockInput.addIntegerResponse(1);
            mockInput.addIntegerResponse(1);
            mockInput.addIntegerResponse(2024);
            app.processChoice(1);
            assertNotNull(getOutput());
        }

        @Test
        @DisplayName("MCDC-06: choice == 2")
        void testChoiceEquals2() throws Exception {
            app.processChoice(2);
            assertFalse(app.isRunning());
        }

        @Test
        @DisplayName("MCDC-07: choice != 1 && choice != 2")
        void testChoiceNotValid() throws Exception {
            app.processChoice(5);
            assertTrue(getOutput().contains("Invalid option"));
        }

        @Test
        @DisplayName("MCDC-08: isLeap = true muestra LEAP")
        void testIsLeapTrue() {
            String result = app.checkDateWithValues(29, 2, 2024);
            assertTrue(result.contains("LEAP"));
        }

        @Test
        @DisplayName("MCDC-09: isLeap = false muestra NOT")
        void testIsLeapFalse() {
            String result = app.checkDateWithValues(28, 2, 2023);
            assertTrue(result.contains("NOT"));
        }
    }

    // =========================================================================
    // EXCEPTION HANDLING TESTS
    // =========================================================================
    @Nested
    @DisplayName("Exception Handling Tests")
    class ExceptionHandlingTests {

        @Test
        @DisplayName("EX-01: Día inválido (0)")
        void testInvalidDayZero() {
            String result = app.checkDateWithValues(0, 6, 2020);
            assertTrue(result.contains("Date Error"));
        }

        @Test
        @DisplayName("EX-02: Día inválido (32)")
        void testInvalidDay32() {
            String result = app.checkDateWithValues(32, 1, 2020);
            assertTrue(result.contains("Date Error"));
        }

        @Test
        @DisplayName("EX-03: Mes inválido (0)")
        void testInvalidMonthZero() {
            String result = app.checkDateWithValues(15, 0, 2020);
            assertTrue(result.contains("Date Error"));
        }

        @Test
        @DisplayName("EX-04: Mes inválido (13)")
        void testInvalidMonth13() {
            String result = app.checkDateWithValues(15, 13, 2020);
            assertTrue(result.contains("Date Error"));
        }

        @Test
        @DisplayName("EX-05: Año inválido negativo")
        void testInvalidYearNegative() {
            String result = app.checkDateWithValues(15, 6, -1);
            assertTrue(result.contains("Date Error"));
        }

        @Test
        @DisplayName("EX-06: Día negativo")
        void testNegativeDay() {
            String result = app.checkDateWithValues(-1, 6, 2020);
            assertTrue(result.contains("Date Error"));
        }

        @Test
        @DisplayName("EX-07: Mes negativo")
        void testNegativeMonth() {
            String result = app.checkDateWithValues(15, -1, 2020);
            assertTrue(result.contains("Date Error"));
        }

        @Test
        @DisplayName("EX-08: Año cero es válido (no lanza error)")
        void testYearZeroIsValid() {
            String result = app.checkDateWithValues(15, 6, 0);
            assertFalse(result.contains("Date Error"));
        }

        @Test
        @DisplayName("EX-09: Día máximo (31) es válido")
        void testMaxDay31() {
            String result = app.checkDateWithValues(31, 1, 2020);
            assertFalse(result.contains("Date Error"));
        }
    }

    // =========================================================================
    // BOUNDARY VALUE TESTS
    // =========================================================================
    @Nested
    @DisplayName("Boundary Value Tests")
    class BoundaryValueTests {

        @ParameterizedTest(name = "BV-{index}: year={0}")
        @DisplayName("Años límite")
        @ValueSource(ints = {1, 4, 100, 400, 1900, 2000, 2024, 9999})
        void testBoundaryYears(int year) {
            String result = app.checkDateWithValues(1, 1, year);
            assertNotNull(result);
            assertTrue(result.contains("year") || result.contains("Error"));
        }

        @ParameterizedTest(name = "BV-{index}: day={0}")
        @DisplayName("Días límite en diferentes meses")
        @CsvSource({
            "1, 1, 2020",
            "31, 1, 2020",
            "28, 2, 2023",
            "29, 2, 2024",
            "30, 4, 2020",
            "31, 12, 2020"
        })
        void testBoundaryDays(int day, int month, int year) {
            String result = app.checkDateWithValues(day, month, year);
            assertNotNull(result);
            assertFalse(result.contains("Date Error"));
        }
    }

    // =========================================================================
    // OUTPUT FORMAT TESTS
    // =========================================================================
    @Nested
    @DisplayName("Output Format Tests")
    class OutputFormatTests {

        @Test
        @DisplayName("OF-01: Formato salida año bisiesto")
        void testLeapYearOutputFormat() {
            app.checkDateWithValues(15, 6, 2024);
            String output = getOutput();
            assertTrue(output.contains("LEAP"));
            assertTrue(output.contains("2024"));
        }

        @Test
        @DisplayName("OF-02: Formato salida año no bisiesto")
        void testNonLeapYearOutputFormat() {
            app.checkDateWithValues(15, 6, 2023);
            String output = getOutput();
            assertTrue(output.contains("NOT"));
        }

        @Test
        @DisplayName("OF-03: Mensaje Goodbye al salir")
        void testGoodbyeMessage() throws Exception {
            app.processChoice(2);
            assertTrue(getOutput().contains("Goodbye!"));
        }

        @Test
        @DisplayName("OF-04: Mensaje opción inválida")
        void testInvalidOptionMessage() throws Exception {
            app.processChoice(100);
            assertTrue(getOutput().contains("Invalid option"));
        }
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
