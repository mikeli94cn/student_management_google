package com.example.studentmanagement.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Response DTO returned to API clients.
 * Decouples the internal JPA Entity from the external REST representation.
 */
public record StudentResponseDto(
        Long id,
        String firstName,
        String lastName,
        String email,
        LocalDate dateOfBirth,
        String major,
        Double gpa,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
