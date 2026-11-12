package com.example.studentmanagement.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

/**
 * Request DTO for creating or updating a Student.
 * Uses Jakarta Bean Validation annotations to validate incoming JSON payloads.
 */
public record StudentRequestDto(
        @NotBlank(message = "First name is required")
        @Size(max = 50, message = "First name must not exceed 50 characters")
        String firstName,

        @NotBlank(message = "Last name is required")
        @Size(max = 50, message = "Last name must not exceed 50 characters")
        String lastName,

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be a valid email address")
        @Size(max = 100, message = "Email must not exceed 100 characters")
        String email,

        @Past(message = "Date of birth must be in the past")
        LocalDate dateOfBirth,

        @NotBlank(message = "Major is required")
        @Size(max = 100, message = "Major must not exceed 100 characters")
        String major,

        @DecimalMin(value = "0.0", message = "GPA must be at least 0.0")
        @DecimalMax(value = "4.0", message = "GPA must not exceed 4.0")
        Double gpa
) {
}
