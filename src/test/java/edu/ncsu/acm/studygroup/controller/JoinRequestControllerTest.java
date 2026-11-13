
package edu.ncsu.acm.studygroup.controller;

import edu.ncsu.acm.studygroup.entity.*;
import edu.ncsu.acm.studygroup.repository.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class JoinRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudyGroupRepository studyGroupRepository;

    @Autowired
    private JoinRequestRepository joinRequestRepository;

    @Autowired
    private GroupMemberRepository groupMemberRepository;

    @Test
    void shouldCreateJoinRequestThroughApi() throws Exception {

        // Create a student in the test database
        String uniqueId = UUID.randomUUID().toString();

        User student = userRepository.save(
                new User("Sakshi",
                        "sakshi-" + uniqueId + "@example.com")
        );

        User creator = userRepository.save(
                new User("Group Creator",
                        "creator-" + uniqueId + "@example.com")
        );

        Course course = courseRepository.save(
                new Course(
                        "CSC-" + uniqueId.substring(0, 8),
                        "Test Course",
                        "Computer Science")
        );

        StudyGroup group = new StudyGroup(
                "CSC Study Group", course, creator);
        group.setMaxSize(4);
        group = studyGroupRepository.save(group);

        // JSON request the frontend would send
        String json = """
                {
                    "userId": %d,
                    "studyGroupId": %d,
                    "message": "Can I join?"
                }
                """.formatted(
                student.getUserId(),
                group.getGroupId()
        );

        // Send POST request and check response
        mockMvc.perform(
                        post("/api/join-requests")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.message").value("Can I join?"))
                .andExpect(jsonPath("$.requestId").isNumber());

        // Confirm the request was saved in the test database
        assertEquals(
                1,
                joinRequestRepository.findByUser(student).size()
        );
    }

    @Test
    void shouldReturn404WhenJoinRequestDoesNotExist()
            throws Exception {

        // Try accepting a nonexistent request
        mockMvc.perform(
                        put("/api/join-requests/999999/accept")
                )
                .andExpect(status().isNotFound());

        // Try rejecting a nonexistent request
        mockMvc.perform(
                        put("/api/join-requests/999999/reject")
                )
                .andExpect(status().isNotFound());

        // Try canceling a nonexistent request
        mockMvc.perform(
                        put("/api/join-requests/999999/cancel")
                )
                .andExpect(status().isNotFound());
    }

    private JoinRequest createPendingJoinRequest() {

        // Generate unique values for each test
        String uniqueId = UUID.randomUUID().toString();

        // Create the group creator
        User creator = userRepository.save(
                new User(
                        "Group Creator",
                        "creator-" + uniqueId + "@example.com"
                )
        );

        // Create the student requesting to join
        User student = userRepository.save(
                new User(
                        "Test Student",
                        "student-" + uniqueId + "@example.com"
                )
        );

        // Create a course
        Course course = courseRepository.save(
                new Course(
                        "CSC-" + uniqueId,
                        "Test Course",
                        "Computer Science"
                )
        );

        // Create a study group
        StudyGroup group = new StudyGroup(
                "API Test Group",
                course,
                creator
        );

        group.setMaxSize(3);
        group = studyGroupRepository.save(group);

        // Create a pending join request
        JoinRequest request = new JoinRequest(
                student,
                group,
                "I would like to join!"
        );

        return joinRequestRepository.save(request);
    }

    @Test
    void shouldAcceptJoinRequestThroughApi()
            throws Exception {

        // Create a pending request
        JoinRequest request = createPendingJoinRequest();

        // Send an API request to accept it
        mockMvc.perform(
                        put("/api/join-requests/{id}/accept",
                                request.getRequestId())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ACCEPTED"))
                .andExpect(jsonPath("$.requestId").isNumber());

        // Verify the student became a member
        assertTrue(
                groupMemberRepository
                        .findByUserAndStudyGroup(
                                request.getUser(),
                                request.getStudyGroup()
                        )
                        .isPresent()
        );
    }

    @Test
    void shouldRejectJoinRequestThroughApi()
            throws Exception {

        JoinRequest request = createPendingJoinRequest();

        // Send rejection request
        mockMvc.perform(
                        put("/api/join-requests/{id}/reject",
                                request.getRequestId())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("REJECTED"))
                .andExpect(jsonPath("$.respondedAt").exists());

        // Rejected student must not become a member
        assertTrue(
                groupMemberRepository
                        .findByUserAndStudyGroup(
                                request.getUser(),
                                request.getStudyGroup()
                        )
                        .isEmpty()
        );
    }

    @Test
    void shouldCancelJoinRequestThroughApi()
            throws Exception {

        JoinRequest request = createPendingJoinRequest();

        // Send cancellation request
        mockMvc.perform(
                        put("/api/join-requests/{id}/cancel",
                                request.getRequestId())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELED"))
                .andExpect(jsonPath("$.respondedAt").exists());

        // Canceled student must not become a member
        assertTrue(
                groupMemberRepository
                        .findByUserAndStudyGroup(
                                request.getUser(),
                                request.getStudyGroup()
                        )
                        .isEmpty()
        );
    }

    @Test
    void shouldReturn409WhenAcceptingRejectedRequest()
            throws Exception {

        // Create a pending request
        JoinRequest request = createPendingJoinRequest();

        // Reject it first
        request.reject();
        joinRequestRepository.save(request);

        // Try to accept the rejected request
        mockMvc.perform(
                        put("/api/join-requests/{id}/accept",
                                request.getRequestId())
                )
                .andExpect(status().isConflict())
                .andExpect(
                        jsonPath("$.error")
                                .value("Only pending requests can be accepted")
                );
    }


    @Test
    void shouldReturn409ForDuplicateJoinRequest()
            throws Exception {

        // Create the first pending request
        JoinRequest existingRequest =
                createPendingJoinRequest();

        // Prepare another request from the same student
        String json = """
            {
                "userId": %d,
                "studyGroupId": %d,
                "message": "Can I join again?"
            }
            """.formatted(
                existingRequest.getUser().getUserId(),
                existingRequest.getStudyGroup().getGroupId()
        );

        // Submit the duplicate through the API
        mockMvc.perform(
                        post("/api/join-requests")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").exists());
    }


    @Test
    void shouldReturn400WhenUserIdIsMissing()
            throws Exception {

        // userId is required, but we left it out
        String json = """
            {
                "studyGroupId": 5,
                "message": "Can I join?"
            }
            """;

        mockMvc.perform(
                        post("/api/join-requests")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isBadRequest());
    }

}
