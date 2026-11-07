package edu.ncsu.acm.studygroup.controller;

import edu.ncsu.acm.studygroup.entity.Course;
import edu.ncsu.acm.studygroup.entity.Enrollment;
import edu.ncsu.acm.studygroup.entity.Section;
import edu.ncsu.acm.studygroup.service.CourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/api/courses")
    public List<Course> getAllCourses() {
        return courseService.getAllCourses();
    }

    @GetMapping("/api/courses/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable Long id) {
        return courseService.getCourseById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/api/courses/{id}/sections")
    public List<Section> getCourseSections(@PathVariable Long id) {
        return courseService.getSectionsForCourseId(id);
    }

    @PostMapping("/api/courses/enroll")
    public ResponseEntity<?> enrollUser(@RequestParam Long userId,
                                        @RequestParam Long courseId,
                                        @RequestParam Long sectionId) {
        try {
            Enrollment enrollment = courseService.enrollUser(userId, courseId, sectionId);
            return ResponseEntity.ok(enrollment);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/api/users/{userId}/enrollments")
    public List<Enrollment> getUserEnrollments(@PathVariable Long userId) {
        return courseService.getUserEnrollments(userId);
    }
}