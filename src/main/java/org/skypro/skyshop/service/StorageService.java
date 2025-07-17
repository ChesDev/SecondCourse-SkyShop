package org.skypro.skyshop.service;

import org.skypro.skyshop.model.article.Article;
import org.skypro.skyshop.model.product.DiscountedProduct;
import org.skypro.skyshop.model.product.FixPriceProduct;
import org.skypro.skyshop.model.product.Product;
import org.skypro.skyshop.model.product.SimpleProduct;
import org.skypro.skyshop.model.search.Searchable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class StorageService {
    private final Map<UUID, Product> productStorage;
    private final Map<UUID, Article> articleStorage;

    public StorageService() {
        this.productStorage = new HashMap<>();
        this.articleStorage = new HashMap<>();
        initializeProducts();
        initializeArticles();
    }

    public void initializeProducts() {
        Product product1 = new SimpleProduct("Хомяк", 100, UUID.randomUUID());
        Product product2 = new DiscountedProduct("Хлеб", 50, 10, UUID.randomUUID());
        Product product3 = new FixPriceProduct("Колбаса", UUID.randomUUID());
        Product product4 = new SimpleProduct("Курица", 200, UUID.randomUUID());
        Product product5 = new SimpleProduct("Вино", 700, UUID.randomUUID());
        Product product6 = new SimpleProduct("Колбаса", 150, UUID.randomUUID());

        productStorage.put(product1.getId(), product1);
        productStorage.put(product2.getId(), product2);
        productStorage.put(product3.getId(), product3);
        productStorage.put(product4.getId(), product4);
        productStorage.put(product5.getId(), product5);
        productStorage.put(product6.getId(), product6);
    }

    public void initializeArticles() {
        Article article1 = new Article("Обучаем хомяка понимать ООП", "Объектно-ориентированное программирование (ООП) — это мощная парадигма, которая позволяет разработчикам создавать гибкие и масштабируемые программные решения. Но как объяснить концепции ООП так, чтобы они стали понятны даже хомяку? В этой статье мы проведем параллели между повадками хомяков и основными принципами ООП, чтобы сделать обучение увлекательным и доступным.", UUID.randomUUID());
        Article article2 = new Article("Маленькие мастера уюта. Хомяки", "Хомяки — это крошечные пушистики, чья жизнь выглядит как нескончаемый сериал о еде, сне и неожиданном беге в колесе. Эти грызуны, несмотря на свои скромные размеры, обладают харизмой, достойной блокбастера.", UUID.randomUUID());
        Article article3 = new Article("Карманные интроверты. Хомяки", "Эти ребята обожают уединение. Посади двух взрослых хомяков в одну клетку — и это уже не милый мультик, а триллер со смертельным исходом. Особенно, если это сирийские хомяки: у них личное пространство размером с Сахару. В общем, если вам нужен пушистый социопат, который смотрит на мир с подозрением, но всё равно милый — хомяк подойдёт идеально.", UUID.randomUUID());
        Article article4 = new Article("Генералы диванной обороны. Хомяки", "Хомяк — это животное, которое выглядит как комочек счастья, но ведёт себя как ветеран постапокалипсиса. Он всё прячет, всё боится, на всякий случай роет окопы в углу клетки и не доверяет даже своей поилке.", UUID.randomUUID());

        articleStorage.put(article1.getId(), article1);
        articleStorage.put(article2.getId(), article2);
        articleStorage.put(article3.getId(), article3);
        articleStorage.put(article4.getId(), article4);
    }

    public Collection<Product> getAllProducts() {
        return Collections.unmodifiableCollection(productStorage.values());
    }

    public Collection<Article> getAllArticles() {
        return Collections.unmodifiableCollection(articleStorage.values());
    }

    public Collection<Searchable> getAllSearchables() {
        return Stream.concat(
                productStorage.values().stream(),
                articleStorage.values().stream()
        ).collect(Collectors.toUnmodifiableList());
    }

    public Optional<Product> getProductById(UUID id) {
        return Optional.ofNullable(productStorage.get(id));
    }

}
