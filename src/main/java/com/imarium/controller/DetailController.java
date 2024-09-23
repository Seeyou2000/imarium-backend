package com.imarium.controller;

import com.imarium.dto.ArtworkDetailDto;
import com.imarium.entity.Artwork;
import com.imarium.entity.SearchHistory;
import com.imarium.entity.User;
import com.imarium.entity.Image;
import com.imarium.service.ArtworkService;
import com.imarium.service.SearchHistoryService;
import com.imarium.service.UserService;
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

@RestController
@RequestMapping("/api/detail")
public class DetailController {

    private final UserService userService;
    private final SearchHistoryService searchHistoryService;
    private final ArtworkService artworkService;

    public DetailController(UserService userService,
                            SearchHistoryService searchHistoryService,
                            ArtworkService artworkService) {
        this.userService = userService;
        this.searchHistoryService = searchHistoryService;
        this.artworkService = artworkService;
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
            @ApiResponse(responseCode = "200", description = "최근 검색어 목록"),
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

    // 3. 작품 사진 API
    @Operation(summary = "작품 사진 조회", description = "특정 작품의 이미지 URL 목록을 반환합니다.")
    @GetMapping("/artwork/{artworkId}/images")
    public ResponseEntity<List<String>> getArtworkImages(@PathVariable Long artworkId) {
        Artwork artwork = artworkService.getArtworkById(artworkId);
        List<String> imageUrls = artwork.getImages().stream()
                .map(Image::getImageUrl)
                .collect(Collectors.toList());
        return ResponseEntity.ok(imageUrls);
    }

    // 4. 작품 정보 API
    @Operation(summary = "작품 정보 조회", description = "특정 작품의 상세 정보를 반환합니다.")
    @GetMapping("/artwork/{artworkId}")
    public ResponseEntity<ArtworkDetailDto> getArtworkDetail(@PathVariable Long artworkId) {
        Artwork artwork = artworkService.getArtworkById(artworkId);
        ArtworkDetailDto dto = new ArtworkDetailDto(
                artwork.getTitle(),
                artwork.getMaterial(),
                artwork.getSize(),
                artwork.getYear(),
                artwork.getArtist().getName(),
                artwork.getDescription(),
                artwork.getIsSaved()
        );
        return ResponseEntity.ok(dto);
    }
}
