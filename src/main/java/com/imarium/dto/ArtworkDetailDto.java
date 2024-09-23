package com.imarium.dto;

import com.imarium.entity.Artist;
import lombok.Data;

@Data
public class ArtworkDetailDto {
    private String title;
    private String material;
    private String size;
    private Integer year;
    private String artist;
    private String description;
    private Boolean isSaved;

    public ArtworkDetailDto(String title, String material, String size, Integer year, String artist, String description, Boolean isSaved) {
        this.title = title;
        this.material = material;
        this.size = size;
        this.year = year;
        this.artist = artist;
        this.description = description;
        this.isSaved = isSaved;
    }
}
