package org.skypro.skyshop;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skypro.skyshop.model.article.Article;
import org.skypro.skyshop.model.product.Product;
import org.skypro.skyshop.model.product.SimpleProduct;
import org.skypro.skyshop.model.search.SearchResult;
import org.skypro.skyshop.model.service.SearchService;
import org.skypro.skyshop.model.service.StorageService;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SearchServiceTest {

    @Mock
    private StorageService storageService;

    @InjectMocks
    private SearchService searchService;

    private Product createTestProduct(String name, UUID id) {
        return new SimpleProduct(name, 100, id);
    }

    private Article createTestArticle(String title, UUID id) {
        return new Article(title, "Test content", id);
    }

    // Поиск в случае отсутствия объектов в StorageService
    @Test
    void search_WhenStorageIsEmpty_ReturnsEmptyList() {
        // Given
        when(storageService.getAllSearchables()).thenReturn(Collections.emptyList());

        // When
        List<SearchResult> results = searchService.search("хомяк");

        // Then
        assertTrue(results.isEmpty());
        verify(storageService).getAllSearchables();
    }

    // Поиск, когда объекты есть, но нет подходящего
    @Test
    void search_WhenNoMatchingItems_ReturnsEmptyList() {
        // Given
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        Product product = createTestProduct("Хлеб", id1);
        Article article = createTestArticle("Программирование на Java", id2);

        when(storageService.getAllSearchables()).thenReturn(Arrays.asList(product, article));

        // When
        List<SearchResult> results = searchService.search("хомяк");

        // Then
        assertTrue(results.isEmpty());
    }

    // Сценарий 3: Поиск, когда есть подходящий продукт
    @Test
    void search_WhenMatchingProductExists_ReturnsProductResult() {
        // Given
        UUID id = UUID.randomUUID();
        Product product = createTestProduct("Хомяк", id);

        when(storageService.getAllSearchables()).thenReturn(Collections.singletonList(product));

        // When
        List<SearchResult> results = searchService.search("хомяк");

        // Then
        assertEquals(1, results.size());
        SearchResult result = results.get(0);
        assertEquals("Хомяк", result.getName());
        assertEquals(id, result.getId());
        assertEquals("PRODUCT", result.getContentType());
    }

    // Сценарий 4: Поиск, когда есть подходящая статья
    @Test
    void search_WhenMatchingArticleExists_ReturnsArticleResult() {
        // Given
        UUID id = UUID.randomUUID();
        Article article = createTestArticle("Хомяки и их привычки", id);

        when(storageService.getAllSearchables()).thenReturn(Collections.singletonList(article));

        // When
        List<SearchResult> results = searchService.search("хомяк");

        // Then
        assertEquals(1, results.size());
        SearchResult result = results.get(0);
        assertEquals("Хомяки и их привычки", result.getName());
        assertEquals(id, result.getId());
        assertEquals("ARTICLE", result.getContentType());
    }

    // Поиск с пустым запросом
    @Test
    void search_WithEmptyQuery_ReturnsEmptyList() {
        // When
        List<SearchResult> results = searchService.search("");

        // Then
        assertTrue(results.isEmpty());
    }


    // Поиск возвращает несколько результатов
    @Test
    void search_WhenMultipleMatchesExist_ReturnsAllResults() {
        // Given
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        UUID id3 = UUID.randomUUID();
        Product product1 = createTestProduct("Хомяк", id1);
        Article article1 = createTestArticle("Хомяки в природе", id2);
        Product product2 = createTestProduct("Корм для хомяков", id3);

        when(storageService.getAllSearchables()).thenReturn(Arrays.asList(product1, article1, product2));

        // When
        List<SearchResult> results = searchService.search("хомяк");

        // Then
        assertEquals(3, results.size());
        assertTrue(results.stream().allMatch(r ->
                r.getName().toLowerCase().contains("хомяк")));
    }

    // Проверка корректности типа возвращаемого объекта
    @Test
    void search_ResultContainsCorrectType() {
        // Given
        UUID productId = UUID.randomUUID();
        UUID articleId = UUID.randomUUID();
        Product product = createTestProduct("Хомяк", productId);
        Article article = createTestArticle("Хомяки", articleId);

        when(storageService.getAllSearchables()).thenReturn(Arrays.asList(product, article));

        // When
        List<SearchResult> results = searchService.search("хомяк");

        // Then
        assertEquals(2, results.size());
        assertEquals("PRODUCT", results.get(0).getContentType());
        assertEquals("ARTICLE", results.get(1).getContentType());
    }
}