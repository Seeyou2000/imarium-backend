package com.imarium.dto;

import lombok.Data;

@Data
public class PriceInfoDto {
    private int maxPrice;
    private int minPrice;
    private int artworkCount;

    public PriceInfoDto(int maxPrice, int minPrice, int artworkCount) {
        this.maxPrice = maxPrice;
        this.minPrice = minPrice;
        this.artworkCount = artworkCount;
    }
}
