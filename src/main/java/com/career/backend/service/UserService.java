package com.career.backend.service;

import com.career.backend.dto.AssessmentDto;
import com.career.backend.dto.ProfileEditDto;
import com.career.backend.dto.UserDto;
import com.career.backend.model.AssessmentResult;
import com.career.backend.model.EditRequest;
import com.career.backend.model.User;
import com.career.backend.repository.AssessmentResultRepository;
import com.career.backend.repository.EditRequestRepository;
import com.career.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final EditRequestRepository editRequestRepository;
    private final AssessmentResultRepository resultRepository;

    public UserDto savePersonalInfo(Long userId, Map<String, Object> info) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setPersonalInfo(info);
        userRepository.save(user);
        return mapToDto(user);
    }

    public ProfileEditDto.Response requestProfileEdit(Long userId, Map<String, Object> editData) {
        userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        EditRequest request = EditRequest.builder()
                .userId(userId)
                .status("PENDING")
                .requestedChanges(editData)
                .build();

        editRequestRepository.save(request);

        ProfileEditDto.Response response = new ProfileEditDto.Response();
        response.setId(request.getId());
        response.setUserId(request.getUserId());
        response.setStatus(request.getStatus());
        response.setRequestedChanges(request.getRequestedChanges());
        response.setRequestedDate(request.getRequestedDate());
        return response;
    }

    public AssessmentDto.ResultResponse submitAssessmentResult(Long userId, AssessmentDto.ResultRequest request) {
        userRepository.findById(userId).orElseThrow(RuntimeException::new);

        AssessmentResult result = AssessmentResult.builder()
                .userId(userId)
                .assessmentId(request.getAssessmentId())
                .score(request.getScore())
                .total(request.getTotal())
                .resultDetails(request.getResultDetails())
                .build();

        resultRepository.save(result);

        AssessmentDto.ResultResponse response = new AssessmentDto.ResultResponse();
        response.setId(result.getId());
        response.setUserId(result.getUserId());
        response.setAssessmentId(result.getAssessmentId());
        response.setScore(result.getScore());
        response.setTotal(result.getTotal());
        response.setResultDetails(result.getResultDetails());
        response.setDate(result.getSubmittedDate());
        return response;
    }

    public List<AssessmentDto.ResultResponse> getUserResults(Long userId) {
        return resultRepository.findByUserIdOrderBySubmittedDateDesc(userId).stream()
                .map(r -> {
                    AssessmentDto.ResultResponse res = new AssessmentDto.ResultResponse();
                    res.setId(r.getId());
                    res.setUserId(r.getUserId());
                    res.setAssessmentId(r.getAssessmentId());
                    res.setScore(r.getScore());
                    res.setTotal(r.getTotal());
                    res.setDate(r.getSubmittedDate());
                    res.setResultDetails(r.getResultDetails());
                    return res;
                })
                .collect(Collectors.toList());
    }

    private UserDto mapToDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setPersonalInfo(user.getPersonalInfo());
        return dto;
    }
}
