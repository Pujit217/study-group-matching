package edu.ncsu.acm.studygroup.repository;

import edu.ncsu.acm.studygroup.entity.Course;
import edu.ncsu.acm.studygroup.entity.Section;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Provides database access operations for Section entities.
 */
public interface SectionRepository extends JpaRepository<Section, Long> {

    /**
     * Finds all sections belonging to a specific course.
     *
     * @param course course whose sections should be returned
     * @return list of matching sections
     */
    List<Section> findByCourse(Course course);
}