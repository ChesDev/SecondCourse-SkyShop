package org.skypro.skyshop.model.basket;

import org.skypro.skyshop.model.product.Product;


public record BasketItem (Product product, int count) {

}
