package com.imarium.dto;

import com.imarium.entity.Artwork;
import lombok.Data;

@Data
public class ArtworkPriceDto {
    private Integer minPrice;
    private Integer maxPrice;

    public ArtworkPriceDto(Integer minPrice, Integer maxPrice) {
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }
}
