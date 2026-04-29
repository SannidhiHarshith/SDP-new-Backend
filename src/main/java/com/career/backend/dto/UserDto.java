package com.career.backend.dto;

import lombok.Data;
import java.util.Map;

@Data
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private String role;
    private Map<String, Object> personalInfo;
}
