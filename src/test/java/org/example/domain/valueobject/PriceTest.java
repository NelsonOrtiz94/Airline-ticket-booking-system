package org.example.domain.valueobject;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para Price value object
 */
class PriceTest {

    @Test
    void testPrice_WhenValid_ShouldCreate() {
        Price price = new Price(BigDecimal.valueOf(100), "USD");
        assertEquals(BigDecimal.valueOf(100), price.amount());
        assertEquals("USD", price.currency());
    }

    @Test
    void testPrice_WhenAmountOnly_ShouldUseDefaultCurrency() {
        Price price = new Price(BigDecimal.valueOf(100));
        assertEquals("USD", price.currency());
    }

    @Test
    void testPrice_WhenNull_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new Price(null, "USD"));
    }

    @Test
    void testPrice_WhenNegative_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class,
            () -> new Price(BigDecimal.valueOf(-100), "USD"));
    }

    @Test
    void testPrice_WhenZero_ShouldCreate() {
        Price price = new Price(BigDecimal.ZERO, "USD");
        assertEquals(BigDecimal.ZERO, price.amount());
    }

    @Test
    void testIsGreaterThan_WhenGreater_ShouldReturnTrue() {
        Price price1 = new Price(BigDecimal.valueOf(200), "USD");
        Price price2 = new Price(BigDecimal.valueOf(100), "USD");
        assertTrue(price1.isGreaterThan(price2));
    }

    @Test
    void testIsGreaterThan_WhenNotGreater_ShouldReturnFalse() {
        Price price1 = new Price(BigDecimal.valueOf(100), "USD");
        Price price2 = new Price(BigDecimal.valueOf(200), "USD");
        assertFalse(price1.isGreaterThan(price2));
    }

    @Test
    void testIsGreaterThan_WhenDifferentCurrency_ShouldThrowException() {
        Price price1 = new Price(BigDecimal.valueOf(100), "USD");
        Price price2 = new Price(BigDecimal.valueOf(100), "EUR");
        assertThrows(IllegalArgumentException.class, () -> price1.isGreaterThan(price2));
    }

    @Test
    void testAdd_WhenSameCurrency_ShouldAddCorrectly() {
        Price price1 = new Price(BigDecimal.valueOf(100), "USD");
        Price price2 = new Price(BigDecimal.valueOf(50), "USD");
        Price result = price1.add(price2);

        assertEquals(BigDecimal.valueOf(150), result.amount());
        assertEquals("USD", result.currency());
    }

    @Test
    void testAdd_WhenDifferentCurrency_ShouldThrowException() {
        Price price1 = new Price(BigDecimal.valueOf(100), "USD");
        Price price2 = new Price(BigDecimal.valueOf(100), "EUR");
        assertThrows(IllegalArgumentException.class, () -> price1.add(price2));
    }
}

