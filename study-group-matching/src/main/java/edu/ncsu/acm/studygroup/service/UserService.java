package edu.ncsu.acm.studygroup.service;

import edu.ncsu.acm.studygroup.entity.User;
import edu.ncsu.acm.studygroup.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Handles business logic related to users.
 * This service sits between controllers and the UserRepository.
 */
@Service
public class UserService {

    /**
     * Repository used to access and modify User records.
     */
    private final UserRepository userRepository;

    /**
     * Creates the service using constructor injection.
     *
     * @param userRepository repository used for user persistence
     */
    public UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Returns all users currently stored in the database.
     *
     * @return list of all users
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Finds a user using the user's database identifier.
     *
     * @param userId ID of the user
     * @return matching user, if one exists
     */
    public Optional<User> getUserById(final Long userId) {
        return userRepository.findById(userId);
    }

    /**
     * Finds a user by email address.
     *
     * @param email email address to search for
     * @return matching user, if one exists
     */
    public Optional<User> getUserByEmail(final String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Finds a user by NC State identifier.
     *
     * @param ncsuId NC State identifier to search for
     * @return matching user, if one exists
     */
    public Optional<User> getUserByNcsuId(final String ncsuId) {
        return userRepository.findByNcsuId(ncsuId);
    }

    /**
     * Creates and stores a new user.
     *
     * @param user user to save
     * @return saved user
     */
    public User createUser(final User user) {
        return userRepository.save(user);
    }

    /**
     * Saves changes made to an existing user.
     *
     * @param user user containing updated information
     * @return updated user
     */
    public User updateUser(final User user) {
        return userRepository.save(user);
    }

    /**
     * Deletes a user by database identifier.
     *
     * @param userId ID of the user to delete
     */
    public void deleteUser(final Long userId) {
        userRepository.deleteById(userId);
    }

    /**
     * Checks whether a user exists with the given email address.
     *
     * @param email email address to check
     * @return true if a matching user exists
     */
    public boolean emailExists(final String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    /**
     * Checks whether a user exists with the given NC State identifier.
     *
     * @param ncsuId NC State identifier to check
     * @return true if a matching user exists
     */
    public boolean ncsuIdExists(final String ncsuId) {
        return userRepository.findByNcsuId(ncsuId).isPresent();
    }
}