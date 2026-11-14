package edu.ncsu.acm.studygroup.service;

import edu.ncsu.acm.studygroup.entity.User;
import edu.ncsu.acm.studygroup.repository.UserRepository;
import edu.ncsu.acm.studygroup.repository.GroupMemberRepository;
import edu.ncsu.acm.studygroup.repository.StudyPreferenceRepository;
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

    private GroupMemberRepository groupMemberRepository;

    /**
     * Service being tested.
     */
    private UserService userService;

    private StudyPreferenceRepository studyPreferenceRepository;

    /**
     * Creates fresh test objects before each test.
     */
    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        groupMemberRepository =
                Mockito.mock(GroupMemberRepository.class);
        studyPreferenceRepository =
                Mockito.mock(StudyPreferenceRepository.class);

        userService = new UserService(
                userRepository,
                groupMemberRepository,
                studyPreferenceRepository
        );
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
}