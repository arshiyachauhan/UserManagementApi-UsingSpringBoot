package org.usermanag.UserManagementAPI;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.usermanag.UserManagementAPI.model.User;
import org.usermanag.UserManagementAPI.repository.UserRepository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;



@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
	
	//helps to mock our http request
	@Autowired
	private MockMvc mockMvc;
	
	//converts json data to objects 
	@Autowired
	private ObjectMapper objectMapper;
	
	//user testing done for controllers
	@Autowired
	private UserRepository userRepository;

	//user entity objects
	private User user1;
	private User user2;

	// List to store multiple users
	private List<User> manyUsers;

	// Track initial user count
//	private long initialUserCount;
	
	@BeforeEach
	void setUp() {
		//update local date time to a simple string
		userRepository.deleteAll();
		objectMapper.registerModule(new JavaTimeModule());
		
		//Create 2 users
		User initialUser1 = new User("John Doe", "john@example.com");
		User initialUser2 = new User("Charlie Smith", "charlie@example.com");
		
		//Save users as List
		List<User> savedInitialUsers = userRepository.saveAll(Arrays.asList(initialUser1, initialUser2));
		this.user1 = savedInitialUsers.get(0);
		this.user2 = savedInitialUsers.get(1);
		
		//adding these user instances to the user repository
		manyUsers = IntStream.rangeClosed(1, 25)
				.mapToObj(i -> new User("User " + i, "user" + i + "@example.com"))
				.collect(Collectors.toList());
		userRepository.saveAll(manyUsers);
	}
	
	@Test
	void testGetUserByIdFound() throws Exception {
	    mockMvc.perform(get("/api/users/" + user1.getId())
	            .contentType(MediaType.APPLICATION_JSON))
	    .andExpect(status().isOk())
	    .andExpect(jsonPath("$.id", is(user1.getId().intValue())))
	    .andExpect(jsonPath("$.name", is(user1.getName())))
	    .andExpect(jsonPath("$.email", is(user1.getEmail())));

	}
	@Test
	void testCreatedUsers() throws Exception {

	    // Arrange
	    User newUser1 = new User("Alice Brown", "alice.brown@example.com");
	    User newUser2 = new User("Rohn Gray", "rohan.gray@example.com");
	    List<User> newUsers = Arrays.asList(newUser1, newUser2);

	    // Assert
	    mockMvc.perform(post("/api/users/multiple")
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(objectMapper.writeValueAsString(newUsers)))
	            .andExpect(status().isCreated())
	            .andExpect(jsonPath("$[0].name",is("Alice Brown")))
	            .andExpect(jsonPath("$[1].email", is("rohan.gray@example.com")));
	}
	@Test
	void testDeleteUserFound() throws Exception {
	    mockMvc.perform(delete("/api/users/" + user1.getId())
	            .contentType(MediaType.APPLICATION_JSON))
	            .andExpect(status().isNoContent());
	}

	@Test
	void testDeleteUserNotFound() throws Exception {
	    Long nonExistingId = 9999L;

	    mockMvc.perform(delete("/api/users/" + nonExistingId)
	            .contentType(MediaType.APPLICATION_JSON))
	            .andExpect(status().isNotFound());
	}



}
