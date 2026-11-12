package com.example.studentmanagement.controller;

import com.example.studentmanagement.dto.StudentRequestDto;
import com.example.studentmanagement.repository.StudentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class StudentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        studentRepository.deleteAll();
    }

    @Test
    void shouldPerformFullCrudLifecycleForStudent() throws Exception {
        // 1. CREATE Student (POST /api/v1/students)
        StudentRequestDto createRequest = new StudentRequestDto(
                "Alice",
                "Smith",
                "alice.smith@university.edu",
                LocalDate.of(2003, 5, 14),
                "Computer Science",
                3.85
        );

        String responseJson = mockMvc.perform(post("/api/v1/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.firstName", is("Alice")))
                .andExpect(jsonPath("$.email", is("alice.smith@university.edu")))
                .andExpect(jsonPath("$.major", is("Computer Science")))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long studentId = objectMapper.readTree(responseJson).get("id").asLong();

        // 2. GET Student by ID (GET /api/v1/students/{id})
        mockMvc.perform(get("/api/v1/students/{id}", studentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Alice")))
                .andExpect(jsonPath("$.gpa", is(3.85)));

        // 3. GET All Students filtered by major (GET /api/v1/students?major=Computer Science)
        mockMvc.perform(get("/api/v1/students").param("major", "Computer Science"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        // 4. UPDATE Student (PUT /api/v1/students/{id})
        StudentRequestDto updateRequest = new StudentRequestDto(
                "Alice",
                "Johnson",
                "alice.smith@university.edu",
                LocalDate.of(2003, 5, 14),
                "Data Science",
                3.95
        );

        mockMvc.perform(put("/api/v1/students/{id}", studentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName", is("Johnson")))
                .andExpect(jsonPath("$.major", is("Data Science")))
                .andExpect(jsonPath("$.gpa", is(3.95)));

        // 5. DELETE Student (DELETE /api/v1/students/{id})
        mockMvc.perform(delete("/api/v1/students/{id}", studentId))
                .andExpect(status().isNoContent());

        // 6. Verify 404 Not Found after deletion
        mockMvc.perform(get("/api/v1/students/{id}", studentId))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnBadRequestWhenValidationFails() throws Exception {
        StudentRequestDto invalidRequest = new StudentRequestDto(
                "",
                "Smith",
                "not-an-email",
                LocalDate.of(2003, 5, 14),
                "Mathematics",
                4.5 // Exceeds 4.0 max GPA
        );

        mockMvc.perform(post("/api/v1/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.firstName").exists())
                .andExpect(jsonPath("$.validationErrors.email").exists())
                .andExpect(jsonPath("$.validationErrors.gpa").exists());
    }
}
