package com.example.gradesystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestErrorController {

    @GetMapping("/checkError")
    public String triggerError() {
        throw new RuntimeException("Это тестовая ошибка 500 для демонстрации работы @ControllerAdvice!");
    }
}