package edu.ncsu.acm.studygroup.controller;

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
     * Service used to perform user-related operations.
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
}