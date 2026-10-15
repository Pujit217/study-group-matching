package edu.ncsu.acm.studygroup.repository;

import edu.ncsu.acm.studygroup.entity.Course;
import edu.ncsu.acm.studygroup.entity.Enrollment;
import edu.ncsu.acm.studygroup.entity.Section;
import edu.ncsu.acm.studygroup.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Provides database access operations for Enrollment entities.
 */
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    /**
     * Finds all enrollments belonging to a specific user.
     *
     * @param user user whose enrollments should be returned
     * @return list of matching enrollments
     */
    List<Enrollment> findByUser(User user);

    /**
     * Finds all enrollments associated with a specific course.
     *
     * @param course course whose enrollments should be returned
     * @return list of matching enrollments
     */
    List<Enrollment> findByCourse(Course course);

    /**
     * Finds all enrollments associated with a specific section.
     *
     * @param section section whose enrollments should be returned
     * @return list of matching enrollments
     */
    List<Enrollment> findBySection(Section section);
}