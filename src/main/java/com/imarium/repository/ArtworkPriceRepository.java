package com.imarium.repository;

import com.imarium.entity.ArtworkPrice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArtworkPriceRepository extends JpaRepository<ArtworkPrice, Long> {
    // 특정 작품의 가격 찾기
    List<ArtworkPrice> findByArtworkId(Long artworkId);
}
