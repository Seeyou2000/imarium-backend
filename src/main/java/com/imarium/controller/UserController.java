package com.imarium.controller;

import com.imarium.dto.UserDto;
import com.imarium.entity.User;
import com.imarium.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 사용자 등록
    @Operation(summary = "사용자 등록", description = "새 사용자를 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 등록 성공"),
            @ApiResponse(responseCode = "400", description = "이메일이 이미 존재하는 경우")
    })
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        if (userService.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest().build(); // 이메일이 이미 존재하는 경우
        }
        User savedUser = userService.saveUser(user);
        return ResponseEntity.ok(savedUser);
    }

    // 이메일 존재 여부 확인
    @Operation(summary = "이메일 존재 여부 확인", description = "주어진 이메일이 이미 존재하는지 확인합니다.")
    @GetMapping("/existsByEmail")
    public ResponseEntity<Boolean> existsByEmail(@RequestParam String email) {
        boolean exists = userService.existsByEmail(email);
        return ResponseEntity.ok(exists);
    }

    // 사용자 정보 조회 (ID로)
    @Operation(summary = "사용자 정보 조회", description = "주어진 ID로 사용자의 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 정보 반환"),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없는 경우")
    })
    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    // 모든 사용자 정보 조회
    @Operation(summary = "모든 사용자 정보 조회", description = "모든 사용자의 정보를 반환합니다.")
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // 사용자 정보 수정
    @Operation(summary = "사용자 정보 수정", description = "주어진 ID로 사용자의 정보를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 정보 수정 성공"),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없는 경우")
    })
    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable Long userId, @RequestBody UserDto userDto) {
        User updatedUser = userService.updateUser(userId, userDto);
        return ResponseEntity.ok(updatedUser);
    }

    // 사용자 삭제
    @Operation(summary = "사용자 삭제", description = "주어진 ID로 사용자를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "삭제 성공"),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없는 경우")
    })
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build(); // 삭제 성공 시 204 No Content
    }
}
