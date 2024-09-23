package com.imarium.repository;

import com.imarium.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    // 특정 작품의 이미지 찾기
    List<Image> findByArtworkId(Long artworkId);

    // 특정 전시의 이미지 찾기
    List<Image> findByExhibitionId(Long exhibitionId);

    // 특정 칼럼의 이미지 찾기
    List<Image> findByColumnId(Long columnId);
}

