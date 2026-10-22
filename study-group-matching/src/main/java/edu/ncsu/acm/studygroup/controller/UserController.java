package edu.ncsu.acm.studygroup.controller;

import edu.ncsu.acm.studygroup.entity.StudyPreference;
import edu.ncsu.acm.studygroup.entity.User;
import edu.ncsu.acm.studygroup.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Provides REST API endpoints for managing users.
 * Requests are passed to UserService, which handles the application logic.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

	/**
	 * TODO 
	 * 
	 * Add in study preference functionality to user controller
     */
    private final UserService userService;

    /**
     * Creates the controller using constructor injection.
     *
     * @param userService service used for user operations
     */
    public UserController(final UserService userService) {
        this.userService = userService;
    }

    /**
     * Returns all users currently stored in the application.
     *
     * @return list of users
     */
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
    
    /**
     * Returns all study preferences currently stored in the application.
     *
     * @return list of study preferences
     */
    @GetMapping
    public List<StudyPreference> getAllPreferences() {
        return userService.getAllPreferences();
    }

    /**
     * Returns one user using the user's database ID.
     *
     * @param userId user ID
     * @return matching user or 404 if the user does not exist
     */
    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(
            @PathVariable final Long userId) {

        return userService.getUserById(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    //Developed after asking Gemini what are potentially missing from the repository
    // controller, and service
    /**
     * Finds a user based on their email and returns it
     * 
     * @param email the email to get a user with
     * @return the matching user 404 if a user doesn't have said email
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(
    		@PathVariable final String email) {
    	
    	return userService.getUserByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    //Developed after asking Gemini what are potentially missing from the repository
    // controller, and service
    /**
     * Finds a user based on their NCSU Id and returns it
     * 
     * @param ncsuID the NCSU Id to get a user with
     * @return the matching user 404 if a user doesn't have said email
     */
    @GetMapping("/ncsu/{ncsuId}")
    public ResponseEntity<User> getUserByNcsuId(
    		@PathVariable final String ncsuId) {
    	
    	return userService.getUserByNcsuId(ncsuId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Finds a study preference based on its associated user and returns it
     * 
     * @param user the user to get the study preference with
     * @return the matching user 404 if a study preference doesn't have an
     * associated user
     */
    @GetMapping("/{preferenceId}")
    public ResponseEntity<StudyPreference> getPreferenceByUser(
    		@PathVariable final User user) {
    	
    	return userService.getPreferenceByUser(user)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Creates a new user.
     *
     * @param user user information received from the request
     * @return newly created user
     */
    @PostMapping
    public ResponseEntity<User> createUser(
            @RequestBody final User user) {

        final User createdUser =
                userService.createUser(user);

        return ResponseEntity.ok(createdUser);
    }
    
    /**
     * Creates a new study preference.
     *
     * @param preference study preference information received from the request
     * @return newly created study preference
     */
    @PostMapping
    public ResponseEntity<StudyPreference> createPreference(
            @RequestBody final StudyPreference preference) {

        final StudyPreference createdPreference =
                userService.createPreference(preference);

        return ResponseEntity.ok(createdPreference);
    }

    /**
     * Updates an existing user.
     *
     * @param userId ID of the user being updated
     * @param updatedUser new user information
     * @return updated user or 404 if the user does not exist
     */
    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(
            @PathVariable final Long userId,
            @RequestBody final User updatedUser) {

        return userService.getUserById(userId)
                .map(existingUser -> {
                    existingUser.updateProfile(
                            updatedUser.getName(),
                            updatedUser.getMajor(),
                            updatedUser.getYear(),
                            updatedUser.getBio(),
                            updatedUser.getProfilePicture()
                    );

                    return ResponseEntity.ok(
                            userService.updateUser(existingUser)
                    );
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Updates an existing study preference.
     *
     * @param preferenceId ID of the study preference being updated
     * @param updatedPreference new study preference information
     * @return updated study preference or 404 if the study preference
     * does not exist
     */
    @PutMapping("/{preferenceId}")
    public ResponseEntity<StudyPreference> updatePreference(
            @PathVariable final Long preferenceId,
            @RequestBody final StudyPreference updatedPreference) {

        return userService.getPreferenceById(preferenceId)
                .map(existingPreference -> {
                    existingPreference.updatePreferences(
                            updatedPreference.getAvailability(),
                            updatedPreference.getStudyStyle(),
                            updatedPreference.getPreferredGroupSize(),
                            updatedPreference.getStudyMode(),
                            updatedPreference.getAdditionalNotes()
                    );

                    return ResponseEntity.ok(
                            userService.updatePreference(existingPreference)
                    );
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Deletes a user by ID.
     *
     * @param userId ID of the user being deleted
     * @return 204 response after successful deletion
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable final Long userId) {

        if (userService.getUserById(userId).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        userService.deleteUser(userId);

        return ResponseEntity.noContent().build();
    }
    
    /**
     * Deletes a study preference by ID.
     *
     * @param preferenceId ID of the study preference being deleted
     * @return 204 response after successful deletion
     */
    @DeleteMapping("/{preferenceId}")
    public ResponseEntity<Void> deletePreference(
            @PathVariable final Long preferenceId) {

        if (userService.getPreferenceById(preferenceId).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        userService.deletePreference(preferenceId);

        return ResponseEntity.noContent().build();
    }
    
    
}