package com.career.backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class AssessmentDto {

    @Data
    public static class AssessmentResponse {
        private Long id;
        private String title;
        private String description;
        private String requiredSkill;
        private List<QuestionResponse> questions;
    }

    @Data
    public static class QuestionResponse {
        private Long id;
        private String type;
        private String text;
        private List<String> options;
        private String answer;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ResultRequest {
        private Integer score;
        private Integer total;
        private Long assessmentId;
        private Map<String, Object> resultDetails;
    }

    @Data
    public static class ResultResponse {
        private Long id;
        private Long userId;
        private Long assessmentId;
        private Integer score;
        private Integer total;
        private LocalDateTime date; // mapping to frontend expected "date" instead of submittedDate
        private Map<String, Object> resultDetails;
    }
}
