package com.imarium.dto;

import lombok.Data;

import java.util.List;

@Data
public class ColDto {
    private Long id;
    private String title;
    private String content;
    private List<String> imageUrls;
    private Boolean isRecommended;

    public ColDto(Long id, String title, String content, List<String> imageUrls, Boolean isRecommended) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.imageUrls = imageUrls;
        this.isRecommended = isRecommended;
    }
}

