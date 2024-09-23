package com.imarium.service;

import com.imarium.entity.Review;
import com.imarium.repository.ReviewRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public Review createReview(Review review) {
        return reviewRepository.save(review);
    }

    public List<Review> findByUserId(Long userId) {
        return reviewRepository.findByUserId(userId);
    }

    public Optional<Review> findById(Long reviewId) {
        return reviewRepository.findById(reviewId);
    }

    public List<Review> findByIsRecommendedTrue() {
        return reviewRepository.findByIsRecommendedTrue();
    }

    public List<Review> findPopularOrLatestReviews(int limit) {
        // 예시: 최신순으로 정렬하여 제한된 수만큼 반환
        return reviewRepository.findAll(Sort.by(Sort.Direction.DESC, "id")).stream().limit(limit).collect(Collectors.toList());
    }
}

