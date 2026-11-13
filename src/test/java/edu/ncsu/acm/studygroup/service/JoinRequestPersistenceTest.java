
package edu.ncsu.acm.studygroup.service;

import edu.ncsu.acm.studygroup.entity.*;
import edu.ncsu.acm.studygroup.repository.*;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class JoinRequestPersistenceTest {

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

    @Autowired
    private JoinRequestService joinRequestService;

    @Autowired
    private EntityManager entityManager;

    @Test
    void shouldSaveMembershipWhenRequestIsAccepted() {

        // 1. Create two real users in the test database
        String uniqueId = UUID.randomUUID().toString();

        User creator = userRepository.save(
                new User("Group Creator",
                        "creator-" + uniqueId + "@example.com")
        );

        User student = userRepository.save(
                new User("Sakshi",
                        "student-" + uniqueId + "@example.com")
        );

        // 2. Create a course
        Course course = courseRepository.save(
                new Course(
                        "CSC-TEST-" + uniqueId.substring(0, 8),
                        "Test Course",
                        "Computer Science")
        );

        // 3. Create a study group
        StudyGroup group = new StudyGroup(
                "Test Study Group", course, creator);

        group.setMaxSize(3);
        group = studyGroupRepository.save(group);

        // 4. Create a pending join request
        JoinRequest request = new JoinRequest(
                student, group, "I want to join");

        request = joinRequestRepository.save(request);

        Long requestId = request.getRequestId();
        Long studentId = student.getUserId();
        Long groupId = group.getGroupId();

        // 5. Write data to the test database
        entityManager.flush();
        entityManager.clear();

        // 6. Reload the request from the database
        JoinRequest savedRequest = joinRequestRepository
                .findById(requestId)
                .orElseThrow();

        // 7. Accept the request using your service
        joinRequestService.acceptRequest(savedRequest);

        // 8. Write changes and clear cached objects
        entityManager.flush();
        entityManager.clear();

        // 9. Reload the accepted request
        JoinRequest acceptedRequest = joinRequestRepository
                .findById(requestId)
                .orElseThrow();

        assertEquals(
                "ACCEPTED",
                acceptedRequest.getStatus()
        );

        // 10. Reload the student and group
        User savedStudent = userRepository
                .findById(studentId)
                .orElseThrow();

        StudyGroup savedGroup = studyGroupRepository
                .findById(groupId)
                .orElseThrow();

        // 11. Verify membership exists in database
        GroupMember membership = groupMemberRepository
                .findByUserAndStudyGroup(
                        savedStudent, savedGroup)
                .orElseThrow();

        assertNotNull(membership.getMemberId());
        assertNotNull(membership.getJoinedAt());
    }
}
