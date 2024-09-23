package com.imarium.repository;

import com.imarium.entity.Exhibition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExhibitionRepository extends JpaRepository<Exhibition, Long> {
    // 특정 작가의 전시 찾기
    List<Exhibition> findByArtistId(Long artistId);
}
