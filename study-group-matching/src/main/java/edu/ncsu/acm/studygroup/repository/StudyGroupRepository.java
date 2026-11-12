package edu.ncsu.acm.studygroup.repository;

import edu.ncsu.acm.studygroup.entity.Course;
import edu.ncsu.acm.studygroup.entity.Section;
import edu.ncsu.acm.studygroup.entity.StudyGroup;
import edu.ncsu.acm.studygroup.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Provides database access operations for StudyGroup entities.
 */
public interface StudyGroupRepository
        extends JpaRepository<StudyGroup, Long> {

    /**
     * Finds all study groups associated with a specific course.
     *
     * @param course course whose study groups should be returned
     * @return list of matching study groups
     */
    List<StudyGroup> findByCourse(Course course);

    /**
     * Finds all study groups associated with a specific section.
     *
     * @param section section whose study groups should be returned
     * @return list of matching study groups
     */
    List<StudyGroup> findBySection(Section section);

    /**
     * Finds all study groups created by a specific user.
     *
     * @param createdBy user who created the study groups
     * @return list of matching study groups
     */
    List<StudyGroup> findByCreatedBy(User createdBy);
}