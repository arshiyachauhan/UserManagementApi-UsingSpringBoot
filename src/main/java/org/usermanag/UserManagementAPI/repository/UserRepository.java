package org.usermanag.UserManagementAPI.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.usermanag.UserManagementAPI.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    // Example of another method (optional): 
    // List<User> findByNameIgnoreCase(String name);
}

