package org.example.domain.service;

import org.example.domain.model.enums.TicketClass;
import org.example.domain.valueobject.Price;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para PriceCalculationService - 100% Coverage
 */
class PriceCalculationServiceTest {

    private PriceCalculationService priceCalculationService;
    private Price basePrice;

    @BeforeEach
    void setUp() {
        priceCalculationService = new PriceCalculationService();
        basePrice = new Price(BigDecimal.valueOf(100000), "USD");
    }

    @Test
    @DisplayName("Debe retornar el mismo precio para clase ECONOMY")
    void testCalculatePrice_Economy_ShouldReturnSamePrice() {
        Price result = priceCalculationService.calculatePrice(basePrice, TicketClass.ECONOMY);

        assertEquals(basePrice.amount(), result.amount());
        assertEquals(basePrice.currency(), result.currency());
    }

    @Test
    @DisplayName("Debe aplicar multiplicador 1.5x para PREMIUM_ECONOMY")
    void testCalculatePrice_PremiumEconomy_ShouldApplyMultiplier() {
        Price result = priceCalculationService.calculatePrice(basePrice, TicketClass.PREMIUM_ECONOMY);

        BigDecimal expected = BigDecimal.valueOf(150000);
        assertEquals(0, expected.compareTo(result.amount()));
    }

    @Test
    @DisplayName("Debe aplicar multiplicador 2.5x para BUSINESS")
    void testCalculatePrice_Business_ShouldApplyMultiplier() {
        Price result = priceCalculationService.calculatePrice(basePrice, TicketClass.BUSINESS);

        BigDecimal expected = BigDecimal.valueOf(250000);
        assertEquals(0, expected.compareTo(result.amount()));
    }

    @Test
    @DisplayName("Debe aplicar multiplicador 4.0x para FIRST_CLASS")
    void testCalculatePrice_FirstClass_ShouldApplyMultiplier() {
        Price result = priceCalculationService.calculatePrice(basePrice, TicketClass.FIRST_CLASS);

        BigDecimal expected = BigDecimal.valueOf(400000);
        assertEquals(0, expected.compareTo(result.amount()));
    }

    @Test
    @DisplayName("Debe aplicar descuento del 10% para 5 o más tickets")
    void testApplyGroupDiscount_FiveOrMoreTickets_ShouldApplyDiscount() {
        Price totalPrice = new Price(BigDecimal.valueOf(500000), "USD");

        Price result = priceCalculationService.applyGroupDiscount(totalPrice, 5);

        BigDecimal expected = BigDecimal.valueOf(450000);
        assertEquals(0, expected.compareTo(result.amount()));
    }

    @Test
    @DisplayName("No debe aplicar descuento para menos de 5 tickets")
    void testApplyGroupDiscount_LessThanFiveTickets_ShouldNotApplyDiscount() {
        Price totalPrice = new Price(BigDecimal.valueOf(400000), "USD");

        Price result = priceCalculationService.applyGroupDiscount(totalPrice, 4);

        assertEquals(totalPrice.amount(), result.amount());
    }

    @Test
    @DisplayName("Debe aplicar descuento para más de 5 tickets")
    void testApplyGroupDiscount_MoreThanFiveTickets_ShouldApplyDiscount() {
        Price totalPrice = new Price(BigDecimal.valueOf(1000000), "USD");

        Price result = priceCalculationService.applyGroupDiscount(totalPrice, 10);

        BigDecimal expected = BigDecimal.valueOf(900000);
        assertEquals(0, expected.compareTo(result.amount()));
    }

    @Test
    @DisplayName("Debe mantener la moneda al calcular precio")
    void testCalculatePrice_ShouldMaintainCurrency() {
        Price priceInCOP = new Price(BigDecimal.valueOf(250000), "COP");

        Price result = priceCalculationService.calculatePrice(priceInCOP, TicketClass.BUSINESS);

        assertEquals("COP", result.currency());
    }
}

