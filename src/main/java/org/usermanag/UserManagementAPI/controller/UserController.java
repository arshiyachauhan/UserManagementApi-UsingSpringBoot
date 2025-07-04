package org.usermanag.UserManagementAPI.controller;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.usermanag.UserManagementAPI.model.User;
import org.usermanag.UserManagementAPI.repository.UserRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // POST single user
    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        User savedUser = userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    // POST multiple users
    @PostMapping("/multiple")
    public ResponseEntity<List<User>> createMultipleUsers(@Valid @RequestBody List<User> users) {
        List<User> savedUsers = userRepository.saveAll(users);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUsers);
    }
    


    // GET all users
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // GET user by ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // PUT update user
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @Valid @RequestBody User userDetails) {
        return userRepository.findById(id).map(existingUser -> {
            existingUser.setName(userDetails.getName());
            existingUser.setEmail(userDetails.getEmail());
            User updatedUser = userRepository.save(existingUser);
            return ResponseEntity.ok(updatedUser);
        }).orElse(ResponseEntity.notFound().build());
    }

    // DELETE user
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Serve mock data from resources/data/MOCK_DATA.json
    @GetMapping("/mock")
    public ResponseEntity<List<User>> getMockUsers() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream inputStream = getClass().getResourceAsStream("/data/MOCK_DATA.json");
            List<User> mockUsers = mapper.readValue(inputStream, new TypeReference<List<User>>() {});

            // Save to database to generate id, createdAt, updatedAt
            List<User> savedUsers = userRepository.saveAll(mockUsers);

            return ResponseEntity.ok(savedUsers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    //PAGINATION
    @GetMapping("/page")
    public ResponseEntity<Page<User>> getUsersPage(Pageable pageable) {
        Page<User> page = userRepository.findAll(pageable);
        return ResponseEntity.ok(page);
    }
    
    //Testing Pagination in PostMan- GET http://localhost:8080/api/users/page?page=0&size=10&sort=name,asc/desc


}
