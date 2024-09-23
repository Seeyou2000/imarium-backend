package com.imarium.dto;

import lombok.Data;

@Data
public class CarouselDto {
    private String imageUrl;
    private String linkUrl;

    public CarouselDto(String imageUrl, String linkUrl) {
        this.imageUrl = imageUrl;
        this.linkUrl = linkUrl;
    }
}

