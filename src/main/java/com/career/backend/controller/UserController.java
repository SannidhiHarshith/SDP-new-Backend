package com.career.backend.controller;

import com.career.backend.dto.AssessmentDto;
import com.career.backend.dto.ProfileEditDto;
import com.career.backend.dto.UserDto;
import com.career.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping({"/api/users", "/users"})
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/{userId}/personal-info")
    public ResponseEntity<?> savePersonalInfo(@PathVariable Long userId, @RequestBody Map<String, Object> info) {
        try {
            UserDto user = userService.savePersonalInfo(userId, info);
            // api.js expects: resolve({ data: { user: users[userIndex] } });
            return ResponseEntity.ok(Map.of("user", user));
        } catch (Exception e) {
            String msg = e.getMessage() != null ? e.getMessage() : "Unknown error occurred";
            return ResponseEntity.badRequest().body(Map.of("message", msg));
        }
    }

    @PostMapping("/{userId}/edit-request")
    public ResponseEntity<?> requestProfileEdit(@PathVariable Long userId, @RequestBody Map<String, Object> editData) {
        try {
            ProfileEditDto.Response response = userService.requestProfileEdit(userId, editData);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/{userId}/results")
    public ResponseEntity<?> submitAssessmentResult(@PathVariable Long userId, @RequestBody AssessmentDto.ResultRequest request) {
        try {
            AssessmentDto.ResultResponse result = userService.submitAssessmentResult(userId, request);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/{userId}/results")
    public ResponseEntity<?> getUserResults(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUserResults(userId));
    }
}
