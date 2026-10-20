package com.example.gradesystem;

import com.example.gradesystem.entity.Role;
import com.example.gradesystem.entity.StudyGroup;
import com.example.gradesystem.entity.User;
import com.example.gradesystem.repository.StudyGroupRepository;
import com.example.gradesystem.repository.SubjectRepository;
import com.example.gradesystem.repository.UserRepository;
import com.example.gradesystem.service.AdminService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private StudyGroupRepository groupRepository;
    @Mock
    private SubjectRepository subjectRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AdminService adminService;

    @Test
    void createStudent_Success() {
        StudyGroup group = StudyGroup.builder().id(1L).name("Группа 101").build();
        when(groupRepository.findById(1L)).thenReturn(Optional.of(group));
        when(passwordEncoder.encode("rawPass")).thenReturn("encodedPass");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        User student = adminService.createStudent("stud1", "rawPass", "Сидоров Сидор", 1L);

        assertEquals("stud1", student.getUsername());
        assertEquals("encodedPass", student.getPassword());
        assertEquals(Role.STUDENT, student.getRole());
        assertEquals("Группа 101", student.getStudyGroup().getName());
        verify(userRepository, times(1)).save(any(User.class));
    }
}