package com.career.backend.service;

import com.career.backend.dto.ProfileEditDto;
import com.career.backend.dto.UserDto;
import com.career.backend.model.EditRequest;
import com.career.backend.model.User;
import com.career.backend.repository.EditRequestRepository;
import com.career.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final EditRequestRepository editRequestRepository;
    private final UserRepository userRepository;

    public List<ProfileEditDto.Response> getPendingEditRequests() {
        return editRequestRepository.findByStatus("PENDING").stream()
                .map(this::mapToDtoEnriched)
                .collect(Collectors.toList());
    }

    public void approveEditRequest(Long requestId) {
        EditRequest request = editRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        
        if (!"PENDING".equals(request.getStatus())) {
            throw new RuntimeException("Request is not PENDING");
        }

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getPersonalInfo() != null) {
            user.getPersonalInfo().putAll(request.getRequestedChanges());
        } else {
            user.setPersonalInfo(request.getRequestedChanges());
        }
        
        userRepository.save(user);

        request.setStatus("APPROVED");
        editRequestRepository.save(request);
    }

    private ProfileEditDto.Response mapToDtoEnriched(EditRequest req) {
        ProfileEditDto.Response res = new ProfileEditDto.Response();
        res.setId(req.getId());
        res.setUserId(req.getUserId());
        res.setStatus(req.getStatus());
        res.setRequestedChanges(req.getRequestedChanges());
        res.setRequestedDate(req.getRequestedDate());

        userRepository.findById(req.getUserId()).ifPresent(user -> {
            UserDto uDto = new UserDto();
            uDto.setId(user.getId());
            uDto.setName(user.getName());
            uDto.setEmail(user.getEmail());
            uDto.setRole(user.getRole());
            uDto.setPersonalInfo(user.getPersonalInfo());
            res.setUser(uDto);
        });

        return res;
    }
}
