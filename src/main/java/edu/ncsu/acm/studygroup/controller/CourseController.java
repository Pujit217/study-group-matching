package edu.ncsu.acm.studygroup.controller;

import edu.ncsu.acm.studygroup.entity.Course;
import edu.ncsu.acm.studygroup.entity.Section;
import edu.ncsu.acm.studygroup.service.CourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Provides REST API endpoints for courses and course sections.
 */
@RestController
@RequestMapping("/api/courses")
public class CourseController {

    /**
     * Service used to perform course-related operations.
     */
    private final CourseService courseService;

    /**
     * Creates the controller using constructor injection.
     *
     * @param courseService service used for course operations
     */
    public CourseController(final CourseService courseService) {
        this.courseService = courseService;
    }

    /**
     * Returns all courses.
     *
     * @return list of courses
     */
    @GetMapping
    public List<Course> getAllCourses() {
        return courseService.getAllCourses();
    }

    /**
     * Returns one course by database ID.
     *
     * @param courseId course ID
     * @return matching course or 404
     */
    @GetMapping("/{courseId}")
    public ResponseEntity<Course> getCourseById(
            @PathVariable final Long courseId) {

        return courseService.getCourseById(courseId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Creates a new course.
     *
     * @param course course information
     * @return newly created course
     */
    @PostMapping
    public ResponseEntity<Course> createCourse(
            @RequestBody final Course course) {

        return ResponseEntity.ok(
                courseService.createCourse(course)
        );
    }

    /**
     * Returns all sections belonging to a course.
     *
     * @param courseId course ID
     * @return list of sections or 404
     */
    @GetMapping("/{courseId}/sections")
    public ResponseEntity<List<Section>> getSections(
            @PathVariable final Long courseId) {

        return courseService.getCourseById(courseId)
                .map(course ->
                        ResponseEntity.ok(
                                courseService
                                        .getSectionsForCourse(course)
                        )
                )
                .orElse(ResponseEntity.notFound().build());
    }
}