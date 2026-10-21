package edu.ncsu.acm.studygroup.repository;

import edu.ncsu.acm.studygroup.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Provides database access operations for User entities.
 */
public interface UserRepository extends JpaRepository<User, Long> {

	/**
	 * TODO
	 * 
	 * Add in deleteById, save, findById, createUser, deleteUser, and findAll
	 * 
	 * findall, find by id, save, deleteById, 
	 */
	
    /**
     * Finds a user by email address.
     *
     * @param email email address to search for
     * @return matching user, if one exists
     */
    Optional<User> findByEmail(String email);

    /**
     * Finds a user by NC State identifier.
     *
     * @param ncsuId NC State identifier to search for
     * @return matching user, if one exists
     */
    Optional<User> findByNcsuId(String ncsuId);
    
}