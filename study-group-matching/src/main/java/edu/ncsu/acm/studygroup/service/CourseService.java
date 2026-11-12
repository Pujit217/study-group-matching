package edu.ncsu.acm.studygroup.service;

import edu.ncsu.acm.studygroup.entity.Course;
import edu.ncsu.acm.studygroup.entity.Section;
import edu.ncsu.acm.studygroup.repository.CourseRepository;
import edu.ncsu.acm.studygroup.repository.SectionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Handles business logic related to courses and course sections.
 */
@Service
public class CourseService {

    /**
     * Repository used to access Course records.
     */
    private final CourseRepository courseRepository;

    /**
     * Repository used to access Section records.
     */
    private final SectionRepository sectionRepository;

    /**
     * Creates the service using constructor injection.
     *
     * @param courseRepository repository used for course persistence
     * @param sectionRepository repository used for section persistence
     */
    public CourseService(final CourseRepository courseRepository,
                         final SectionRepository sectionRepository) {
        this.courseRepository = courseRepository;
        this.sectionRepository = sectionRepository;
    }

    /**
     * Returns all courses currently stored in the database.
     *
     * @return list of all courses
     */
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    /**
     * Finds a course using its database identifier.
     *
     * @param courseId course ID
     * @return matching course, if one exists
     */
    public Optional<Course> getCourseById(final Long courseId) {
        return courseRepository.findById(courseId);
    }

    /**
     * Finds a course using its official course code.
     *
     * @param courseCode course code such as CSC 316
     * @return matching course, if one exists
     */
    public Optional<Course> getCourseByCode(final String courseCode) {
        return courseRepository.findByCourseCode(courseCode);
    }

    /**
     * Creates and stores a new course.
     *
     * @param course course to save
     * @return saved course
     */
    public Course createCourse(final Course course) {
        return courseRepository.save(course);
    }

    /**
     * Saves changes made to an existing course.
     *
     * @param course course containing updated information
     * @return updated course
     */
    public Course updateCourse(final Course course) {
        return courseRepository.save(course);
    }

    /**
     * Deletes a course using its database identifier.
     *
     * @param courseId course ID
     */
    public void deleteCourse(final Long courseId) {
        courseRepository.deleteById(courseId);
    }

    /**
     * Returns all sections associated with a course.
     *
     * @param course course whose sections should be returned
     * @return list of course sections
     */
    public List<Section> getSectionsForCourse(final Course course) {
        return sectionRepository.findByCourse(course);
    }

    /**
     * Creates and stores a new course section.
     *
     * @param section section to save
     * @return saved section
     */
    public Section createSection(final Section section) {
        return sectionRepository.save(section);
    }

    /**
     * Finds a section using its database identifier.
     *
     * @param sectionId section ID
     * @return matching section, if one exists
     */
    public Optional<Section> getSectionById(final Long sectionId) {
        return sectionRepository.findById(sectionId);
    }
}