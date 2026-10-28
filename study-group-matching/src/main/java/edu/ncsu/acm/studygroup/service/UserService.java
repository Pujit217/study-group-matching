package edu.ncsu.acm.studygroup.service;

import edu.ncsu.acm.studygroup.dto.CreateUserDto;
import edu.ncsu.acm.studygroup.dto.StudyPreferenceDto;
import edu.ncsu.acm.studygroup.entity.StudyPreference;
import edu.ncsu.acm.studygroup.entity.User;
import edu.ncsu.acm.studygroup.repository.StudyPreferenceRepository;
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
     * Repository for accessing and modifying Study Preference records
     */
    private final StudyPreferenceRepository studyPreferenceRepository;
   
    /*
     * Made an inquiry to Gemini asking if adding the preference repo as below would
     * work due to unfamiliarity. While it works, it may lead to later bugs so I will keep an eye out
     */
    
    /**
     * Creates the service using constructor injection.
     *
     * @param userRepository repository used for user persistence
     * @param studyPreferenceRepository repository used for study preference persistence
     */
    public UserService(final UserRepository userRepository,
    		final StudyPreferenceRepository studyPreferenceRepository) {
        this.userRepository = userRepository;
        this.studyPreferenceRepository = studyPreferenceRepository;
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
     * Returns all study preferences currently stored in the database.
     *
     * @return list of all study preferences
     */
    public List<StudyPreference> getAllPreferences() {
        return studyPreferenceRepository.findAll();
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
     * Finds a study preference associated with a user.
     *
     * @param user the user to search for
     * @return matching study preference, if one exists
     */
    public Optional<StudyPreference> getPreferenceByUser(final User user) {
        return studyPreferenceRepository.findByUser(user);
    }
    
    /**
     * Finds a study preference associated with a user based
     * on the user's ID.
     * 
     * @param userId the user id to 
     * @return the study preference associated with a user, if it exists
     */
    public Optional<StudyPreference> getPreferenceByUserId(final Long userId) {
    	if (userRepository.findById(userId).isPresent()) {
    		User user = userRepository.findById(userId).get();
            return studyPreferenceRepository.findByUser(user);
    	}
    	return Optional.empty();
    	
    }
    
    /**
     * Finds a study preference using the database identifier.
     *
     * @param preferenceId the study preference ID to search for
     * @return matching study preference, if one exists
     */
    public Optional<StudyPreference> getPreferenceById(final Long preferenceId) {
        return studyPreferenceRepository.findById(preferenceId);
    }

    /**
     * Creates and stores a new user.
     *
     * @param user user to save
     * @return saved user
     */
    public User createUser(final CreateUserDto userDto) {
    	User user = new User(userDto.getName(), userDto.getEmail());
        return userRepository.save(user);
    }
    
    //Gemini was used to help handle the case when a user id doesn't exist in the
    //repository
    /**
     * Creates and stores a new study preference.
     *
     * @param preference the study preference to save
     * @return saved study preference
     */
    public Optional<StudyPreference> createPreference(final StudyPreferenceDto preferenceDto) {
        return userRepository.findById(preferenceDto.getUserId())
                .map(user -> {
                    StudyPreference preference = new StudyPreference(user);
                    return studyPreferenceRepository.save(preference);
                });
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
     * Saves changes made to an existing study preference
     *
     * @param preference the updated study preference to save
     * @return saved updated study preference
     */
    public StudyPreference updatePreference(final StudyPreference preference) {
        return studyPreferenceRepository.save(preference);
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
     * Deletes a study preference by database identifier.
     *
     * @param preferenceId ID of the user to delete
     */
    public void deletePreference(final Long preferenceId) {
        studyPreferenceRepository.deleteById(preferenceId);
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