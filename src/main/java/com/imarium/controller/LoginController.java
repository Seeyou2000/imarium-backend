package com.imarium.controller;

import com.imarium.dto.AuthResponseDto;
import com.imarium.entity.Artist;
import com.imarium.entity.User;
import com.imarium.security.JwtUtil;
import com.imarium.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public LoginController(UserService userService,
                           PasswordEncoder passwordEncoder,
                           JwtUtil jwtUtil) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    // 1. 이메일 확인 API
    @Operation(summary = "이메일 확인", description = "입력한 이메일이 이미 존재하는지 확인합니다.")
    @PostMapping("/check-email")
    public ResponseEntity<Boolean> checkEmail(@RequestParam String email) {
        boolean exists = userService.existsByEmail(email);
        return ResponseEntity.ok(exists);
    }

    // 2. 비밀번호 확인 API
    @Operation(summary = "비밀번호 확인", description = "입력한 이메일과 비밀번호가 일치하는지 확인합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "비밀번호 일치 여부"),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
    })
    @PostMapping("/check-password")
    public ResponseEntity<Boolean> checkPassword(@RequestParam String email, @RequestParam String password) {
        User user = userService.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        boolean matches = passwordEncoder.matches(password, user.getPassword());
        return ResponseEntity.ok(matches);
    }

    // 3. 로그인 API
    @Operation(summary = "로그인", description = "사용자가 이메일과 비밀번호로 로그인합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공 및 JWT 토큰 반환"),
            @ApiResponse(responseCode = "401", description = "유효하지 않은 이메일 또는 비밀번호")
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String email,
                                   @RequestParam String password,
                                   @RequestParam(required = false) boolean rememberMe) {
        User user = userService.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }

        // Create authentication token
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword(), user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        // Generate JWT token
        String token = jwtUtil.generateToken(user.getEmail(), user.getId());

        // Save login state if needed
        user.setIsSavedLogin(rememberMe);
        userService.saveUser(user);

        return ResponseEntity.ok(new AuthResponseDto(token, user instanceof Artist ? "ARTIST" : "USER"));
    }

    // 4. 아이디 저장 여부 API
    @Operation(summary = "아이디 저장 여부 확인", description = "현재 사용자의 아이디 저장 여부를 확인합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "아이디 저장 여부"),
            @ApiResponse(responseCode = "401", description = "로그인하지 않은 경우")
    })
    @GetMapping("/is-saved-login")
    public ResponseEntity<Boolean> isSavedLogin(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated() || (authentication instanceof AnonymousAuthenticationToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String email = authentication.getName();
        User user = userService.findByEmail(email);
        return ResponseEntity.ok(user.getIsSavedLogin());
    }
}