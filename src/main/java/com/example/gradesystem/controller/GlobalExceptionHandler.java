package com.example.gradesystem.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Перехватываем абсолютно все ошибки (Exception.class)
    @ExceptionHandler(Exception.class)
    public String handleAllExceptions(Exception ex, Model model) {
        // Передаем текст ошибки на страницу error-500.html
        model.addAttribute("errorMessage", ex.getMessage());
        return "error-500";
    }
}