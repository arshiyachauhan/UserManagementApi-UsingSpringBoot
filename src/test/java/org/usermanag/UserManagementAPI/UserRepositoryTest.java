package org.usermanag.UserManagementAPI;


import static org.assertj.core.api.Assertions.*;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.usermanag.UserManagementAPI.model.User;
import org.usermanag.UserManagementAPI.repository.UserRepository;

@DataJpaTest
//it loads only a slice of the Spring context relevant to JPA

public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    void setUp() {
        userRepository.deleteAllInBatch();
        user1 = new User("John Doe", "john@example.com");
        user2 = new User("Charlie Smith", "charlie@example.com");
        user3 = new User("Manja","Manja@example.com");

        entityManager.persist(user1);
        entityManager.persist(user2);
        entityManager.flush();
        entityManager.clear();
    }

    @Test 
    void testFindEmailFound() {
        Optional<User> foundUser = userRepository.findByEmail(user1.getEmail());
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getName()).isEqualTo(user1.getName());
        assertThat(foundUser.get().getEmail()).isEqualTo(user1.getEmail());
        assertThat(foundUser.get().getId()).isEqualTo(user1.getId());
    }
    
    @Test
    void testSavedUser() {
    	user2 = new User("Charlie Smith", "charlie@example.com");
        User savedUser = userRepository.save(user2);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getName()).isEqualTo("Charlie Smith");
        assertThat(savedUser.getEmail()).isEqualTo("charlie@example.com");
    }
    
    @Test
    void testUpdateUser() {
    	//updating user3 data
    	String newName="manoj reddy";
    	String newEmail="manojreddy@gmail.com";
    	user3.setName(newName);
    	user3.setEmail(newEmail);
    	//save the updated user
    	User updatedUser= userRepository.save(user3);
    	 assertThat(updatedUser).isNotNull();
         assertThat(updatedUser.getName()).isEqualTo(newName);
         assertThat(updatedUser.getEmail()).isEqualTo(newEmail);
    }
    
    @Test
    void testDeleteUser() {
        User user = userRepository.save(new User("Delete Me", "deleteme@example.com"));
        assertThat(userRepository.findById(user.getId())).isPresent();

        userRepository.delete(user);
        assertThat(userRepository.findById(user.getId())).isNotPresent();
    }

    
    

}
