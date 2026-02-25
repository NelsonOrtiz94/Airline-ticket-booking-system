package org.example.shared.util;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para DateUtil
 */
class DateUtilTest {

    @Test
    void testFormat_WhenValidDateTime_ShouldFormatCorrectly() {
        LocalDateTime dateTime = LocalDateTime.of(2026, 2, 13, 10, 30, 45);
        String formatted = DateUtil.format(dateTime);
        assertEquals("13/02/2026 10:30:45", formatted);
    }

    @Test
    void testFormat_WhenNull_ShouldReturnNull() {
        String formatted = DateUtil.format(null);
        assertNull(formatted);
    }

    @Test
    void testParse_WhenValidString_ShouldParseCorrectly() {
        String dateString = "13/02/2026 10:30:45";
        LocalDateTime parsed = DateUtil.parse(dateString);

        assertNotNull(parsed);
        assertEquals(2026, parsed.getYear());
        assertEquals(2, parsed.getMonthValue());
        assertEquals(13, parsed.getDayOfMonth());
        assertEquals(10, parsed.getHour());
        assertEquals(30, parsed.getMinute());
        assertEquals(45, parsed.getSecond());
    }

    @Test
    void testParse_WhenNull_ShouldReturnNull() {
        LocalDateTime parsed = DateUtil.parse(null);
        assertNull(parsed);
    }

    @Test
    void testFormatAndParse_ShouldBeReversible() {
        LocalDateTime original = LocalDateTime.of(2026, 3, 15, 14, 20, 30);
        String formatted = DateUtil.format(original);
        LocalDateTime parsed = DateUtil.parse(formatted);

        assertEquals(original, parsed);
    }
}

