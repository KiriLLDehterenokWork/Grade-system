package com.example.gradesystem.controller;

import com.example.gradesystem.entity.Grade;
import com.example.gradesystem.entity.User;
import com.example.gradesystem.repository.UserRepository;
import com.example.gradesystem.service.GradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {

    private final GradeService gradeService;
    private final UserRepository userRepository;

    @GetMapping
    public String dashboard(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User student = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        List<Grade> grades = gradeService.getGradesByStudent(student.getId());

        model.addAttribute("student", student);
        model.addAttribute("grades", grades);
        model.addAttribute("subjectAverages", gradeService.calculateAverageBySubject(grades));
        model.addAttribute("overallAverage", gradeService.calculateOverallAverage(grades));
        return "student";
    }
}