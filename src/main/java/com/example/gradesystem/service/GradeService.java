package com.example.gradesystem.service;

import com.example.gradesystem.entity.*;
import com.example.gradesystem.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GradeService {

    private final GradeRepository gradeRepository;
    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;

    public List<Grade> getGradesByTeacher(Long teacherId) {
        return gradeRepository.findAllByTeacherIdWithDetails(teacherId);
    }

    public List<Grade> getGradesByStudent(Long studentId) {
        return gradeRepository.findAllByStudentIdWithDetails(studentId);
    }

    @Transactional
    public Grade addGrade(Long teacherId, Long studentId, Long subjectId, Integer value, LocalDate date) {
        User teacher = userRepository.findById(teacherId)
                .orElseThrow(() -> new IllegalArgumentException("Учитель не найден"));
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Студент не найден"));
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new IllegalArgumentException("Предмет не найден"));

        boolean teachesSubject = teacher.getSubjects().stream()
                .anyMatch(s -> s.getId().equals(subjectId));
        if (!teachesSubject) {
            throw new IllegalStateException("Вы не ведете данный предмет!");
        }

        Grade grade = Grade.builder()
                .teacher(teacher)
                .student(student)
                .subject(subject)
                .value(value)
                .gradeDate(date != null ? date : LocalDate.now())
                .build();

        return gradeRepository.save(grade);
    }

    @Transactional
    public Grade updateGrade(Long gradeId, Long teacherId, Integer newValue, LocalDate newDate) {
        Grade grade = gradeRepository.findById(gradeId)
                .orElseThrow(() -> new IllegalArgumentException("Оценка не найдена"));

        if (!grade.getTeacher().getId().equals(teacherId)) {
            throw new IllegalStateException("Нельзя изменить чужую оценку!");
        }

        grade.setValue(newValue);
        if (newDate != null) {
            grade.setGradeDate(newDate);
        }
        return gradeRepository.save(grade);
    }

    @Transactional
    public void deleteGrade(Long gradeId, Long teacherId) {
        Grade grade = gradeRepository.findById(gradeId)
                .orElseThrow(() -> new IllegalArgumentException("Оценка не найдена"));

        if (!grade.getTeacher().getId().equals(teacherId)) {
            throw new IllegalStateException("Нельзя удалить чужую оценку!");
        }
        gradeRepository.delete(grade);
    }

    public Map<String, Double> calculateAverageBySubject(List<Grade> grades) {
        Map<String, List<Grade>> grouped = grades.stream()
                .collect(Collectors.groupingBy(g -> g.getSubject().getName()));

        Map<String, Double> averages = new LinkedHashMap<>();
        grouped.forEach((subjectName, subjectGrades) -> {
            double avg = subjectGrades.stream()
                    .mapToInt(Grade::getValue)
                    .average()
                    .orElse(0.0);
            averages.put(subjectName, Math.round(avg * 100.0) / 100.0);
        });
        return averages;
    }

    public double calculateOverallAverage(List<Grade> grades) {
        if (grades.isEmpty()) {
            return 0.0;
        }
        double avg = grades.stream()
                .mapToInt(Grade::getValue)
                .average()
                .orElse(0.0);
        return Math.round(avg * 100.0) / 100.0;
    }
}