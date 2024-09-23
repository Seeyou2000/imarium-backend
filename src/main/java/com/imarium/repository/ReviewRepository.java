package com.imarium.repository;

import com.imarium.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    // 사용자의 리뷰 찾기
    List<Review> findByUserId(Long userId);

    // 추천 리뷰 찾기
    List<Review> findByIsRecommendedTrue();
}
