package edu.ncsu.acm.studygroup.repository;

import edu.ncsu.acm.studygroup.entity.StudyPreference;
import edu.ncsu.acm.studygroup.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Provides database access operations for StudyPreference entities.
 */
public interface StudyPreferenceRepository
        extends JpaRepository<StudyPreference, Long> {

    /**
     * Finds the study-preference profile belonging to a user.
     *
     * @param user user whose preferences should be returned
     * @return matching preference profile, if one exists
     */
    Optional<StudyPreference> findByUser(User user);
}