package com.imarium.repository;

import com.imarium.entity.Col;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ColRepository extends JpaRepository<Col, Long> {
    // 사용자의 칼럼 찾기
    List<Col> findByUserId(Long userId);

    // 추천 칼럼 찾기
    List<Col> findByIsRecommendedTrue();
}

