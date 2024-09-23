package com.imarium.dto;

import lombok.Data;

@Data
public class ReviewDto {
    private Long id;
    private String content;
    private Integer likes;
    private Boolean isRecommended;

    public ReviewDto(Long id, String content, Integer likes, Boolean isRecommended) {
        this.id = id;
        this.content = content;
        this.likes = likes;
        this.isRecommended = isRecommended;
    }
}

