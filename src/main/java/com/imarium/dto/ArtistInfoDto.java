package com.imarium.dto;

import lombok.Data;

@Data
public class ArtistInfoDto {
    private Long artistId;
    private String Name;
    private String Tag;
    private int likes;
    private String bannerUrl;

    public ArtistInfoDto(Long artistId, String Name, String Tag, int likes, String bannerUrl) {
        this.artistId = artistId;
        this.Name = Name;
        this.Tag = Tag;
        this.likes = likes;
        this.bannerUrl = bannerUrl;
    }
}
