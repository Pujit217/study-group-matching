package edu.ncsu.acm.studygroup.controller;

import edu.ncsu.acm.studygroup.entity.Course;
import edu.ncsu.acm.studygroup.entity.StudyGroup;
import edu.ncsu.acm.studygroup.entity.User;
import edu.ncsu.acm.studygroup.service.CourseService;
import edu.ncsu.acm.studygroup.service.MatchingService;
import edu.ncsu.acm.studygroup.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Provides REST API endpoints for the rules-based
 * study-group matching system.
 */
@RestController
@RequestMapping("/api/matching")
public class MatchingController {

    /**
     * Service containing the matching logic.
     */
    private final MatchingService matchingService;

    /**
     * Service used to locate users.
     */
    private final UserService userService;

    /**
     * Service used to locate courses.
     */
    private final CourseService courseService;

    /**
     * Creates the controller using constructor injection.
     *
     * @param matchingService matching service
     * @param userService user service
     * @param courseService course service
     */
    public MatchingController(
            final MatchingService matchingService,
            final UserService userService,
            final CourseService courseService) {

        this.matchingService = matchingService;
        this.userService = userService;
        this.courseService = courseService;
    }

    /**
     * Finds compatible study groups for a user
     * within a specific course.
     *
     * @param userId user searching for groups
     * @param courseId course being searched
     * @return ranked study groups or 404
     */
    @GetMapping
    public ResponseEntity<List<StudyGroup>> findMatches(
            @RequestParam final Long userId,
            @RequestParam final Long courseId) {

        final User user = userService
                .getUserById(userId)
                .orElse(null);

        final Course course = courseService
                .getCourseById(courseId)
                .orElse(null);

        if (user == null || course == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(
                matchingService.findMatchingGroups(
                        user,
                        course
                )
        );
    }
}