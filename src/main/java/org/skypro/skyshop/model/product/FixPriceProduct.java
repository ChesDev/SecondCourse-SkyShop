package org.skypro.skyshop.model.product;

import java.util.UUID;

public class FixPriceProduct extends Product {
    private static final double FIX_PRICE = 100;
    private final UUID id;

    public FixPriceProduct(String name, UUID id) {
        super(name);
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    @Override
    public double getPrice() {
        return FIX_PRICE;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public String toString() {
        return new StringBuilder("Фикс. цена! ")
                .append(getName())
                .append(": ")
                .append(String.format("%.2f ₽", getPrice()))
                .toString();
    }
}