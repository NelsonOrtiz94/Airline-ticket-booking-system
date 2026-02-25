package org.example.domain.service;

import org.example.domain.valueobject.Price;
import org.example.domain.model.enums.TicketClass;

import java.math.BigDecimal;

/**
 * Servicio de dominio para cálculo de precios
 */
public class PriceCalculationService {

    private static final BigDecimal BUSINESS_CLASS_MULTIPLIER = BigDecimal.valueOf(2.5);
    private static final BigDecimal FIRST_CLASS_MULTIPLIER = BigDecimal.valueOf(4.0);

    /**
     * Calcula el precio final según la clase del ticket
     */
    public Price calculatePrice(Price basePrice, TicketClass ticketClass) {
        return switch (ticketClass) {
            case ECONOMY -> basePrice;
            case PREMIUM_ECONOMY -> new Price(
                basePrice.amount().multiply(BigDecimal.valueOf(1.5)),
                basePrice.currency()
            );
            case BUSINESS -> new Price(
                basePrice.amount().multiply(BUSINESS_CLASS_MULTIPLIER),
                basePrice.currency()
            );
            case FIRST_CLASS -> new Price(
                basePrice.amount().multiply(FIRST_CLASS_MULTIPLIER),
                basePrice.currency()
            );
        };
    }

    /**
     * Calcula descuento por múltiples tickets
     */
    public Price applyGroupDiscount(Price totalPrice, int numberOfTickets) {
        if (numberOfTickets >= 5) {
            BigDecimal discount = totalPrice.amount().multiply(BigDecimal.valueOf(0.10));
            return new Price(
                totalPrice.amount().subtract(discount),
                totalPrice.currency()
            );
        }
        return totalPrice;
    }
}

