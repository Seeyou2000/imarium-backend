package com.imarium.controller;

import com.imarium.dto.CarouselDto;
import com.imarium.dto.RelatedItemDto;
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

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/showroom")
public class ShowroomController {

    private final UserService userService;
    private final SearchHistoryService searchHistoryService;
    private final CarouselService carouselService;
    private final ExhibitionService exhibitionService;
    private final ArtworkService artworkService;
    private final TagService tagService;

    public ShowroomController(UserService userService,
                              SearchHistoryService searchHistoryService,
                              CarouselService carouselService,
                              ExhibitionService exhibitionService,
                              ArtworkService artworkService,
                              TagService tagService) {
        this.userService = userService;
        this.searchHistoryService = searchHistoryService;
        this.carouselService = carouselService;
        this.exhibitionService = exhibitionService;
        this.artworkService = artworkService;
        this.tagService = tagService;
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

    // 4. 해당하는 작품 혹은 전시들 API
    @Operation(summary = "관련 작품 및 전시", description = "주어진 태그에 해당하는 작품이나 전시를 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "관련 작품 및 전시 목록 반환")
    })
    @GetMapping("/related")
    public ResponseEntity<List<RelatedItemDto>> getRelatedItems(@RequestParam(required = false) String tag) {
        List<RelatedItemDto> relatedItems;
        if (tag != null && !tag.isEmpty()) {
            relatedItems = tagService.findByTag(tag).stream()
                    .map(item -> new RelatedItemDto(item.getTitle(), item.getDescription(), item.getIsSaved(), item.getImageUrl()))
                    .collect(Collectors.toList());
        } else {
            // 기본적으로 모든 작품과 전시를 반환
            List<Artwork> artworks = artworkService.findAll();
            List<Exhibition> exhibitions = exhibitionService.findAll();
            relatedItems = Stream.concat(
                    artworks.stream().map(artwork -> new RelatedItemDto(artwork.getTitle(), artwork.getDescription(), artwork.getIsSaved(), artwork.getImages().get(0).getImageUrl())),
                    exhibitions.stream().map(exhibition -> new RelatedItemDto(exhibition.getTitle(), exhibition.getDescription(), exhibition.getIsSaved(), exhibition.getImages().get(0).getImageUrl()))
            ).collect(Collectors.toList());
        }
        return ResponseEntity.ok(relatedItems);
    }

    // 5. 태그 검색 API
    @Operation(summary = "태그 검색", description = "주어진 태그에 해당하는 작품이나 전시를 검색합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "검색된 작품 및 전시 목록 반환")
    })
    @GetMapping("/search/tags")
    public ResponseEntity<List<RelatedItemDto>> searchByTag(@RequestParam String tag) {
        List<RelatedItemDto> relatedItems = tagService.findByTag(tag).stream()
                .map(item -> new RelatedItemDto(item.getTitle(), item.getDescription(), item.getIsSaved(), item.getImageUrl()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(relatedItems);
    }
}
