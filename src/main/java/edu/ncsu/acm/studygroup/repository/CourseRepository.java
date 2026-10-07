package edu.ncsu.acm.studygroup.repository;

import edu.ncsu.acm.studygroup.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Provides database access operations for Course entities.
 */
public interface CourseRepository extends JpaRepository<Course, Long> {

    /**
     * Finds a course by its official course code.
     *
     * @param courseCode course code such as CSC 316
     * @return matching course, if one exists
     */
    Optional<Course> findByCourseCode(String courseCode);
}