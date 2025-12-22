package iso2.exe1;

import iso2.exe1.domain.CustomDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class CustomDateTest {

    // ============================================================
    // EACH-USE (1-wise) - 11 test cases
    // Each value of each variable is used at least once
    // ============================================================
    @Nested
    @DisplayName("Each-Use Coverage (1-wise)")
    class EachUseCoverage {

        @Test
        @DisplayName("Case 1: Day=0, Month=0, Year=-1 -> Exception")
        void testCase1_InvalidAll() {
            assertThrows(IllegalArgumentException.class, () -> new CustomDate(0, 0, -1));
        }

        @Test
        @DisplayName("Case 2: Day=1, Month=1, Year=0 -> Valid")
        void testCase2_Valid() {
            CustomDate date = new CustomDate(1, 1, 0);
            assertNotNull(date);
        }

        @Test
        @DisplayName("Case 3: Day=2, Month=2, Year=1 -> Valid")
        void testCase3_Valid() {
            CustomDate date = new CustomDate(2, 2, 1);
            assertNotNull(date);
        }

        @Test
        @DisplayName("Case 4: Day=30, Month=11, Year=MinInt -> Exception")
        void testCase4_MinIntYear() {
            assertThrows(IllegalArgumentException.class, () -> new CustomDate(30, 11, Integer.MIN_VALUE));
        }

        @Test
        @DisplayName("Case 5: Day=31, Month=12, Year=MaxInt -> Valid")
        void testCase5_MaxIntYear() {
            CustomDate date = new CustomDate(31, 12, Integer.MAX_VALUE);
            assertNotNull(date);
        }

        @Test
        @DisplayName("Case 6: Day=32, Month=13, Year=valid -> Exception")
        void testCase6_InvalidDayMonth() {
            assertThrows(IllegalArgumentException.class, () -> new CustomDate(32, 13, 2023));
        }

        @Test
        @DisplayName("Case 7: Day=MinInt, Month=MinInt -> Exception")
        void testCase7_MinIntDayMonth() {
            assertThrows(IllegalArgumentException.class, () -> new CustomDate(Integer.MIN_VALUE, Integer.MIN_VALUE, 0));
        }

        @Test
        @DisplayName("Case 8: Day=MaxInt, Month=MaxInt -> Exception")
        void testCase8_MaxIntDayMonth() {
            assertThrows(IllegalArgumentException.class, () -> new CustomDate(Integer.MAX_VALUE, Integer.MAX_VALUE, 0));
        }

        @Test
        @DisplayName("Case 9: Valid boundary day=1")
        void testCase9_BoundaryDay1() {
            CustomDate date = new CustomDate(1, 1, 2023);
            assertEquals(1, date.getDay());
        }

        @Test
        @DisplayName("Case 10: Valid boundary day=31")
        void testCase10_BoundaryDay31() {
            CustomDate date = new CustomDate(31, 1, 2023);
            assertEquals(31, date.getDay());
        }

        @Test
        @DisplayName("Case 11: Valid boundary month=12")
        void testCase11_BoundaryMonth12() {
            CustomDate date = new CustomDate(1, 12, 2023);
            assertEquals(12, date.getMonth());
        }
    }

    // ============================================================
    // PAIRWISE (2-wise) - Selected cases from PICT algorithm
    // Covers all pairs of variable values
    // ============================================================
    @Nested
    @DisplayName("Pairwise Coverage (2-wise)")
    class PairwiseCoverage {

        // Valid cases
        @ParameterizedTest
        @DisplayName("Valid pairwise combinations")
        @CsvSource({
            "1, 1, 1",
            "2, 2, 1",
            "30, 12, 1",
            "31, 12, 1",
            "1, 11, 0",
            "2, 11, 0",
            "30, 11, 0",
            "1, 2, 1",
            "2, 1, 1",
            "30, 1, 0",
            "31, 1, 0",
            "1, 12, 0"
        })
        void testValidPairwiseCombinations(int day, int month, int year) {
            CustomDate date = new CustomDate(day, month, year);
            assertNotNull(date);
            assertEquals(day, date.getDay());
            assertEquals(month, date.getMonth());
            assertEquals(year, date.getYear());
        }

        // Invalid day cases
        @ParameterizedTest
        @DisplayName("Invalid day pairwise - day < 1")
        @CsvSource({
            "0, 1, 0",
            "0, 12, 1",
            "0, 11, 0",
            "-1, 1, 0"
        })
        void testInvalidDayLow(int day, int month, int year) {
            assertThrows(IllegalArgumentException.class, () -> new CustomDate(day, month, year));
        }

        @ParameterizedTest
        @DisplayName("Invalid day pairwise - day > 31")
        @CsvSource({
            "32, 1, 0",
            "32, 12, 1",
            "32, 11, 0",
            "32, 2, 1"
        })
        void testInvalidDayHigh(int day, int month, int year) {
            assertThrows(IllegalArgumentException.class, () -> new CustomDate(day, month, year));
        }

        // Invalid month cases
        @ParameterizedTest
        @DisplayName("Invalid month pairwise - month < 1")
        @CsvSource({
            "1, 0, 0",
            "31, 0, 1",
            "2, 0, 0"
        })
        void testInvalidMonthLow(int day, int month, int year) {
            assertThrows(IllegalArgumentException.class, () -> new CustomDate(day, month, year));
        }

        @ParameterizedTest
        @DisplayName("Invalid month pairwise - month > 12")
        @CsvSource({
            "1, 13, 0",
            "31, 13, 1",
            "30, 13, 0",
            "2, 13, 1"
        })
        void testInvalidMonthHigh(int day, int month, int year) {
            assertThrows(IllegalArgumentException.class, () -> new CustomDate(day, month, year));
        }

        // Invalid year cases
        @ParameterizedTest
        @DisplayName("Invalid year pairwise - year < 0")
        @CsvSource({
            "1, 1, -1",
            "31, 12, -1",
            "2, 2, -1"
        })
        void testInvalidYearNegative(int day, int month, int year) {
            assertThrows(IllegalArgumentException.class, () -> new CustomDate(day, month, year));
        }

        // Extreme value pairs
        @Test
        @DisplayName("MinInt day with valid month/year")
        void testMinIntDay() {
            assertThrows(IllegalArgumentException.class, () -> new CustomDate(Integer.MIN_VALUE, 1, 0));
        }

        @Test
        @DisplayName("MaxInt day with valid month/year")
        void testMaxIntDay() {
            assertThrows(IllegalArgumentException.class, () -> new CustomDate(Integer.MAX_VALUE, 1, 0));
        }

        @Test
        @DisplayName("MinInt month with valid day/year")
        void testMinIntMonth() {
            assertThrows(IllegalArgumentException.class, () -> new CustomDate(1, Integer.MIN_VALUE, 0));
        }

        @Test
        @DisplayName("MaxInt month with valid day/year")
        void testMaxIntMonth() {
            assertThrows(IllegalArgumentException.class, () -> new CustomDate(1, Integer.MAX_VALUE, 0));
        }

        @Test
        @DisplayName("MinInt year with valid day/month")
        void testMinIntYear() {
            assertThrows(IllegalArgumentException.class, () -> new CustomDate(1, 1, Integer.MIN_VALUE));
        }

        @Test
        @DisplayName("MaxInt year with valid day/month")
        void testMaxIntYearValid() {
            CustomDate date = new CustomDate(1, 1, Integer.MAX_VALUE);
            assertEquals(Integer.MAX_VALUE, date.getYear());
        }
    }

    // ============================================================
    // DECISION COVERAGE (DC)
    // Each decision evaluates to both true and false
    // ============================================================
    @Nested
    @DisplayName("Decision Coverage (DC)")
    class DecisionCoverage {

        // Decision 1: if (day < 1 || day > 31)
        @Nested
        @DisplayName("Decision 1: day < 1 || day > 31")
        class DayDecision {
            @Test
            @DisplayName("DC1.1: Decision FALSE (day=1, valid)")
            void testDayDecisionFalse() {
                CustomDate date = new CustomDate(1, 1, 2023);
                assertEquals(1, date.getDay());
            }

            @Test
            @DisplayName("DC1.2: Decision TRUE via day < 1 (day=0)")
            void testDayDecisionTrueViaLow() {
                assertThrows(IllegalArgumentException.class, () -> new CustomDate(0, 1, 2023));
            }

            @Test
            @DisplayName("DC1.3: Decision TRUE via day > 31 (day=32)")
            void testDayDecisionTrueViaHigh() {
                assertThrows(IllegalArgumentException.class, () -> new CustomDate(32, 1, 2023));
            }
        }

        // Decision 2: if (month < 1 || month > 12)
        @Nested
        @DisplayName("Decision 2: month < 1 || month > 12")
        class MonthDecision {
            @Test
            @DisplayName("DC2.1: Decision FALSE (month=1, valid)")
            void testMonthDecisionFalse() {
                CustomDate date = new CustomDate(1, 1, 2023);
                assertEquals(1, date.getMonth());
            }

            @Test
            @DisplayName("DC2.2: Decision TRUE via month < 1 (month=0)")
            void testMonthDecisionTrueViaLow() {
                assertThrows(IllegalArgumentException.class, () -> new CustomDate(1, 0, 2023));
            }

            @Test
            @DisplayName("DC2.3: Decision TRUE via month > 12 (month=13)")
            void testMonthDecisionTrueViaHigh() {
                assertThrows(IllegalArgumentException.class, () -> new CustomDate(1, 13, 2023));
            }
        }

        // Decision 3: if (year < 0)
        @Nested
        @DisplayName("Decision 3: year < 0")
        class YearDecision {
            @Test
            @DisplayName("DC3.1: Decision FALSE (year=0, valid)")
            void testYearDecisionFalse() {
                CustomDate date = new CustomDate(1, 1, 0);
                assertEquals(0, date.getYear());
            }

            @Test
            @DisplayName("DC3.2: Decision TRUE (year=-1)")
            void testYearDecisionTrue() {
                assertThrows(IllegalArgumentException.class, () -> new CustomDate(1, 1, -1));
            }
        }

        // Decision 4: (year % 4 == 0 && year % 100 != 0) || year % 400 == 0
        @Nested
        @DisplayName("Decision 4: isLeapYear")
        class LeapYearDecision {
            @Test
            @DisplayName("DC4.1: Decision TRUE (year=0, divisible by 400)")
            void testLeapYearDecisionTrue() {
                CustomDate date = new CustomDate(1, 1, 0);
                assertTrue(date.isLeapYear());
            }

            @Test
            @DisplayName("DC4.2: Decision FALSE (year=1, not leap)")
            void testLeapYearDecisionFalse() {
                CustomDate date = new CustomDate(1, 1, 1);
                assertFalse(date.isLeapYear());
            }
        }
    }

    // ============================================================
    // MC/DC (Modified Condition/Decision Coverage)
    // Each condition independently affects the decision outcome
    // ============================================================
    @Nested
    @DisplayName("MC/DC Coverage")
    class MCDCCoverage {

        // MC/DC for Decision 1: day < 1 || day > 31
        @Nested
        @DisplayName("MC/DC Decision 1: day < 1 || day > 31")
        class MCDCDayDecision {
            @Test
            @DisplayName("MCDC1.1: A=0, B=0 -> Result=0 (day=1)")
            void testMCDC1_A0B0() {
                CustomDate date = new CustomDate(1, 1, 2023);
                assertEquals(1, date.getDay());
            }

            @Test
            @DisplayName("MCDC1.2: A=1, B=0 -> Result=1 (day=0) - A dominates")
            void testMCDC1_A1B0() {
                assertThrows(IllegalArgumentException.class, () -> new CustomDate(0, 1, 2023));
            }

            @Test
            @DisplayName("MCDC1.3: A=0, B=1 -> Result=1 (day=32) - B dominates")
            void testMCDC1_A0B1() {
                assertThrows(IllegalArgumentException.class, () -> new CustomDate(32, 1, 2023));
            }
        }

        // MC/DC for Decision 2: month < 1 || month > 12
        @Nested
        @DisplayName("MC/DC Decision 2: month < 1 || month > 12")
        class MCDCMonthDecision {
            @Test
            @DisplayName("MCDC2.1: A=0, B=0 -> Result=0 (month=1)")
            void testMCDC2_A0B0() {
                CustomDate date = new CustomDate(1, 1, 2023);
                assertEquals(1, date.getMonth());
            }

            @Test
            @DisplayName("MCDC2.2: A=1, B=0 -> Result=1 (month=0) - A dominates")
            void testMCDC2_A1B0() {
                assertThrows(IllegalArgumentException.class, () -> new CustomDate(1, 0, 2023));
            }

            @Test
            @DisplayName("MCDC2.3: A=0, B=1 -> Result=1 (month=13) - B dominates")
            void testMCDC2_A0B1() {
                assertThrows(IllegalArgumentException.class, () -> new CustomDate(1, 13, 2023));
            }
        }

        // MC/DC for Decision 3: year < 0
        @Nested
        @DisplayName("MC/DC Decision 3: year < 0")
        class MCDCYearDecision {
            @Test
            @DisplayName("MCDC3.1: A=0 -> Result=0 (year=0)")
            void testMCDC3_A0() {
                CustomDate date = new CustomDate(1, 1, 0);
                assertEquals(0, date.getYear());
            }

            @Test
            @DisplayName("MCDC3.2: A=1 -> Result=1 (year=-1) - A dominates")
            void testMCDC3_A1() {
                assertThrows(IllegalArgumentException.class, () -> new CustomDate(1, 1, -1));
            }
        }

        // MC/DC for Decision 4: (year % 4 == 0 && year % 100 != 0) || year % 400 == 0
        // A: year % 4 == 0
        // B: year % 100 != 0
        // C: year % 400 == 0
        @Nested
        @DisplayName("MC/DC Decision 4: isLeapYear")
        class MCDCLeapYearDecision {
            
            @Test
            @DisplayName("MCDC4.1: A=T, B=F, C=T -> TRUE (year=0) - C dominates")
            void testMCDC4_Year0() {
                CustomDate date = new CustomDate(1, 1, 0);
                assertTrue(date.isLeapYear());
            }

            @Test
            @DisplayName("MCDC4.2: A=F, B=T, C=F -> FALSE (year=1) - A dominates")
            void testMCDC4_Year1() {
                CustomDate date = new CustomDate(1, 1, 1);
                assertFalse(date.isLeapYear());
            }

            @Test
            @DisplayName("MCDC4.3: A=T, B=T, C=F -> TRUE (year=4) - A&&B true")
            void testMCDC4_Year4() {
                CustomDate date = new CustomDate(1, 1, 4);
                assertTrue(date.isLeapYear());
            }

            @Test
            @DisplayName("MCDC4.4: A=T, B=F, C=F -> FALSE (year=100) - B dominates")
            void testMCDC4_Year100() {
                CustomDate date = new CustomDate(1, 1, 100);
                assertFalse(date.isLeapYear());
            }

            @Test
            @DisplayName("MCDC4.5: A=T, B=F, C=T -> TRUE (year=400) - C dominates")
            void testMCDC4_Year400() {
                CustomDate date = new CustomDate(1, 1, 400);
                assertTrue(date.isLeapYear());
            }

            @Test
            @DisplayName("MCDC4.6: A=T, B=T, C=F -> TRUE (year=2024)")
            void testMCDC4_Year2024() {
                CustomDate date = new CustomDate(1, 1, 2024);
                assertTrue(date.isLeapYear());
            }

            @Test
            @DisplayName("MCDC4.7: A=T, B=F, C=F -> FALSE (year=1900)")
            void testMCDC4_Year1900() {
                CustomDate date = new CustomDate(1, 1, 1900);
                assertFalse(date.isLeapYear());
            }

            @Test
            @DisplayName("MCDC4.8: A=T, B=F, C=T -> TRUE (year=2000)")
            void testMCDC4_Year2000() {
                CustomDate date = new CustomDate(1, 1, 2000);
                assertTrue(date.isLeapYear());
            }
        }
    }

    // ============================================================
    // Additional Coverage Tests (Getters, toString)
    // ============================================================
    @Nested
    @DisplayName("Utility Methods Coverage")
    class UtilityMethodsCoverage {
        
        @Test
        void testGetDay() {
            CustomDate date = new CustomDate(15, 6, 2023);
            assertEquals(15, date.getDay());
        }

        @Test
        void testGetMonth() {
            CustomDate date = new CustomDate(15, 6, 2023);
            assertEquals(6, date.getMonth());
        }

        @Test
        void testGetYear() {
            CustomDate date = new CustomDate(15, 6, 2023);
            assertEquals(2023, date.getYear());
        }

        @Test
        void testToString() {
            CustomDate date = new CustomDate(5, 3, 2023);
            assertEquals("05/03/2023", date.toString());
        }

        @Test
        void testToStringWithPadding() {
            CustomDate date = new CustomDate(1, 1, 1);
            assertEquals("01/01/0001", date.toString());
        }
    }
}
