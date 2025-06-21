package org.skypro.skyshop.model.article;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.skypro.skyshop.model.search.Searchable;

import java.util.Objects;
import java.util.UUID;

public class Article implements Searchable {
    private final String articleTitle;
    private final String articleText;
    private final UUID id;

    public Article(String articleTitle, String articleText, UUID id) {
        this.articleTitle = articleTitle;
        this.articleText = articleText;
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public String getArticleText() {
        return articleText;
    }

    @Override
    public String toString() {
        return new StringBuilder(articleTitle)
                .append(".\n")
                .append(articleText)
                .toString();
    }

    @JsonIgnore
    @Override
    public String getSearchTerm() {
        return getArticleTitle();
    }

    @JsonIgnore
    @Override
    public String getContentType() {
        return "ARTICLE";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return Objects.equals(articleTitle, article.articleTitle) && Objects.equals(articleText, article.articleText);
    }

    @Override
    public int hashCode() {
        return Objects.hash(articleTitle, articleText);
    }
}

