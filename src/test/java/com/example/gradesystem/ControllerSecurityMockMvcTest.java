package com.example.gradesystem;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ControllerSecurityMockMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void loginPage_IsAccessibleToAll() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    void checkErrorEndpoint_ReturnsError500View() throws Exception {
        mockMvc.perform(get("/checkError"))
                .andExpect(status().isOk())
                .andExpect(view().name("error-500"))
                .andExpect(model().attributeExists("errorMessage"));
    }

    @Test
    void adminPage_RedirectsUnauthenticatedUserToLogin() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void adminPage_AllowsAdminUser() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin"));
    }

    @Test
    @WithMockUser(username = "student", roles = {"STUDENT"})
    void adminPage_ForbidsStudentUser() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(status().isForbidden());
    }
}