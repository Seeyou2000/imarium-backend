package com.imarium.controller;

import com.imarium.dto.ColDto;
import com.imarium.dto.EventDto;
import com.imarium.dto.ExhibitionDto;
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
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/view")
public class ViewController {

    private final UserService userService;
    private final SearchHistoryService searchHistoryService;
    private final ExhibitionService exhibitionService;
    private final ReviewService reviewService;
    private final ColService columnService;
    private final EventService eventService;

    public ViewController(UserService userService,
                          SearchHistoryService searchHistoryService,
                          ExhibitionService exhibitionService,
                          ReviewService reviewService,
                          ColService columnService,
                          EventService eventService) {
        this.userService = userService;
        this.searchHistoryService = searchHistoryService;
        this.exhibitionService = exhibitionService;
        this.reviewService = reviewService;
        this.columnService = columnService;
        this.eventService = eventService;
    }

    // 1. 로그인 여부 확인 API
    @Operation(summary = "로그인 여부 확인", description = "현재 사용자의 로그인 상태를 확인합니다.")
    @GetMapping("/login-status")
    public ResponseEntity<Boolean> checkLoginStatus(Authentication authentication) {
        boolean isLoggedIn = authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken);
        return ResponseEntity.ok(isLoggedIn);
    }

    // 2. 최근 검색어 API (로그인 시)
    @Operation(summary = "최근 검색어 조회", description = "로그인한 사용자의 최근 검색어를 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "검색어 목록 반환"),
            @ApiResponse(responseCode = "401", description = "사용자가 로그인하지 않은 경우")
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

    // 3. 전시 정보 API
    @Operation(summary = "전시 정보 조회", description = "주어진 ID로 전시 정보를 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "전시 정보 반환"),
            @ApiResponse(responseCode = "404", description = "전시를 찾을 수 없는 경우")
    })
    @GetMapping("/exhibition/{exhibitionId}")
    public ResponseEntity<ExhibitionDto> getExhibitionInfo(@PathVariable Long exhibitionId) {
        Exhibition exhibition = exhibitionService.getExhibitionById(exhibitionId);
        ExhibitionDto dto = new ExhibitionDto(
                exhibition.getId(),
                exhibition.getTitle(),
                exhibition.getDescription(),
                exhibition.getIsSaved(),
                exhibition.getImages().stream().map(Image::getImageUrl).collect(Collectors.toList())
        );
        return ResponseEntity.ok(dto);
    }

    // 4. 리뷰 정보 API
    @Operation(summary = "리뷰 정보 조회", description = "주어진 ID로 리뷰 정보를 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "리뷰 정보 반환"),
            @ApiResponse(responseCode = "404", description = "리뷰를 찾을 수 없는 경우")
    })
    @GetMapping("/review/{reviewId}")
    public ResponseEntity<ReviewDto> getReviewInfo(@PathVariable Long reviewId) {
        Review review = reviewService.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));
        ReviewDto dto = new ReviewDto(review.getId(), review.getContent(), review.getLikes(), review.getIsRecommended());
        return ResponseEntity.ok(dto);
    }

    // 5. 칼럼 정보 API
    @Operation(summary = "칼럼 정보 조회", description = "주어진 ID로 칼럼 정보를 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "칼럼 정보 반환"),
            @ApiResponse(responseCode = "404", description = "칼럼을 찾을 수 없는 경우")
    })
    @GetMapping("/column/{columnId}")
    public ResponseEntity<ColDto> getColumnInfo(@PathVariable Long columnId) {
        Col column = columnService.findById(columnId)
                .orElseThrow(() -> new RuntimeException("Column not found"));
        ColDto dto = new ColDto(column.getId(), column.getTitle(), column.getContent(), Collections.singletonList(column.getImageUrl()), column.getIsRecommended());
        return ResponseEntity.ok(dto);
    }

    // 6. 이벤트 정보 API
    @Operation(summary = "이벤트 정보 조회", description = "주어진 ID로 이벤트 정보를 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이벤트 정보 반환"),
            @ApiResponse(responseCode = "404", description = "이벤트를 찾을 수 없는 경우")
    })
    @GetMapping("/event/{eventId}")
    public ResponseEntity<EventDto> getEventInfo(@PathVariable Long eventId) {
        Optional<Event> optionalEvent = eventService.getEventById(eventId);

        if (optionalEvent.isPresent()) {
            Event event = optionalEvent.get();
            EventDto dto = new EventDto(event.getId(), event.getContent(), event.getDate());
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.notFound().build(); // 이벤트가 존재하지 않을 경우 404 Not Found
        }
    }
}

