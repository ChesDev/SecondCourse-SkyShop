package org.skypro.skyshop.service;

import org.skypro.skyshop.model.search.SearchResult;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchService {
    private final StorageService storageService;

    public SearchService(StorageService storageService) {
        this.storageService = storageService;
    }

    public List<SearchResult> search(String query) {
        return storageService.getAllSearchables().stream()
                .filter(item -> containsIgnoreCase(item.getSearchTerm(), query))
                .map(SearchResult::fromSearchable)
                .collect(Collectors.toList());
    }

    private boolean containsIgnoreCase(String source, String query) {
        if (query == null || query.isEmpty()) {
            return false;
        }
        return source.toLowerCase().contains(query.toLowerCase());
    }
}
