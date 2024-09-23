package com.imarium.dto;

import lombok.Data;

import java.util.List;

@Data
public class ExhibitionDto {
    private Long id;
    private String title;
    private String description;
    private Boolean isSaved;
    private List<String> imageUrls;

    public ExhibitionDto(Long id, String title, String description, Boolean isSaved, List<String> imageUrls) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.isSaved = isSaved;
        this.imageUrls = imageUrls;
    }
}

