package com.imarium.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Artist extends User {

    @Column(nullable = false)
    private String bannerUrl;

    @Column(nullable = false)
    private Integer likes;

    @Column(nullable = false)
    private String tags;

    // 연관 관계 설정
    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL)
    private List<Artwork> artworks;

    @OneToMany(mappedBy = "artist", cascade = CascadeType.ALL)
    private List<Exhibition> exhibitions;
}

