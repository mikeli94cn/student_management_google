package com.example.studentmanagement.service.impl;

import com.example.studentmanagement.dto.StudentRequestDto;
import com.example.studentmanagement.dto.StudentResponseDto;
import com.example.studentmanagement.entity.Student;
import com.example.studentmanagement.exception.DuplicateResourceException;
import com.example.studentmanagement.exception.ResourceNotFoundException;
import com.example.studentmanagement.repository.StudentRepository;
import com.example.studentmanagement.service.StudentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public StudentResponseDto createStudent(StudentRequestDto requestDto) {
        if (studentRepository.existsByEmail(requestDto.email())) {
            throw new DuplicateResourceException("A student with email '" + requestDto.email() + "' already exists");
        }

        Student student = new Student(
                requestDto.firstName(),
                requestDto.lastName(),
                requestDto.email(),
                requestDto.dateOfBirth(),
                requestDto.major(),
                requestDto.gpa()
        );

        Student savedStudent = studentRepository.save(student);
        return mapToResponseDto(savedStudent);
    }

    @Override
    @Transactional(readOnly = true)
    public StudentResponseDto getStudentById(Long id) {
        Student student = findStudentOrThrow(id);
        return mapToResponseDto(student);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentResponseDto> getAllStudents(String major) {
        List<Student> students;
        if (major != null && !major.isBlank()) {
            students = studentRepository.findByMajorIgnoreCase(major.trim());
        } else {
            students = studentRepository.findAll();
        }
        return students.stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    @Override
    public StudentResponseDto updateStudent(Long id, StudentRequestDto requestDto) {
        Student student = findStudentOrThrow(id);

        if (!student.getEmail().equalsIgnoreCase(requestDto.email())
                && studentRepository.existsByEmail(requestDto.email())) {
            throw new DuplicateResourceException("A student with email '" + requestDto.email() + "' already exists");
        }

        student.setFirstName(requestDto.firstName());
        student.setLastName(requestDto.lastName());
        student.setEmail(requestDto.email());
        student.setDateOfBirth(requestDto.dateOfBirth());
        student.setMajor(requestDto.major());
        student.setGpa(requestDto.gpa());

        Student updatedStudent = studentRepository.save(student);
        return mapToResponseDto(updatedStudent);
    }

    @Override
    public void deleteStudent(Long id) {
        Student student = findStudentOrThrow(id);
        studentRepository.delete(student);
    }

    private Student findStudentOrThrow(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
    }

    private StudentResponseDto mapToResponseDto(Student student) {
        return new StudentResponseDto(
                student.getId(),
                student.getFirstName(),
                student.getLastName(),
                student.getEmail(),
                student.getDateOfBirth(),
                student.getMajor(),
                student.getGpa(),
                student.getCreatedAt(),
                student.getUpdatedAt()
        );
    }
}
