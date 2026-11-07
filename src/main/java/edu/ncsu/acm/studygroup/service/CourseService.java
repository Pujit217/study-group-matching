package edu.ncsu.acm.studygroup.service;

import edu.ncsu.acm.studygroup.entity.Course;
import edu.ncsu.acm.studygroup.entity.Enrollment;
import edu.ncsu.acm.studygroup.entity.Section;
import edu.ncsu.acm.studygroup.entity.User;
import edu.ncsu.acm.studygroup.repository.CourseRepository;
import edu.ncsu.acm.studygroup.repository.EnrollmentRepository;
import edu.ncsu.acm.studygroup.repository.SectionRepository;
import edu.ncsu.acm.studygroup.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Handles business logic related to courses, sections, and user enrollments.
 */
@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final SectionRepository sectionRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;

    public CourseService(final CourseRepository courseRepository,
                         final SectionRepository sectionRepository,
                         final EnrollmentRepository enrollmentRepository,
                         final UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.sectionRepository = sectionRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.userRepository = userRepository;
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Optional<Course> getCourseById(final Long courseId) {
        return courseRepository.findById(courseId);
    }

    public Optional<Course> getCourseByCode(final String courseCode) {
        return courseRepository.findByCourseCode(courseCode);
    }

    public Course createCourse(final Course course) {
        return courseRepository.save(course);
    }

    public Course updateCourse(final Course course) {
        return courseRepository.save(course);
    }

    public void deleteCourse(final Long courseId) {
        courseRepository.deleteById(courseId);
    }

    // Notice we updated this method to use the custom query we wrote in the repository
    public List<Section> getSectionsForCourseId(final Long courseId) {
        return sectionRepository.findByCourse_CourseId(courseId);
    }

    public Section createSection(final Section section) {
        return sectionRepository.save(section);
    }

    public Optional<Section> getSectionById(final Long sectionId) {
        return sectionRepository.findById(sectionId);
    }

    // --- ENROLLMENT LOGIC ---

    /**
     * Enrolls a user in a specific section of a course.
     * Prevents duplicate enrollments and verifies the section belongs to the course.
     */
    @Transactional
    public Enrollment enrollUser(Long userId, Long courseId, Long sectionId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));
        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new IllegalArgumentException("Section not found"));

        if (!section.getCourse().getCourseId().equals(course.getCourseId())) {
            throw new IllegalArgumentException("This section does not belong to the specified course");
        }

        Optional<Enrollment> existingEnrollment = enrollmentRepository.findByUser_UserIdAndCourse_CourseId(userId, courseId);
        if (existingEnrollment.isPresent()) {
            throw new IllegalStateException("User is already enrolled in this course");
        }

        Enrollment enrollment = new Enrollment(user, course, section);
        return enrollmentRepository.save(enrollment);
    }

    /**
     * Retrieves all enrollments for a specific user.
     */
    public List<Enrollment> getUserEnrollments(Long userId) {
        return enrollmentRepository.findByUser_UserId(userId);
    }
}