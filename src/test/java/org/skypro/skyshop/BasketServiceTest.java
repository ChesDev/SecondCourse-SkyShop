package org.skypro.skyshop;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skypro.skyshop.model.basket.BasketItem;
import org.skypro.skyshop.model.basket.ProductBasket;
import org.skypro.skyshop.model.basket.UserBasket;
import org.skypro.skyshop.model.exceptions.NoSuchProductException;
import org.skypro.skyshop.model.product.DiscountedProduct;
import org.skypro.skyshop.model.product.Product;
import org.skypro.skyshop.model.product.SimpleProduct;
import org.skypro.skyshop.service.BasketService;
import org.skypro.skyshop.service.StorageService;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BasketServiceTest {

    @Mock
    private ProductBasket productBasket;

    @Mock
    private StorageService storageService;

    @InjectMocks
    private BasketService basketService;

    private Product createTestProduct(String name, int price, UUID id) {
        return new SimpleProduct(name, price, id);
    }

    // Добавление несуществующего товара в корзину
    @Test
    void addProductToBasket_WhenProductDoesNotExist_ShouldThrowException() {
        // Given
        UUID nonExistentId = UUID.randomUUID();
        when(storageService.getProductById(nonExistentId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NoSuchProductException.class, () -> {
            basketService.addProductToBasket(nonExistentId);
        });

        verify(storageService).getProductById(nonExistentId);
        verifyNoInteractions(productBasket);
    }

    // Добавление существующего товара в корзину
    @Test
    void addProductToBasket_WhenProductExists_ShouldAddToBasket() {
        // Given
        UUID productId = UUID.randomUUID();
        Product product = createTestProduct("Хомяк", 100, productId);
        when(storageService.getProductById(productId)).thenReturn(Optional.of(product));

        // When
        basketService.addProductToBasket(productId);

        // Then
        verify(storageService).getProductById(productId);
        verify(productBasket).add(productId);
    }

    // Получение пустой корзины
    @Test
    void getUserBasket_WhenBasketIsEmpty_ShouldReturnEmptyBasket() {
        // Given
        when(productBasket.getBasket()).thenReturn(Collections.emptyMap());

        // When
        UserBasket result = basketService.getUserBasket();

        // Then
        assertTrue(result.items().isEmpty());
        assertEquals(0.0, result.total());
        verify(productBasket).getBasket();
        verifyNoInteractions(storageService);
    }

    // Получение корзины с товарами
    @Test
    void getUserBasket_WhenBasketHasProducts_ShouldReturnCorrectBasket() {
        // Given
        UUID productId1 = UUID.randomUUID();
        UUID productId2 = UUID.randomUUID();
        Product product1 = createTestProduct("Хомяк", 100, productId1);
        Product product2 = createTestProduct("Хлеб", 50, productId2);

        Map<UUID, Integer> basketContent = new HashMap<>();
        basketContent.put(productId1, 2);
        basketContent.put(productId2, 3);

        when(productBasket.getBasket()).thenReturn(basketContent);
        when(storageService.getProductById(productId1)).thenReturn(Optional.of(product1));
        when(storageService.getProductById(productId2)).thenReturn(Optional.of(product2));

        // When
        UserBasket result = basketService.getUserBasket();

        // Then
        assertEquals(2, result.items().size());
        assertEquals(350.0, result.total()); // 2*100 + 3*50 = 350

        assertTrue(result.items().contains(new BasketItem(product1, 2)));
        assertTrue(result.items().contains(new BasketItem(product2, 3)));

        verify(productBasket).getBasket();
        verify(storageService).getProductById(productId1);
        verify(storageService).getProductById(productId2);
    }

    // Получение корзины с товаром, который удален из хранилища
    @Test
    void getUserBasket_WhenProductInBasketNoLongerExists_ShouldThrowException() {
        // Given
        UUID removedProductId = UUID.randomUUID();
        Map<UUID, Integer> basketContent = Collections.singletonMap(removedProductId, 1);

        when(productBasket.getBasket()).thenReturn(basketContent);
        when(storageService.getProductById(removedProductId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NoSuchProductException.class, () -> {
            basketService.getUserBasket();
        });

        verify(productBasket).getBasket();
        verify(storageService).getProductById(removedProductId);
    }

    // Расчет общей суммы корзины
    @Test
    void getUserBasket_ShouldCalculateTotalCorrectly() {
        // Given
        UUID productId = UUID.randomUUID();
        Product product = createTestProduct("Вино", 700, productId);
        int quantity = 2;

        when(productBasket.getBasket()).thenReturn(Collections.singletonMap(productId, quantity));
        when(storageService.getProductById(productId)).thenReturn(Optional.of(product));

        // When
        UserBasket result = basketService.getUserBasket();

        // Then
        assertEquals(1400.0, result.total()); // 700 * 2 = 1400
    }

    // Расчет суммы для товара со скидкой
    @Test
    void getUserBasket_WithDiscountedProduct_ShouldCalculateTotalWithDiscount() {
        // Given
        UUID productId = UUID.randomUUID();
        Product product = new DiscountedProduct("Хлеб", 50, 10, productId); // Цена 50, скидка 10%
        int quantity = 4;

        when(productBasket.getBasket()).thenReturn(Collections.singletonMap(productId, quantity));
        when(storageService.getProductById(productId)).thenReturn(Optional.of(product));

        // When
        UserBasket result = basketService.getUserBasket();

        // Then
        assertEquals(180.0, result.total()); // 50 * 0.9 * 4 = 180
    }
}