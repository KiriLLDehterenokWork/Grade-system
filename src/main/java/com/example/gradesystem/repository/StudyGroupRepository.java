package com.example.gradesystem.repository;

import com.example.gradesystem.entity.StudyGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface StudyGroupRepository extends JpaRepository<StudyGroup, Long> {
    Optional<StudyGroup> findByName(String name);
}