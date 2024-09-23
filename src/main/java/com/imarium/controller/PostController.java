package com.imarium.controller;

import com.imarium.dto.CarouselDto;
import com.imarium.dto.ColDto;
import com.imarium.dto.ReviewDto;
import com.imarium.entity.*;
import com.imarium.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/post")
public class PostController {

    private final UserService userService;
    private final SearchHistoryService searchHistoryService;
    private final CarouselService carouselService;
    private final ReviewService reviewService;
    private final ColService columnService;

    public PostController(UserService userService,
                          SearchHistoryService searchHistoryService,
                          CarouselService carouselService,
                          ReviewService reviewService,
                          ColService columnService) {
        this.userService = userService;
        this.searchHistoryService = searchHistoryService;
        this.carouselService = carouselService;
        this.reviewService = reviewService;
        this.columnService = columnService;
    }

    // 1. 로그인 여부 확인 API
    @Operation(summary = "로그인 여부 확인", description = "현재 사용자가 로그인했는지 확인합니다.")
    @GetMapping("/login-status")
    public ResponseEntity<Boolean> checkLoginStatus(Authentication authentication) {
        boolean isLoggedIn = authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken);
        return ResponseEntity.ok(isLoggedIn);
    }

    // 2. 최근 검색어 API (로그인 시)
    @Operation(summary = "최근 검색어", description = "로그인한 사용자의 최근 검색어를 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "검색어 목록 반환"),
            @ApiResponse(responseCode = "401", description = "로그인하지 않은 경우")
    })
    @GetMapping("/search-history")
    public ResponseEntity<List<String>> getRecentSearchHistory(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated() || (authentication instanceof AnonymousAuthenticationToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String email = authentication.getName();
        User user = userService.findByEmail(email);
        List<SearchHistory> histories = searchHistoryService.findByUserId(user.getId());
        List<String> keywords = histories.stream()
                .map(SearchHistory::getKeyword)
                .collect(Collectors.toList());
        return ResponseEntity.ok(keywords);
    }

    // 3. 카루셀(슬라이드) 화면 API(이미지, 링크)
    @Operation(summary = "카루셀 정보", description = "슬라이드로 보여줄 카루셀 이미지와 링크를 반환합니다.")
    @GetMapping("/carousel")
    public ResponseEntity<List<CarouselDto>> getCarousel() {
        List<Carousel> carousels = carouselService.findAll();
        List<CarouselDto> carouselDtos = carousels.stream()
                .map(carousel -> new CarouselDto(carousel.getImageUrl(), carousel.getLinkUrl()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(carouselDtos);
    }

    // 4. 리뷰 API (최신순, 추천순)
    @Operation(summary = "리뷰 목록", description = "최신 리뷰 또는 추천 리뷰를 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "리뷰 목록 반환")
    })
    @GetMapping("/reviews")
    public ResponseEntity<List<ReviewDto>> getReviews(@RequestParam(required = false) String sort) {
        List<Review> reviews;
        if ("recommended".equalsIgnoreCase(sort)) {
            reviews = reviewService.findByIsRecommendedTrue();
        } else {
            reviews = reviewService.findPopularOrLatestReviews(25);
        }
        List<ReviewDto> reviewDtos = reviews.stream()
                .map(review -> new ReviewDto(review.getId(), review.getContent(), review.getLikes(), review.getIsRecommended()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(reviewDtos);
    }

    // 5. 리뷰 정보 API
    @Operation(summary = "리뷰 정보", description = "특정 리뷰의 상세 정보를 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "리뷰 정보 반환"),
            @ApiResponse(responseCode = "404", description = "리뷰가 존재하지 않는 경우")
    })
    @GetMapping("/review/{reviewId}")
    public ResponseEntity<ReviewDto> getReviewInfo(@PathVariable Long reviewId) {
        Review review = reviewService.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));
        ReviewDto dto = new ReviewDto(review.getId(), review.getContent(), review.getLikes(), review.getIsRecommended());
        return ResponseEntity.ok(dto);
    }

    // 6. 칼럼 API (최신순, 추천순)
    @Operation(summary = "칼럼 목록", description = "최신 칼럼 또는 추천 칼럼을 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "칼럼 목록 반환")
    })
    @GetMapping("/columns")
    public ResponseEntity<List<ColDto>> getColumns(@RequestParam(required = false) String sort) {
        List<Col> columns;
        if ("recommended".equalsIgnoreCase(sort)) {
            columns = columnService.findByIsRecommendedTrue();
        } else {
            columns = columnService.findPopularOrLatestColumns(25);
        }
        List<ColDto> columnDtos = columns.stream()
                .map(column -> new ColDto(column.getId(), column.getTitle(), column.getContent(), Collections.singletonList(column.getImageUrl()), column.getIsRecommended()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(columnDtos);
    }

    // 7. 칼럼 정보 API
    @Operation(summary = "칼럼 정보", description = "특정 칼럼의 상세 정보를 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "칼럼 정보 반환"),
            @ApiResponse(responseCode = "404", description = "칼럼이 존재하지 않는 경우")
    })
    @GetMapping("/column/{columnId}")
    public ResponseEntity<ColDto> getColumnInfo(@PathVariable Long columnId) {
        Col column = columnService.findById(columnId)
                .orElseThrow(() -> new RuntimeException("Column not found"));
        ColDto dto = new ColDto(column.getId(), column.getTitle(), column.getContent(), Collections.singletonList(column.getImageUrl()), column.getIsRecommended());
        return ResponseEntity.ok(dto);
    }
}

