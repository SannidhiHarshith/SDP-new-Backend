package com.career.backend.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "edit_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EditRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String status; // PENDING, APPROVED

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "requested_changes", columnDefinition = "json", nullable = false)
    private Map<String, Object> requestedChanges;

    @CreationTimestamp
    @Column(name = "requested_date", updatable = false)
    private LocalDateTime requestedDate;
}
