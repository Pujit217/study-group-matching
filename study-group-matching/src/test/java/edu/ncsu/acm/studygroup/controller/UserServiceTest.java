package edu.ncsu.acm.studygroup.controller;

import edu.ncsu.acm.studygroup.entity.User;
import edu.ncsu.acm.studygroup.repository.UserRepository;
import edu.ncsu.acm.studygroup.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Tests basic UserService behavior using a mocked repository.
 */
class UserServiceTest {

    /**
     * Mock repository used so the test does not require a real database.
     */
    private UserRepository userRepository;

    /**
     * Service being tested.
     */
    private UserService userService;

    /**
     * Creates fresh test objects before each test.
     */
    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    /**
     * Verifies that a user can be found by email.
     */
    @Test
    void getUserByEmailReturnsUser() {
        User user = new User("Test User", "test@ncsu.edu");

        when(userRepository.findByEmail("test@ncsu.edu"))
                .thenReturn(Optional.of(user));

        Optional<User> result =
                userService.getUserByEmail("test@ncsu.edu");

        assertTrue(result.isPresent());
        assertEquals("Test User", result.get().getName());
    }
    
    /**
     * Checks to see if a user's profile is updated correctly 
     */
    @Test
    void testUpdatingUserInfo() {
    	User user = new User("Test User", "test@ncsu.edu");
    	user.updateProfile("John Doe", "CSC", "2000", "Math is cool", "cool png");
    	userService.updateUser(user);
    	
    	 when(userRepository.findByEmail("test@ncsu.edu"))
         .thenReturn(Optional.of(user));
    	 
    	 Optional<User> result =
                 userService.getUserByEmail("test@ncsu.edu");
    	 
    	 assertTrue(result.isPresent());
         assertEquals("John Doe", result.get().getName());
         assertEquals("CSC", result.get().getMajor());
         assertEquals("2000", result.get().getYear());
         assertEquals("Math is cool", result.get().getBio());
         assertEquals("cool png", result.get().getProfilePicture());
    }
    

    
}