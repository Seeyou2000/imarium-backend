package com.imarium.controller;

import com.imarium.dto.PostDto;
import com.imarium.entity.Post;
import com.imarium.entity.SearchHistory;
import com.imarium.entity.User;
import com.imarium.service.PostService;
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
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/write")
public class WriteController {

    private final UserService userService;
    private final SearchHistoryService searchHistoryService;
    private final PostService postService;

    public WriteController(UserService userService,
                           SearchHistoryService searchHistoryService,
                           PostService postService) {
        this.userService = userService;
        this.searchHistoryService = searchHistoryService;
        this.postService = postService;
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

    // 3. 게시글 작성 API
    @Operation(summary = "게시글 작성", description = "새로운 게시글을 작성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "게시글 작성 성공"),
            @ApiResponse(responseCode = "401", description = "사용자가 로그인하지 않은 경우")
    })
    @PostMapping
    public ResponseEntity<PostDto> createPost(@RequestBody PostDto postDto, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated() || (authentication instanceof AnonymousAuthenticationToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String email = authentication.getName();
        User user = userService.findByEmail(email);
        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setUser(user);
        Post savedPost = postService.createPost(post);
        PostDto responseDto = new PostDto(savedPost.getId(), savedPost.getTitle(), savedPost.getContent(), savedPost.getUser().getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    // 4. 게시글 수정 API
    @Operation(summary = "게시글 수정", description = "주어진 ID의 게시글을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 수정 성공"),
            @ApiResponse(responseCode = "404", description = "게시물을 찾을 수 없는 경우"),
            @ApiResponse(responseCode = "403", description = "사용자가 게시글의 소유자가 아닌 경우"),
            @ApiResponse(responseCode = "401", description = "사용자가 로그인하지 않은 경우")
    })
    @PutMapping("/{postId}")
    public ResponseEntity<PostDto> updatePost(@PathVariable Long postId, @RequestBody PostDto postDto, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated() || (authentication instanceof AnonymousAuthenticationToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = authentication.getName();
        User user = userService.findByEmail(email);

        Optional<Post> optionalPost = postService.getPostById(postId);
        if (optionalPost.isPresent()) {
            Post existingPost = optionalPost.get();
            if (!existingPost.getUser().getId().equals(user.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            existingPost.setTitle(postDto.getTitle());
            existingPost.setContent(postDto.getContent());
            Post updatedPost = postService.updatePost(existingPost);
            PostDto responseDto = new PostDto(updatedPost.getId(), updatedPost.getTitle(), updatedPost.getContent(), updatedPost.getUser().getEmail());
            return ResponseEntity.ok(responseDto);
        } else {
            return ResponseEntity.notFound().build(); // 게시물이 존재하지 않을 경우 404 Not Found
        }
    }

    // 5. 게시글 삭제 API
    @Operation(summary = "게시글 삭제", description = "주어진 ID의 게시글을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "게시글 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "게시물을 찾을 수 없는 경우"),
            @ApiResponse(responseCode = "403", description = "사용자가 게시글의 소유자가 아닌 경우"),
            @ApiResponse(responseCode = "401", description = "사용자가 로그인하지 않은 경우")
    })
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated() || (authentication instanceof AnonymousAuthenticationToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = authentication.getName();
        User user = userService.findByEmail(email);

        Optional<Post> optionalPost = postService.getPostById(postId);
        if (optionalPost.isPresent()) {
            Post existingPost = optionalPost.get();
            if (!existingPost.getUser().getId().equals(user.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            postService.deletePost(postId);
            return ResponseEntity.noContent().build(); // 삭제 성공 시 204 No Content
        } else {
            return ResponseEntity.notFound().build(); // 게시물이 존재하지 않을 경우 404 Not Found
        }
    }

    // 6. 전체 게시글 조회 API
    @Operation(summary = "전체 게시글 조회", description = "모든 게시글을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<PostDto>> getAllPosts() {
        List<Post> posts = postService.getAllPosts();
        List<PostDto> postDtos = posts.stream()
                .map(post -> new PostDto(post.getId(), post.getTitle(), post.getContent(), post.getUser().getEmail()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(postDtos);
    }
}

