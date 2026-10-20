package com.example.gradesystem.repository;

import com.example.gradesystem.entity.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface GradeRepository extends JpaRepository<Grade, Long> {
    @Query("SELECT g FROM Grade g JOIN FETCH g.subject JOIN FETCH g.teacher WHERE g.student.id = :studentId ORDER BY g.gradeDate DESC")
    List<Grade> findAllByStudentIdWithDetails(@Param("studentId") Long studentId);

    @Query("SELECT g FROM Grade g JOIN FETCH g.student s LEFT JOIN FETCH s.studyGroup JOIN FETCH g.subject WHERE g.teacher.id = :teacherId ORDER BY g.gradeDate DESC")
    List<Grade> findAllByTeacherIdWithDetails(@Param("teacherId") Long teacherId);
}