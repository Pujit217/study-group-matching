package edu.ncsu.acm.studygroup.controller;

import edu.ncsu.acm.studygroup.dto.StudyPreferenceDto;
import edu.ncsu.acm.studygroup.entity.StudyPreference;
import edu.ncsu.acm.studygroup.entity.User;
import edu.ncsu.acm.studygroup.repository.StudyPreferenceRepository;
import edu.ncsu.acm.studygroup.repository.UserRepository;
import edu.ncsu.acm.studygroup.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests basic UserService behavior using a mocked repository.
 */
class UserServiceTest {

    /**
     * Mock user repository used so the test does not require a real database.
     */
    private UserRepository userRepository;
    
    /**
     * Mock study preference repository used so the test does not require
     *  a real database.
     */
    private StudyPreferenceRepository studyPreferenceRepository;

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
        studyPreferenceRepository = Mockito.mock(StudyPreferenceRepository.class);
        userService = new UserService(userRepository, studyPreferenceRepository);
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
     * Created using Gemini. Tests that creating a preference with 
     * a valid user id works as intended
     */
    @Test
    void createPreference() {
    	StudyPreferenceDto dto = new StudyPreferenceDto();
        dto.setUserId(1L);

        User user = new User("Test User", "test@ncsu.edu");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(studyPreferenceRepository.save(any(StudyPreference.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Optional<StudyPreference> result = userService.createPreference(dto);

        assertTrue(result.isPresent());
        verify(studyPreferenceRepository).save(any(StudyPreference.class));
    }
    
    /**
     * Created using Gemini. Tests that creating a preference that
     * doesn't have a user id works correctly
     */
    @Test
    void createPreferenceUserNot() {
        StudyPreferenceDto dto = new StudyPreferenceDto();
        dto.setUserId(99L);

        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<StudyPreference> result = userService.createPreference(dto);

        assertTrue(result.isEmpty());
        // Ensure save was NEVER called because the user didn't exist
        verify(studyPreferenceRepository, never()).save(any());
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
    
    @Test
    void testUpdatingPreferenceInfo() {
    	User user = new User("Test User", "test@ncsu.edu");
    	StudyPreference s = new StudyPreference(user);
    	s.updatePreferences("MW 11-2", "With music",7, "talkative", "Enjoys jazz");
    	
    	when(studyPreferenceRepository.findByUser(user))
        .thenReturn(Optional.of(s));
    	
    	Optional<StudyPreference> result = 
    			userService.getPreferenceByUser(user);
    	
    	assertTrue(result.isPresent());
    	assertEquals("MW 11-2", result.get().getAvailability());
    	assertEquals("With music", result.get().getStudyStyle());
    	assertEquals(7, result.get().getPreferredGroupSize());
    	assertEquals("talkative", result.get().getStudyMode());
    	assertEquals("Enjoys jazz", result.get().getAdditionalNotes());
    }
    
    /**
     * Tests that getPreferenceByUserId works correctly
     */
    @Test
    void testGetPreferenceByUserId() {
    	User user = new User("Test User", "test@ncsu.edu");
    	StudyPreference preference = new StudyPreference(user);
    	user.setStudyPreference(preference);
    	Long id = (long) 100000;
    	user.setUserId(id);
    	user.getStudyPreference().updatePreferences("MW", "group", 3, "lock in", "have fun");
    	when(userRepository.findById(id))
        .thenReturn(Optional.of(user));
    	when(studyPreferenceRepository.findByUser(user))
        .thenReturn(Optional.of(preference));
    	Optional<StudyPreference> result1 =
                userService.getPreferenceByUserId(user.getUserId());
    	Optional<StudyPreference> result2 =
                userService.getPreferenceByUserId((long)00000);
    	assertTrue(result2.isEmpty());
    	assertTrue(result1.isPresent());
    	assertEquals("MW", result1.get().getAvailability());
        assertEquals("group", result1.get().getStudyStyle());
        assertEquals(3, result1.get().getPreferredGroupSize());
        assertEquals("lock in", result1.get().getStudyMode());
        assertEquals("have fun", result1.get().getAdditionalNotes());
    }
    
    /**
     * Created by Gemini. Tests that a user is deleted from the
     * repository
     */
    @Test
    void testDeleteUserCallsDeleteById() {
        Long userId = 1L;

        userService.deleteUser(userId);

        // Verifies that the service instructed the repository to delete ID 1
        verify(userRepository).deleteById(userId);
    }
    
    /**
     * Tests that a preference is deleted from the 
     * repository
     */
    @Test
    void testDeletePreferenceById() {
    	Long preferenceId = 1L;
    	
    	userService.deletePreference(preferenceId);
    	
    	verify(studyPreferenceRepository).deleteById(preferenceId);
    }

    
}