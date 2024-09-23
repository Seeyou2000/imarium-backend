package com.imarium.dto;

import lombok.Data;

@Data
public class ArtistDto {
    private Long id;
    private String bannerUrl;
    private String artistName;
    private String artistTags;
    private Integer likes;

    public ArtistDto(Long id, String bannerUrl, String artistName, String artistTags, Integer likes) {
        this.id = id;
        this.bannerUrl = bannerUrl;
        this.artistName = artistName;
        this.artistTags = artistTags;
        this.likes = likes;
    }
}

