package com.example.gradesystem.controller;

import com.example.gradesystem.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("groups", adminService.getAllGroups());
        model.addAttribute("subjects", adminService.getAllSubjects());
        model.addAttribute("teachers", adminService.getAllTeachers());
        model.addAttribute("students", adminService.getAllStudents());
        return "admin";
    }

    @PostMapping("/groups")
    public String createGroup(@RequestParam String name) {
        adminService.createGroup(name);
        return "redirect:/admin";
    }

    @PostMapping("/subjects")
    public String createSubject(@RequestParam String name) {
        adminService.createSubject(name);
        return "redirect:/admin";
    }

    @PostMapping("/teachers")
    public String createTeacher(@RequestParam String username,
                                @RequestParam String password,
                                @RequestParam String fullName,
                                @RequestParam(required = false) List<Long> subjectIds) {
        adminService.createTeacher(username, password, fullName, subjectIds);
        return "redirect:/admin";
    }

    @PostMapping("/students")
    public String createStudent(@RequestParam String username,
                                @RequestParam String password,
                                @RequestParam String fullName,
                                @RequestParam Long groupId) {
        adminService.createStudent(username, password, fullName, groupId);
        return "redirect:/admin";
    }

    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
        return "redirect:/admin";
    }
}