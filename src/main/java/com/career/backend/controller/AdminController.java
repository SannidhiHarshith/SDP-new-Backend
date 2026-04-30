package com.career.backend.controller;

import com.career.backend.dto.AssessmentDto;
import com.career.backend.service.AdminService;
import com.career.backend.service.AssessmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping({"/api/admin", "/admin"})
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final AssessmentService assessmentService;

    @GetMapping("/edit-requests")
    public ResponseEntity<?> getEditRequests() {
        return ResponseEntity.ok(adminService.getPendingEditRequests());
    }

    @PutMapping("/edit-requests/{requestId}/approve")
    public ResponseEntity<?> approveEditRequest(@PathVariable Long requestId) {
        try {
            adminService.approveEditRequest(requestId);
            return ResponseEntity.ok(Map.of("message", "Approved"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/assessments")
    public ResponseEntity<?> createAssessment(@RequestBody AssessmentDto.AssessmentResponse request) {
        try {
            AssessmentDto.AssessmentResponse response = assessmentService.createAssessment(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }
}
