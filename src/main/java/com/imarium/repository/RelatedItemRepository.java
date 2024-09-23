package com.imarium.repository;

import com.imarium.entity.RelatedItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RelatedItemRepository extends JpaRepository<RelatedItem, Long> {

    List<RelatedItem> findByTagId(Long tagId);  // 태그 ID로 연관 항목 검색

    List<RelatedItem> findByArtworkId(Long artworkId);  // 특정 작품과 연관된 태그들

    List<RelatedItem> findByExhibitionId(Long exhibitionId);  // 특정 전시와 연관된 태그들

    List<RelatedItem> findByColumnId(Long columnId);  // 특정 칼럼과 연관된 태그들
}

