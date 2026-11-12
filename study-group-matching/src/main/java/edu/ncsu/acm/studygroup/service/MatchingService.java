package edu.ncsu.acm.studygroup.service;

import edu.ncsu.acm.studygroup.entity.Course;
import edu.ncsu.acm.studygroup.entity.StudyGroup;
import edu.ncsu.acm.studygroup.entity.StudyPreference;
import edu.ncsu.acm.studygroup.entity.User;
import edu.ncsu.acm.studygroup.repository.StudyGroupRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

/**
 * Provides the initial rules-based matching logic for the application.
 * Matching is intentionally simple for the MVP and can be improved later.
 */
@Service
public class MatchingService {

    /**
     * Repository used to retrieve study groups available for matching.
     */
    private final StudyGroupRepository studyGroupRepository;

    /**
     * Creates the service using constructor injection.
     *
     * @param studyGroupRepository repository used to access study groups
     */
    public MatchingService(
            final StudyGroupRepository studyGroupRepository) {
        this.studyGroupRepository = studyGroupRepository;
    }

    /**
     * Returns study groups associated with the requested course.
     * Full groups are excluded from the matching results.
     *
     * @param course course for which groups should be found
     * @return available study groups
     */
    public List<StudyGroup> findAvailableGroups(
            final Course course) {
        return studyGroupRepository.findByCourse(course)
                .stream()
                .filter(group -> !group.isFull())
                .toList();
    }

    /**
     * Calculates a simple match score between a user and a study group.
     * The score compares the user's study preferences with group settings.
     *
     * @param user       user being matched
     * @param studyGroup potential study group
     * @return compatibility score between 0.0 and 1.0
     */
    public double calculateGroupMatchScore(
            final User user,
            final StudyGroup studyGroup) {

        if (user == null || studyGroup == null) {
            return 0.0;
        }

        final StudyPreference preference =
                user.getPreferences();

        if (preference == null) {
            return 0.0;
        }

        double score = 0.0;
        int comparisons = 0;

        /**
         * Compares preferred study mode with the group's meeting mode.
         */
        if (preference.getStudyMode() != null
                && studyGroup.getStudyMode() != null) {
            comparisons++;

            if (preference.getStudyMode()
                    .equalsIgnoreCase(studyGroup.getStudyMode())
                    || preference.getStudyMode()
                    .equalsIgnoreCase("Both")
                    || studyGroup.getStudyMode()
                    .equalsIgnoreCase("Both")) {
                score++;
            }
        }

        /**
         * Compares the user's preferred group size with
         * the maximum size of the study group.
         */
        if (preference.getPreferredGroupSize() > 0
                && studyGroup.getMaxSize() > 0) {
            comparisons++;

            if (Math.abs(
                    preference.getPreferredGroupSize()
                            - studyGroup.getMaxSize()) <= 1) {
                score++;
            }
        }

        /**
         * Gives partial compatibility when the group still has space.
         */
        comparisons++;

        if (!studyGroup.isFull()) {
            score++;
        }

        if (comparisons == 0) {
            return 0.0;
        }

        return score / comparisons;
    }

    /**
     * Returns available study groups sorted from highest
     * compatibility score to lowest.
     *
     * @param user   user searching for a study group
     * @param course course being searched
     * @return ranked list of compatible study groups
     */
    public List<StudyGroup> findMatchingGroups(
            final User user,
            final Course course) {

        return findAvailableGroups(course)
                .stream()
                .sorted(
                        Comparator.comparingDouble(
                                (StudyGroup group) ->
                                        calculateGroupMatchScore(
                                                user,
                                                group
                                        )
                        ).reversed()
                )
                .toList();
    }
}