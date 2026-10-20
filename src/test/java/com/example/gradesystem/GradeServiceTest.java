package com.example.gradesystem;

import com.example.gradesystem.entity.*;
import com.example.gradesystem.repository.GradeRepository;
import com.example.gradesystem.repository.SubjectRepository;
import com.example.gradesystem.repository.UserRepository;
import com.example.gradesystem.service.GradeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GradeServiceTest {

    @Mock
    private GradeRepository gradeRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private SubjectRepository subjectRepository;

    @InjectMocks
    private GradeService gradeService;

    private Subject javaSubject;
    private User teacher;
    private User student;

    @BeforeEach
    void setUp() {
        javaSubject = Subject.builder().id(1L).name("Java").build();
        teacher = User.builder().id(10L).username("teacher").role(Role.TEACHER).subjects(Set.of(javaSubject)).build();
        student = User.builder().id(20L).username("student").role(Role.STUDENT).build();
    }

    @Test
    void addGrade_Success_WhenTeacherTeachesSubject() {
        when(userRepository.findById(10L)).thenReturn(Optional.of(teacher));
        when(userRepository.findById(20L)).thenReturn(Optional.of(student));
        when(subjectRepository.findById(1L)).thenReturn(Optional.of(javaSubject));
        when(gradeRepository.save(any(Grade.class))).thenAnswer(i -> i.getArguments()[0]);

        Grade result = gradeService.addGrade(10L, 20L, 1L, 9, LocalDate.now());

        assertNotNull(result);
        assertEquals(9, result.getValue());
        assertEquals("Java", result.getSubject().getName());
        verify(gradeRepository, times(1)).save(any(Grade.class));
    }

    @Test
    void addGrade_ThrowsException_WhenTeacherDoesNotTeachSubject() {
        Subject otherSubject = Subject.builder().id(2L).name("История").build();
        when(userRepository.findById(10L)).thenReturn(Optional.of(teacher));
        when(userRepository.findById(20L)).thenReturn(Optional.of(student));
        when(subjectRepository.findById(2L)).thenReturn(Optional.of(otherSubject));

        assertThrows(IllegalStateException.class, () ->
                gradeService.addGrade(10L, 20L, 2L, 8, LocalDate.now())
        );
        verify(gradeRepository, never()).save(any());
    }

    @Test
    void calculateOverallAndSubjectAverages_CorrectMath() {
        Grade g1 = Grade.builder().value(8).subject(javaSubject).build();
        Grade g2 = Grade.builder().value(9).subject(javaSubject).build();
        Grade g3 = Grade.builder().value(10).subject(javaSubject).build();
        List<Grade> grades = List.of(g1, g2, g3);

        double overall = gradeService.calculateOverallAverage(grades);
        Map<String, Double> bySubject = gradeService.calculateAverageBySubject(grades);

        assertEquals(9.0, overall);
        assertEquals(9.0, bySubject.get("Java"));
    }
}