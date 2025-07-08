package org.usermanag.UserManagementAPI.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;



@Entity
@Table(name = "users") 
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is Required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

//    @NotBlank(message = "Email is required")
//    @Email(message = "Email should be valid")
//    private String email;
//    
    //IMPLEMENTING SpringBoot SECURITY
    
    @NotBlank(message="Password is required")
    private String password;
    
    @ElementCollection(fetch= FetchType.EAGER)
    @CollectionTable(name="user_roles",joinColumns=
    @JoinColumn(name= "user_id"))
    @Column(name="role")
    private Set<String> roles= new HashSet<>();
    
    //no two same emails
    @NotBlank(message="Email is required")
    @Email(message="Email should be valid")
    @Column(unique= true)
    private String email;

    // Timestamp fields
    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // Default constructor
    public User() {}
    

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }



    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

}
