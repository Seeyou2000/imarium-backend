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
@RequestMapping("/api/main")
public class MainController {

    private final UserService userService;
    private final SearchHistoryService searchHistoryService;
    private final CarouselService carouselService;
    private final ReviewService reviewService;
    private final ColService columnService;

    public MainController(UserService userService,
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

    // 4. 리뷰 API
    @Operation(summary = "리뷰 목록", description = "추천 리뷰 목록을 반환합니다. 로그인 여부에 따라 다르게 반환됩니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "리뷰 목록 반환")
    })
    @GetMapping("/reviews")
    public ResponseEntity<List<ReviewDto>> getReviews(Authentication authentication) {
        List<Review> reviews;
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            reviews = reviewService.findByIsRecommendedTrue().stream().limit(25).collect(Collectors.toList());
        } else {
            reviews = reviewService.findPopularOrLatestReviews(25);
        }
        List<ReviewDto> reviewDtos = reviews.stream()
                .map(review -> new ReviewDto(review.getId(), review.getContent(), review.getLikes(), review.getIsRecommended()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(reviewDtos);
    }

    // 5. 칼럼 API
    @Operation(summary = "칼럼 목록", description = "추천 칼럼 목록을 반환합니다. 로그인 여부에 따라 다르게 반환됩니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "칼럼 목록 반환")
    })
    @GetMapping("/columns")
    public ResponseEntity<List<ColDto>> getColumns(Authentication authentication) {
        List<Col> columns;
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            columns = columnService.findByIsRecommendedTrue().stream().limit(25).collect(Collectors.toList());
        } else {
            columns = columnService.findPopularOrLatestColumns(25);
        }
        List<ColDto> columnDtos = columns.stream()
                .map(column -> new ColDto(column.getId(), column.getTitle(), column.getContent(), Collections.singletonList(column.getImageUrl()), column.getIsRecommended()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(columnDtos);
    }
}