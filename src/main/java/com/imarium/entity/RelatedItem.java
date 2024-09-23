package com.imarium.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class RelatedItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tag_id")
    private Tag tag;

    @ManyToOne
    @JoinColumn(name = "artwork_id", nullable = true)
    private Artwork artwork;

    @ManyToOne
    @JoinColumn(name = "exhibition_id", nullable = true)
    private Exhibition exhibition;

    @ManyToOne
    @JoinColumn(name = "column_id", nullable = true)
    private Col column;

    public String getTitle() {
        if (artwork != null) {
            return artwork.getTitle();
        } else if (exhibition != null) {
            return exhibition.getTitle();
        } else {
            return "Unknown Title";
        }
    }

    public String getDescription() {
        if (artwork != null) {
            return artwork.getDescription();
        } else if (exhibition != null) {
            return exhibition.getDescription();
        } else {
            return "No Description Available";
        }
    }

    public Boolean getIsSaved() {
        if (artwork != null) {
            return artwork.getIsSaved();
        } else if (exhibition != null) {
            return exhibition.getIsSaved();
        } else {
            return false; // 또는 null, 기본값으로 설정
        }
    }

    public String getImageUrl() {
        if (artwork != null && !artwork.getImages().isEmpty()) {
            return artwork.getImages().get(0).getImageUrl();
        } else if (exhibition != null && !exhibition.getImages().isEmpty()) {
            return exhibition.getImages().get(0).getImageUrl();
        } else {
            return "No Image Available";
        }
    }
}
