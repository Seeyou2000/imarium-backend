package com.imarium.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Artwork {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(nullable = false)
    private String material;

    @Column(nullable = false)
    private String size;

    @Column(nullable = false)
    private Integer year;

    @Column(nullable = false)
    private Boolean isSaved;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id", nullable = false)
    private Artist artist;

    // Artwork와 Image 간의 1:N 관계 설정
    @OneToMany(mappedBy = "artwork", cascade = CascadeType.ALL)
    private List<Image> images;

    @OneToMany(mappedBy = "artwork", cascade = CascadeType.ALL)
    private List<ArtworkPrice> artworkPrices;
}
