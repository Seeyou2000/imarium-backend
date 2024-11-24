package com.imarium.dto;

import lombok.Data;

@Data
public class ReviewDto {
    private Long id;
    private Long artist_id;
    private String content;
    private Integer likes;
    private Boolean isRecommended;

    public ReviewDto(Long id, Long artist_id, String content, Integer likes, Boolean isRecommended) {
        this.id = id;
        this.artist_id = artist_id;
        this.content = content;
        this.likes = likes;
        this.isRecommended = isRecommended;
    }
}

