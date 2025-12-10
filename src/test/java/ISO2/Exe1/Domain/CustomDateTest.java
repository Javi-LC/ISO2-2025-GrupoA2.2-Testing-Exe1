package ISO2.Exe1.Domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

class CustomDateTest {

    @DisplayName("Testing Valid Dates from Each-Use Table")
    @ParameterizedTest
    @CsvSource({
        "1, 1, 0",      // Case 2: Valid
        "2, 2, 1",      // Case 3: Valid
        "31, 12, 2020"  // Case 5: Valid (Adjusted MaxInt logic for practical year)
    })
    void testValidConstructor(int day, int month, int year) {
        assertDoesNotThrow(() -> new CustomDate(day, month, year));
    }

    @DisplayName("Testing Invalid Dates (Exceptions) from Each-Use Table")
    @ParameterizedTest
    @CsvSource({
        "0, 0, -1",     // Case 1: Exception (Day 0, Month 0, Year -1)
        "30, 11, -2147483648", // Case 4: Exception (MinInt Year)
        "32, 13, 0",    // Case 6: Exception (Day 32, Month 13)
    })
    void testInvalidConstructor(int day, int month, int year) {
        assertThrows(IllegalArgumentException.class, () -> {
            new CustomDate(day, month, year);
        });
    }

    @DisplayName("Testing Leap Year Logic (Decision Coverage)")
    @ParameterizedTest
    @CsvSource({
        "1, 1, 0, true",     // Case: Year 0 is leap (Divisible by 400) [cite: 330]
        "1, 1, 2000, true",  // Divisible by 400
        "1, 1, 2024, true",  // Divisible by 4 but not 100
        "1, 1, 1, false",    // Case: Year 1 is not leap [cite: 332]
        "1, 1, 100, false",  // Divisible by 4 and 100, but not 400 (Not Leap)
        "1, 1, 2023, false"  // Not divisible by 4
    })
    void testIsLeapYear(int day, int month, int year, boolean expectedResult) {
        CustomDate date = new CustomDate(day, month, year);
        assertEquals(expectedResult, date.isLeapYear(), 
            "Error testing leap year for year: " + year);
    }

    @DisplayName("Testing Boundaries for Day and Month")
    @ParameterizedTest
    @CsvSource({
        // Testing Day Boundaries (1 and 31 are valid)
        "1, 1, 2000",
        "31, 1, 2000",
        // Testing Month Boundaries (1 and 12 are valid)
        "15, 1, 2000",
        "15, 12, 2000"
    })
    void testBoundaries(int day, int month, int year) {
        assertDoesNotThrow(() -> new CustomDate(day, month, year));
    }
    
    @DisplayName("Testing Invalid Boundaries (Just outside limits)")
    @ParameterizedTest
    @CsvSource({
        "0, 1, 2000",   // Day < 1
        "32, 1, 2000",  // Day > 31
        "1, 0, 2000",   // Month < 1
        "1, 13, 2000",  // Month > 12
        "1, 1, -1"      // Year < 0
    })
    void testInvalidBoundaries(int day, int month, int year) {
        assertThrows(IllegalArgumentException.class, () -> new CustomDate(day, month, year));
    }

    @Test
    @DisplayName("Testing Getters")
    void testGetters() {
        CustomDate date = new CustomDate(15, 6, 2022);
        assertAll("Getters",
            () -> assertEquals(15, date.getDay()),
            () -> assertEquals(6, date.getMonth()),
            () -> assertEquals(2022, date.getYear())
        );
    }

    @Test
    @DisplayName("Testing toString")
    void testToString() {
        CustomDate date = new CustomDate(5, 3, 2021);
        assertEquals("05/03/2021", date.toString());
    }
}
