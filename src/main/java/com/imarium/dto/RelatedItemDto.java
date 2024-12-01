package com.imarium.dto;

import lombok.Data;

@Data
public class RelatedItemDto {
    private Long id;
    private String title;
    private String description;
    private Boolean isSaved;
    private String imageUrl;

    public RelatedItemDto(Long id, String title, String description, Boolean isSaved, String imageUrl) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.isSaved = isSaved;
        this.imageUrl = imageUrl;
    }
}

