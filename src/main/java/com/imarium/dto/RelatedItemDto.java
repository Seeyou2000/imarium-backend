package com.imarium.dto;

import lombok.Data;

@Data
public class RelatedItemDto {
    private String title;
    private String description;
    private Boolean isSaved;
    private String imageUrl;

    public RelatedItemDto(String title, String description, Boolean isSaved, String imageUrl) {
        this.title = title;
        this.description = description;
        this.isSaved = isSaved;
        this.imageUrl = imageUrl;
    }
}

