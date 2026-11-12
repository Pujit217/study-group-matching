package edu.ncsu.acm.studygroup.service;

import edu.ncsu.acm.studygroup.entity.Course;
import edu.ncsu.acm.studygroup.entity.Section;
import edu.ncsu.acm.studygroup.entity.StudyGroup;
import edu.ncsu.acm.studygroup.entity.User;
import edu.ncsu.acm.studygroup.repository.StudyGroupRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Handles business logic related to creating, viewing,
 * updating, and managing study groups.
 */
@Service
public class StudyGroupService {

    /**
     * Repository used to access StudyGroup records.
     */
    private final StudyGroupRepository studyGroupRepository;

    /**
     * Creates the service using constructor injection.
     *
     * @param studyGroupRepository repository used for study-group persistence
     */
    public StudyGroupService(
            final StudyGroupRepository studyGroupRepository) {
        this.studyGroupRepository = studyGroupRepository;
    }

    /**
     * Returns all study groups currently stored in the database.
     *
     * @return list of all study groups
     */
    public List<StudyGroup> getAllStudyGroups() {
        return studyGroupRepository.findAll();
    }

    /**
     * Finds a study group using its database identifier.
     *
     * @param groupId study-group ID
     * @return matching study group, if one exists
     */
    public Optional<StudyGroup> getStudyGroupById(
            final Long groupId) {
        return studyGroupRepository.findById(groupId);
    }

    /**
     * Creates and stores a new study group.
     *
     * @param studyGroup study group to save
     * @return saved study group
     */
    public StudyGroup createStudyGroup(
            final StudyGroup studyGroup) {
        return studyGroupRepository.save(studyGroup);
    }

    /**
     * Saves changes made to an existing study group.
     *
     * @param studyGroup group containing updated information
     * @return updated study group
     */
    public StudyGroup updateStudyGroup(
            final StudyGroup studyGroup) {
        return studyGroupRepository.save(studyGroup);
    }

    /**
     * Deletes a study group using its database identifier.
     *
     * @param groupId study-group ID
     */
    public void deleteStudyGroup(final Long groupId) {
        studyGroupRepository.deleteById(groupId);
    }

    /**
     * Returns all study groups associated with a course.
     *
     * @param course target course
     * @return matching study groups
     */
    public List<StudyGroup> getStudyGroupsByCourse(
            final Course course) {
        return studyGroupRepository.findByCourse(course);
    }

    /**
     * Returns study groups associated with a specific section.
     *
     * @param section target course section
     * @return matching study groups
     */
    public List<StudyGroup> getStudyGroupsBySection(
            final Section section) {
        return studyGroupRepository.findBySection(section);
    }

    /**
     * Returns study groups created by a specific user.
     *
     * @param user group creator
     * @return study groups created by the user
     */
    public List<StudyGroup> getStudyGroupsCreatedBy(
            final User user) {
        return studyGroupRepository.findByCreatedBy(user);
    }

    /**
     * Checks whether a study group has room for another member.
     *
     * @param studyGroup group being checked
     * @return true if another member can join
     */
    public boolean hasAvailableSpace(
            final StudyGroup studyGroup) {
        return !studyGroup.isFull();
    }
}