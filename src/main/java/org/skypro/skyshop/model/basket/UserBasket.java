package org.skypro.skyshop.model.basket;

import java.util.List;


public record UserBasket(List<BasketItem> items, double total) {
    public UserBasket(List<BasketItem> items) {
        this(items, calculateTotal(items));
    }

    private static double calculateTotal(List<BasketItem> items) {
        return items.stream()
                .mapToDouble(item -> item.product().getPrice() * item.count())
                .sum();
    }
}
