package com.example.gradesystem.repository;

import com.example.gradesystem.entity.Role;
import com.example.gradesystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    List<User> findByRoleOrderByFullNameAsc(Role role);
}