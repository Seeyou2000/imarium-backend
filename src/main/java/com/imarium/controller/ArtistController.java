package com.imarium.controller;

import com.imarium.dto.ArtistDto;
import com.imarium.dto.ArtworkDto;
import com.imarium.dto.ArtworkPriceDto;
import com.imarium.dto.ExhibitionDto;
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

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/artist")
public class ArtistController {

    private final UserService userService;
    private final SearchHistoryService searchHistoryService;
    private final ArtistService artistService;
    private final ArtworkService artworkService;
    private final ExhibitionService exhibitionService;
    private final ArtworkPriceService artworkPriceService;

    public ArtistController(UserService userService,
                            SearchHistoryService searchHistoryService,
                            ArtistService artistService,
                            ArtworkService artworkService,
                            ExhibitionService exhibitionService,
                            ArtworkPriceService artworkPriceService) {
        this.userService = userService;
        this.searchHistoryService = searchHistoryService;
        this.artistService = artistService;
        this.artworkService = artworkService;
        this.exhibitionService = exhibitionService;
        this.artworkPriceService = artworkPriceService;
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
    public ResponseEntity<?> getRecentSearchHistory(Authentication authentication) {
        if (!isArtist(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 여기는 작가가 아니라는 http 사이트를 띄워줘야함!!!
        }

        String email = authentication.getName();
        User user = userService.findByEmail(email);
        List<SearchHistory> histories = searchHistoryService.findByUserId(user.getId());
        List<String> keywords = histories.stream()
                .map(SearchHistory::getKeyword)
                .collect(Collectors.toList());
        return ResponseEntity.ok(keywords);
    }

    // 3. 작가 정보 API
    @Operation(summary = "작가 정보 조회", description = "특정 작가의 정보를 반환합니다.")
    @GetMapping("/{artistId}")
    public ResponseEntity<ArtistDto> getArtistInfo(@PathVariable Long artistId, Authentication authentication) {
        if (!isArtist(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 여기는 작가가 아니라는 http 사이트를 띄워줘야함!!!
        }

        Artist artist = artistService.getArtistById(artistId);
        ArtistDto artistDto = new ArtistDto(artist.getId(), artist.getBannerUrl(), artist.getName(), artist.getTags(), artist.getLikes());
        return ResponseEntity.ok(artistDto);
    }

    // 4. 작가 작품 정보 API
    @Operation(summary = "작가 작품 목록 조회", description = "특정 작가의 작품 목록을 반환합니다.")
    @GetMapping("/{artistId}/artworks")
    public ResponseEntity<List<ArtworkDto>> getArtistArtworks(@PathVariable Long artistId, Authentication authentication) {
        if (!isArtist(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 여기는 작가가 아니라는 http 사이트를 띄워줘야함!!!
        }

        List<Artwork> artworks = artworkService.getArtworksByArtist(artistId);
        List<ArtworkDto> artworkDtos = artworks.stream()
                .map(artwork -> new ArtworkDto(
                        artwork.getId(),
                        artwork.getTitle(),
                        artwork.getDescription(),
                        artwork.getMaterial(),
                        artwork.getSize(),
                        artwork.getYear(),
                        artwork.getIsSaved(),
                        artwork.getImages().stream().map(Image::getImageUrl).collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(artworkDtos);
    }

    // 5. 작가 전시 정보 API
    @Operation(summary = "작가 전시 목록 조회", description = "특정 작가의 전시 목록을 반환합니다.")
    @GetMapping("/{artistId}/exhibitions")
    public ResponseEntity<List<ExhibitionDto>> getArtistExhibitions(@PathVariable Long artistId, Authentication authentication) {
        if (!isArtist(authentication)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 여기는 작가가 아니라는 http 사이트를 띄워줘야함!!!
        }

        List<Exhibition> exhibitions = exhibitionService.getExhibitionsByArtist(artistId);
        List<ExhibitionDto> exhibitionDtos = exhibitions.stream()
                .map(exhibition -> new ExhibitionDto(
                        exhibition.getId(),
                        exhibition.getTitle(),
                        exhibition.getDescription(),
                        exhibition.getIsSaved(),
                        exhibition.getImages().stream().map(Image::getImageUrl).collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(exhibitionDtos);
    }

    // 6. 작가 작품 가격 API
    @Operation(summary = "작품 가격 조회", description = "특정 작가의 작품 가격 목록을 반환합니다.")
    @GetMapping("/{artistId}/artworks/prices")
    public ResponseEntity<List<ArtworkPriceDto>> getArtworkPrices(@PathVariable Long artistId,
                                                                  @RequestParam int limit) {
        List<Artwork> artworks = artworkService.getArtworksByArtist(artistId);
        List<ArtworkPrice> prices = artworks.stream()
                .flatMap(artwork -> artworkPriceService.getPricesByArtwork(artwork.getId()).stream())
                .sorted(Comparator.comparing(ArtworkPrice::getMinPrice))
                .limit(limit)
                .toList();
        List<ArtworkPriceDto> priceDtos = prices.stream()
                .map(price -> new ArtworkPriceDto(price.getMinPrice(), price.getMaxPrice()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(priceDtos);
    }

    private boolean isArtist(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }
        String email = authentication.getName();
        User user = userService.findByEmail(email);
        return user != null && user.isArtist(); // Check if user is an artist
    }
}

