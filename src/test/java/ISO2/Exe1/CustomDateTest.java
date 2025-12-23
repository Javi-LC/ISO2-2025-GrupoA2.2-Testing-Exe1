package ISO2.Exe1;

import static org.junit.Assert.*;

import org.junit.Test;

import ISO2.Exe1.Domain.CustomDate;

/**
 * Unified test class for CustomDate following the same JUnit4 style as Exe2 tests
 * (no Enclosed runner, single test class, ordered imports and static asserts).
 */
public class CustomDateTest {

    // -----------------------------------------------------------------
    // EACH-USE (1-wise)
    // -----------------------------------------------------------------

    @Test(expected = IllegalArgumentException.class)
    public void EU_testCase1_InvalidAll() {
        new CustomDate(0, 0, -1);
    }

    @Test
    public void EU_testCase2_Valid() {
        CustomDate date = new CustomDate(1, 1, 0);
        assertNotNull(date);
    }

    @Test
    public void EU_testCase3_Valid() {
        CustomDate date = new CustomDate(2, 2, 1);
        assertNotNull(date);
    }

    @Test(expected = IllegalArgumentException.class)
    public void EU_testCase4_MinIntYear() {
        new CustomDate(30, 11, Integer.MIN_VALUE);
    }

    @Test
    public void EU_testCase5_MaxIntYear() {
        CustomDate date = new CustomDate(31, 12, Integer.MAX_VALUE);
        assertNotNull(date);
    }

    @Test(expected = IllegalArgumentException.class)
    public void EU_testCase6_InvalidDayMonth() {
        new CustomDate(32, 13, 2023);
    }

