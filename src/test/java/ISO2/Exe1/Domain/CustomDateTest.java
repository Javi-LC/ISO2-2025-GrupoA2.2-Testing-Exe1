package ISO2.Exe1.Domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class CustomDateTest {

    @Test
    void testLeapYearDivisibleBy4() {
        CustomDate date = new CustomDate(1, 1, 2024);
        assertTrue(date.isLeapYear(), "2024 should be a leap year");
    }

    @Test
    void testNotLeapYearDivisibleBy100() {
        CustomDate date = new CustomDate(1, 1, 1900);
        assertFalse(date.isLeapYear(), "1900 should not be a leap year");
    }

    @Test
    void testLeapYearDivisibleBy400() {
        CustomDate date = new CustomDate(1, 1, 2000);
        assertTrue(date.isLeapYear(), "2000 should be a leap year");
    }

    @Test
    void testNotLeapYear() {
        CustomDate date = new CustomDate(1, 1, 2023);
        assertFalse(date.isLeapYear(), "2023 should not be a leap year");
    }

    @Test
    void testNegativeYearThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new CustomDate(1, 1, -5);
        });
        assertEquals("Year cannot be negative", exception.getMessage());
    }

    @Test
    void testInvalidMonthThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new CustomDate(1, 13, 2023);
        });
        assertEquals("Month must be between 1 and 12", exception.getMessage());
    }

    @Test
    void testInvalidDayThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new CustomDate(32, 1, 2023);
        });
        assertEquals("Day must be between 1 and 31", exception.getMessage());
    }
    
    @ParameterizedTest
    @ValueSource(ints = {2024, 2000, 2020, 2016})
    void testLeapYears(int year) {
        CustomDate date = new CustomDate(1, 1, year);
        assertTrue(date.isLeapYear());
    }

    @ParameterizedTest
    @ValueSource(ints = {2023, 1900, 2021, 2019})
    void testNonLeapYears(int year) {
        CustomDate date = new CustomDate(1, 1, year);
        assertFalse(date.isLeapYear());
    }
}
