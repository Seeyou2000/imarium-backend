package com.imarium.repository;

import com.imarium.entity.Artwork;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArtworkRepository extends JpaRepository<Artwork, Long> {
    // 특정 작가의 작품 찾기
    List<Artwork> findByArtistId(Long artistId);
}

