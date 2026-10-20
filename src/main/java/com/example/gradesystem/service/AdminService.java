package com.example.gradesystem.service;

import com.example.gradesystem.entity.*;
import com.example.gradesystem.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final StudyGroupRepository groupRepository;
    private final SubjectRepository subjectRepository;
    private final PasswordEncoder passwordEncoder;

    public List<StudyGroup> getAllGroups() {
        return groupRepository.findAll();
    }

    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    public List<User> getAllTeachers() {
        return userRepository.findByRoleOrderByFullNameAsc(Role.TEACHER);
    }

    public List<User> getAllStudents() {
        return userRepository.findByRoleOrderByFullNameAsc(Role.STUDENT);
    }

    @Transactional
    public StudyGroup createGroup(String name) {
        return groupRepository.save(StudyGroup.builder().name(name).build());
    }

    @Transactional
    public Subject createSubject(String name) {
        return subjectRepository.save(Subject.builder().name(name).build());
    }

    @Transactional
    public User createTeacher(String username, String rawPassword, String fullName, List<Long> subjectIds) {
        Set<Subject> subjects = new HashSet<>();
        if (subjectIds != null && !subjectIds.isEmpty()) {
            subjects.addAll(subjectRepository.findAllById(subjectIds));
        }
        User teacher = User.builder()
                .username(username)
                .password(passwordEncoder.encode(rawPassword))
                .fullName(fullName)
                .role(Role.TEACHER)
                .subjects(subjects)
                .build();
        return userRepository.save(teacher);
    }

    @Transactional
    public User createStudent(String username, String rawPassword, String fullName, Long groupId) {
        StudyGroup group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Группа не найдена"));
        User student = User.builder()
                .username(username)
                .password(passwordEncoder.encode(rawPassword))
                .fullName(fullName)
                .role(Role.STUDENT)
                .studyGroup(group)
                .build();
        return userRepository.save(student);
    }

    @Transactional
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}