package org.skypro.skyshop.model.search;

import java.util.UUID;

public interface Searchable {

    UUID getId();

    String getSearchTerm();

    String getContentType();


    default String getStringRepresentation() {
        return new StringBuilder()
                .append(getSearchTerm())
                .append(" - ")
                .append(getContentType())
                .toString();
    }

}
