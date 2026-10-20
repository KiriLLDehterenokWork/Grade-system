package com.example.gradesystem.controller;

import com.example.gradesystem.entity.Role;
import com.example.gradesystem.entity.User;
import com.example.gradesystem.repository.UserRepository;
import com.example.gradesystem.service.GradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/teacher")
@RequiredArgsConstructor
public class TeacherController {

    private final GradeService gradeService;
    private final UserRepository userRepository;

    @GetMapping
    public String dashboard(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User teacher = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        model.addAttribute("teacher", teacher);
        model.addAttribute("subjects", teacher.getSubjects());
        model.addAttribute("students", userRepository.findByRoleOrderByFullNameAsc(Role.STUDENT));
        model.addAttribute("grades", gradeService.getGradesByTeacher(teacher.getId()));
        model.addAttribute("today", LocalDate.now());
        return "teacher";
    }

    @PostMapping("/grades")
    public String addGrade(@AuthenticationPrincipal UserDetails userDetails,
                           @RequestParam Long studentId,
                           @RequestParam Long subjectId,
                           @RequestParam Integer value,
                           @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate gradeDate) {
        User teacher = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        gradeService.addGrade(teacher.getId(), studentId, subjectId, value, gradeDate);
        return "redirect:/teacher";
    }

    @PostMapping("/grades/{id}/update")
    public String updateGrade(@AuthenticationPrincipal UserDetails userDetails,
                              @PathVariable Long id,
                              @RequestParam Integer value,
                              @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate gradeDate) {
        User teacher = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        gradeService.updateGrade(id, teacher.getId(), value, gradeDate);
        return "redirect:/teacher";
    }

    @PostMapping("/grades/{id}/delete")
    public String deleteGrade(@AuthenticationPrincipal UserDetails userDetails,
                              @PathVariable Long id) {
        User teacher = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        gradeService.deleteGrade(id, teacher.getId());
        return "redirect:/teacher";
    }
}