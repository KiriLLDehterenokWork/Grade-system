package com.example.gradesystem.config;

import com.example.gradesystem.entity.*;
import com.example.gradesystem.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final StudyGroupRepository groupRepository;
    private final SubjectRepository subjectRepository;
    private final GradeRepository gradeRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            User admin = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .fullName("Главный Администратор")
                    .role(Role.ADMIN)
                    .build();
            userRepository.save(admin);

            StudyGroup group = groupRepository.findByName("Группа 101").orElse(null);
            Subject javaSubject = subjectRepository.findByName("Программирование на Java").orElse(null);

            User teacher = User.builder()
                    .username("teacher")
                    .password(passwordEncoder.encode("teacher123"))
                    .fullName("Иванов Иван Иванович")
                    .role(Role.TEACHER)
                    .subjects(javaSubject != null ? Set.of(javaSubject) : Set.of())
                    .build();
            teacher = userRepository.save(teacher);

            User student = User.builder()
                    .username("student")
                    .password(passwordEncoder.encode("student123"))
                    .fullName("Петров Петр Петрович")
                    .role(Role.STUDENT)
                    .studyGroup(group)
                    .build();
            student = userRepository.save(student);

            if (javaSubject != null) {
                gradeRepository.save(Grade.builder()
                        .value(9)
                        .gradeDate(LocalDate.now().minusDays(2))
                        .student(student)
                        .subject(javaSubject)
                        .teacher(teacher)
                        .build());
                gradeRepository.save(Grade.builder()
                        .value(10)
                        .gradeDate(LocalDate.now())
                        .student(student)
                        .subject(javaSubject)
                        .teacher(teacher)
                        .build());
            }
        }
    }
}