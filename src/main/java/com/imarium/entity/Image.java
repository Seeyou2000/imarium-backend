package com.imarium.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String imageUrl;

    // 연관관계 설정: 작품에 속한 이미지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artwork_id")
    private Artwork artwork;

    // 연관관계 설정: 전시에 속한 이미지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exhibition_id")
    private Exhibition exhibition;

    // 연관관계 설정: 칼럼에 속한 이미지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "column_id")
    private Col column;
}
