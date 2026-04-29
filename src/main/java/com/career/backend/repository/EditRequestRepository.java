package com.career.backend.repository;

import com.career.backend.model.EditRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EditRequestRepository extends JpaRepository<EditRequest, Long> {
    List<EditRequest> findByStatus(String status);
}
