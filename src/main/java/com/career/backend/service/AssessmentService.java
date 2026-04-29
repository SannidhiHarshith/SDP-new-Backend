package com.career.backend.service;

import com.career.backend.dto.AssessmentDto;
import com.career.backend.model.Assessment;
import com.career.backend.model.Question;
import com.career.backend.repository.AssessmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssessmentService {

    private final AssessmentRepository assessmentRepository;

    public List<AssessmentDto.AssessmentResponse> getAllAssessments() {
        return assessmentRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public AssessmentDto.AssessmentResponse getAssessmentById(Long id) {
        Assessment assessment = assessmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assessment not found"));
        return mapToDto(assessment);
    }

    public AssessmentDto.AssessmentResponse createAssessment(AssessmentDto.AssessmentResponse request) {
        Assessment assessment = Assessment.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .requiredSkill(request.getRequiredSkill())
                .build();

        if (request.getQuestions() != null) {
            List<Question> questions = request.getQuestions().stream()
                    .map(q -> Question.builder()
                            .text(q.getText())
                            .type(q.getType())
                            .options(q.getOptions())
                            .answer(q.getAnswer())
                            .assessment(assessment)
                            .build())
                    .collect(Collectors.toList());
            assessment.setQuestions(questions);
        }

        Assessment savedAssessment = assessmentRepository.save(assessment);
        return mapToDto(savedAssessment);
    }

    private AssessmentDto.AssessmentResponse mapToDto(Assessment assessment) {
        AssessmentDto.AssessmentResponse dto = new AssessmentDto.AssessmentResponse();
        dto.setId(assessment.getId());
        dto.setTitle(assessment.getTitle());
        dto.setDescription(assessment.getDescription());
        dto.setRequiredSkill(assessment.getRequiredSkill());

        if (assessment.getQuestions() != null) {
            dto.setQuestions(assessment.getQuestions().stream()
                    .map(q -> {
                        AssessmentDto.QuestionResponse qDto = new AssessmentDto.QuestionResponse();
                        qDto.setId(q.getId());
                        qDto.setType(q.getType());
                        qDto.setText(q.getText());
                        qDto.setOptions(q.getOptions());
                        qDto.setAnswer(q.getAnswer());
                        return qDto;
                    }).collect(Collectors.toList()));
        }
        return dto;
    }
}
