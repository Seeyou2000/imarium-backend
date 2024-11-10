package com.imarium.dto;

import lombok.Data;

import java.util.List;

@Data
public class ArtworkDto {
    private Long id;
    private String title;
    private String description;
    private String material;
    private String size;
    private String category;
    private Integer year;
    private Boolean isSaved;
    private List<String> imageUrls;

    public ArtworkDto(Long id, String title, String description, String material, String size, String category, Integer year, Boolean isSaved, List<String> imageUrls) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.material = material;
        this.size = size;
        this.category = category;
        this.year = year;
        this.isSaved = isSaved;
        this.imageUrls = imageUrls;
    }
}

