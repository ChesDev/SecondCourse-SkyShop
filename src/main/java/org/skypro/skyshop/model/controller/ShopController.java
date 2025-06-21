package org.skypro.skyshop.model.controller;

import org.skypro.skyshop.model.article.Article;
import org.skypro.skyshop.model.product.Product;
import org.skypro.skyshop.model.search.SearchResult;
import org.skypro.skyshop.model.service.SearchService;
import org.skypro.skyshop.model.service.StorageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

@RestController
public class ShopController {
    private final StorageService storageService;
    private final SearchService searchService;

    public ShopController(StorageService storageService, SearchService searchService) {
        this.storageService = storageService;
        this.searchService = searchService;
    }

    @GetMapping
    public String hello() {
        return "Hello, world!";
    }

    @GetMapping("/search")
    public List<SearchResult> search(@RequestParam String pattern) {
        System.out.println("Поисковый запрос: " + pattern);
        return searchService.search(pattern);
    }

    @GetMapping("/products")
    public Collection<Product> getAllProducts() {
        System.out.println("Запрос к /products получен");
        return storageService.getAllProducts();
    }

    @GetMapping("/articles")
    public Collection<Article> getAllArticles() {
        System.out.println("Запрос к /articles получен");
        return storageService.getAllArticles();
    }

}
