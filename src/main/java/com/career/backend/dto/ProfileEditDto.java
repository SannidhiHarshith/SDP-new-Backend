package com.career.backend.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.Map;

public class ProfileEditDto {

    @Data
    public static class Request {
        private Map<String, Object> requestedChanges;
    }

    @Data
    public static class Response {
        private Long id;
        private Long userId;
        private String status;
        private Map<String, Object> requestedChanges;
        private LocalDateTime requestedDate;
        private UserDto user; // For admin view enrichment
    }
}