    @Test(expected = IllegalArgumentException.class)
    public void EU_testCase7_MinIntDayMonth() {
        new CustomDate(Integer.MIN_VALUE, Integer.MIN_VALUE, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void EU_testCase8_MaxIntDayMonth() {
        new CustomDate(Integer.MAX_VALUE, Integer.MAX_VALUE, 0);
    }

    @Test
    public void EU_testCase9_BoundaryDay1() {
        CustomDate date = new CustomDate(1, 1, 2023);
        assertEquals(1, date.getDay());
    }

    @Test
    public void EU_testCase10_BoundaryDay31() {
        CustomDate date = new CustomDate(31, 1, 2023);
        assertEquals(31, date.getDay());
    }

    @Test
    public void EU_testCase11_BoundaryMonth12() {
        CustomDate date = new CustomDate(1, 12, 2023);
        assertEquals(12, date.getMonth());
    }

    // -----------------------------------------------------------------
    // PAIRWISE (2-wise)
    // -----------------------------------------------------------------

    @Test
    public void PW_testValidPairwiseCombinations() {
        int[][] data = {
            {1, 1, 1}, {2, 2, 1}, {30, 12, 1}, {31, 12, 1},
            {1, 11, 0}, {2, 11, 0}, {30, 11, 0},
            {1, 2, 1}, {2, 1, 1}, {30, 1, 0}, {31, 1, 0}, {1, 12, 0}
        };

        for (int[] row : data) {
            CustomDate date = new CustomDate(row[0], row[1], row[2]);
            assertNotNull("Failed for: " + row[0] + "/" + row[1] + "/" + row[2], date);
            assertEquals(row[0], date.getDay());
            assertEquals(row[1], date.getMonth());
            assertEquals(row[2], date.getYear());
        }
    }

    @Test
    public void PW_testInvalidDayLow() {
        int[][] data = { {0, 1, 0}, {0, 12, 1}, {0, 11, 0}, {-1, 1, 0} };
        for (int[] row : data) {
            try {
                new CustomDate(row[0], row[1], row[2]);
                fail("Should have thrown Exception for day < 1: " + row[0]);
            } catch (IllegalArgumentException e) {
                // Expected
            }
        }
    }

    @Test
    public void PW_testInvalidDayHigh() {
        int[][] data = { {32, 1, 0}, {32, 12, 1}, {32, 11, 0}, {32, 2, 1} };
        for (int[] row : data) {
            try {
                new CustomDate(row[0], row[1], row[2]);
                fail("Should have thrown Exception for day > 31: " + row[0]);
            } catch (IllegalArgumentException e) {
                // Expected
            }
        }
    }

    @Test
    public void PW_testInvalidMonthLow() {
        int[][] data = { {1, 0, 0}, {31, 0, 1}, {2, 0, 0} };
        for (int[] row : data) {
            try {
                new CustomDate(row[0], row[1], row[2]);
                fail("Should have thrown Exception for month < 1: " + row[1]);
            } catch (IllegalArgumentException e) {
                // Expected
            }
        }
    }

    @Test
    public void PW_testInvalidMonthHigh() {
        int[][] data = { {1, 13, 0}, {31, 13, 1}, {30, 13, 0}, {2, 13, 1} };
        for (int[] row : data) {
            try {
                new CustomDate(row[0], row[1], row[2]);
                fail("Should have thrown Exception for month > 12: " + row[1]);
            } catch (IllegalArgumentException e) {
                // Expected
            }
        }
    }

    @Test
    public void PW_testInvalidYearNegative() {
        int[][] data = { {1, 1, -1}, {31, 12, -1}, {2, 2, -1} };
        for (int[] row : data) {
            try {
                new CustomDate(row[0], row[1], row[2]);
                fail("Should have thrown Exception for year < 0: " + row[2]);
            } catch (IllegalArgumentException e) {
                // Expected
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void PW_testMinIntDay() {
        new CustomDate(Integer.MIN_VALUE, 1, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void PW_testMaxIntDay() {
        new CustomDate(Integer.MAX_VALUE, 1, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void PW_testMinIntMonth() {
        new CustomDate(1, Integer.MIN_VALUE, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void PW_testMaxIntMonth() {
        new CustomDate(1, Integer.MAX_VALUE, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void PW_testMinIntYear() {
        new CustomDate(1, 1, Integer.MIN_VALUE);
    }

    @Test
    public void PW_testMaxIntYearValid() {
        CustomDate date = new CustomDate(1, 1, Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, date.getYear());
    }

    // -----------------------------------------------------------------
    // DECISION COVERAGE (DC)
    // -----------------------------------------------------------------

    @Test
    public void DC_testDayDecisionFalse() {
        CustomDate date = new CustomDate(1, 1, 2023);
        assertEquals(1, date.getDay());
    }

    @Test(expected = IllegalArgumentException.class)
    public void DC_testDayDecisionTrueViaLow() {
        new CustomDate(0, 1, 2023);
    }

    @Test(expected = IllegalArgumentException.class)
    public void DC_testDayDecisionTrueViaHigh() {
        new CustomDate(32, 1, 2023);
    }

    @Test
    public void DC_testMonthDecisionFalse() {
        CustomDate date = new CustomDate(1, 1, 2023);
        assertEquals(1, date.getMonth());
    }

    @Test(expected = IllegalArgumentException.class)
    public void DC_testMonthDecisionTrueViaLow() {
        new CustomDate(1, 0, 2023);
    }

    @Test(expected = IllegalArgumentException.class)
    public void DC_testMonthDecisionTrueViaHigh() {
        new CustomDate(1, 13, 2023);
    }

    @Test
    public void DC_testYearDecisionFalse() {
        CustomDate date = new CustomDate(1, 1, 0);
        assertEquals(0, date.getYear());
    }

    @Test(expected = IllegalArgumentException.class)
    public void DC_testYearDecisionTrue() {
        new CustomDate(1, 1, -1);
    }

    @Test
    public void DC_testLeapYearDecisionTrue() {
        CustomDate date = new CustomDate(1, 1, 0);
        assertTrue(date.isLeapYear());
    }

    @Test
    public void DC_testLeapYearDecisionFalse() {
        CustomDate date = new CustomDate(1, 1, 1);
        assertFalse(date.isLeapYear());
    }

    // -----------------------------------------------------------------
    // MC/DC (Modified Condition/Decision Coverage)
    // -----------------------------------------------------------------

    @Test
    public void MCDC_testMCDC1_A0B0() {
        CustomDate date = new CustomDate(1, 1, 2023);
        assertEquals(1, date.getDay());
    }

    @Test(expected = IllegalArgumentException.class)
    public void MCDC_testMCDC1_A1B0() {
        new CustomDate(0, 1, 2023);
    }

    @Test(expected = IllegalArgumentException.class)
    public void MCDC_testMCDC1_A0B1() {
        new CustomDate(32, 1, 2023);
    }

    @Test
    public void MCDC_testMCDC2_A0B0() {
        CustomDate date = new CustomDate(1, 1, 2023);
        assertEquals(1, date.getMonth());
    }

    @Test(expected = IllegalArgumentException.class)
    public void MCDC_testMCDC2_A1B0() {
        new CustomDate(1, 0, 2023);
    }

    @Test(expected = IllegalArgumentException.class)
    public void MCDC_testMCDC2_A0B1() {
        new CustomDate(1, 13, 2023);
    }

    @Test
    public void MCDC_testMCDC3_A0() {
        CustomDate date = new CustomDate(1, 1, 0);
        assertEquals(0, date.getYear());
    }

    @Test(expected = IllegalArgumentException.class)
    public void MCDC_testMCDC3_A1() {
        new CustomDate(1, 1, -1);
    }

    @Test
    public void MCDC_testMCDC4_Year0() {
        CustomDate date = new CustomDate(1, 1, 0);
        assertTrue(date.isLeapYear());
    }

    @Test
    public void MCDC_testMCDC4_Year1() {
        CustomDate date = new CustomDate(1, 1, 1);
        assertFalse(date.isLeapYear());
    }

    @Test
    public void MCDC_testMCDC4_Year4() {
        CustomDate date = new CustomDate(1, 1, 4);
        assertTrue(date.isLeapYear());
    }

    @Test
    public void MCDC_testMCDC4_Year100() {
        CustomDate date = new CustomDate(1, 1, 100);
        assertFalse(date.isLeapYear());
    }

    @Test
    public void MCDC_testMCDC4_Year400() {
        CustomDate date = new CustomDate(1, 1, 400);
        assertTrue(date.isLeapYear());
    }

    @Test
    public void MCDC_testMCDC4_Year2024() {
        CustomDate date = new CustomDate(1, 1, 2024);
        assertTrue(date.isLeapYear());
    }

    @Test
    public void MCDC_testMCDC4_Year1900() {
        CustomDate date = new CustomDate(1, 1, 1900);
        assertFalse(date.isLeapYear());
    }

    @Test
    public void MCDC_testMCDC4_Year2000() {
        CustomDate date = new CustomDate(1, 1, 2000);
        assertTrue(date.isLeapYear());
    }

    // -----------------------------------------------------------------
    // Additional Coverage Tests (Utility Methods)
    // -----------------------------------------------------------------

    @Test
    public void UTIL_testGetDay() {
        CustomDate date = new CustomDate(15, 6, 2023);
        assertEquals(15, date.getDay());
    }

    @Test
    public void UTIL_testGetMonth() {
        CustomDate date = new CustomDate(15, 6, 2023);
        assertEquals(6, date.getMonth());
    }

    @Test
    public void UTIL_testGetYear() {
        CustomDate date = new CustomDate(15, 6, 2023);
        assertEquals(2023, date.getYear());
    }

    @Test
    public void UTIL_testToString() {
        CustomDate date = new CustomDate(5, 3, 2023);
        assertEquals("05/03/2023", date.toString());
    }

    @Test
    public void UTIL_testToStringWithPadding() {
        CustomDate date = new CustomDate(1, 1, 1);
        assertEquals("01/01/0001", date.toString());
    }
}