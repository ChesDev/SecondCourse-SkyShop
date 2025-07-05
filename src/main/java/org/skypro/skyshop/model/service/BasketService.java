package org.skypro.skyshop.model.service;

import org.skypro.skyshop.model.exceptions.NoSuchProductException;
import org.skypro.skyshop.model.basket.BasketItem;
import org.skypro.skyshop.model.basket.ProductBasket;
import org.skypro.skyshop.model.basket.UserBasket;
import org.skypro.skyshop.model.product.Product;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BasketService {
    private final ProductBasket productBasket;
    private final StorageService storageService;

    public BasketService(ProductBasket productBasket, StorageService storageService) {
        this.productBasket = productBasket;
        this.storageService = storageService;
    }

    public void addProductToBasket(UUID id) {
        Optional<Product> productOptional = storageService.getProductById(id);
        if (!productOptional.isPresent()) {
            throw new NoSuchProductException();
        }
        productBasket.add(id);
    }

    public UserBasket getUserBasket() {
        Map<UUID, Integer> basket = productBasket.getBasket();
        List<BasketItem> basketItems = basket.entrySet().stream()
                .map(entry -> {
                    Product product = storageService.getProductById(entry.getKey())
                            .orElseThrow(() -> new NoSuchProductException());
                    return new BasketItem(product, entry.getValue());
                })
                .collect(Collectors.toList());
        return new UserBasket(basketItems);
    }
}