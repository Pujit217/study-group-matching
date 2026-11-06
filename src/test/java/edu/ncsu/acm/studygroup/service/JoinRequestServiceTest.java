
package edu.ncsu.acm.studygroup.service;

import edu.ncsu.acm.studygroup.entity.JoinRequest;
import edu.ncsu.acm.studygroup.entity.StudyGroup;
import edu.ncsu.acm.studygroup.entity.User;
import edu.ncsu.acm.studygroup.repository.JoinRequestRepository;
import edu.ncsu.acm.studygroup.repository.UserRepository;
import edu.ncsu.acm.studygroup.repository.StudyGroupRepository;
import org.junit.jupiter.api.Test;
import edu.ncsu.acm.studygroup.entity.GroupMember;
import edu.ncsu.acm.studygroup.repository.GroupMemberRepository;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JoinRequestServiceTest {

    @Test
    void shouldRejectDuplicatePendingRequest() {

        // Create a fake repository for testing
        JoinRequestRepository repository =
                mock(JoinRequestRepository.class);

        // Create the service we want to test
        UserRepository userRepository =
                mock(UserRepository.class);

        StudyGroupRepository studyGroupRepository =
                mock(StudyGroupRepository.class);


        GroupMemberRepository groupMemberRepository =
                mock(GroupMemberRepository.class);

        JoinRequestService service =
                new JoinRequestService(
                        repository,
                        userRepository,
                        studyGroupRepository,
                        groupMemberRepository);


        // Create a user and study group
        User user = new User();
        StudyGroup group = new StudyGroup();
        user.setUserId(1L);
        group.setGroupId(5L);

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(studyGroupRepository.findById(5L))
                .thenReturn(Optional.of(group));


        // Create a join request
        JoinRequest request = new JoinRequest();
        request.setUser(user);
        request.setStudyGroup(group);

        // Pretend a pending request already exists
        when(repository.existsByUserAndStudyGroupAndStatus(
                user, group, "PENDING"))
                .thenReturn(true);

        // Verify that duplicate requests are rejected
        assertThrows(
                IllegalStateException.class,
                () -> service.createJoinRequest(request)
        );

        // Verify nothing was saved
        verify(repository, never()).save(any(JoinRequest.class));
    }

    @Test
    void shouldCreateJoinRequestWhenNoDuplicateExists() {

        // Step 1: Create a fake repository
        JoinRequestRepository repository =
                mock(JoinRequestRepository.class);

        // Step 2: Create our service
        UserRepository userRepository =
                mock(UserRepository.class);

        StudyGroupRepository studyGroupRepository =
                mock(StudyGroupRepository.class);


        GroupMemberRepository groupMemberRepository =
                mock(GroupMemberRepository.class);

        JoinRequestService service =
                new JoinRequestService(
                        repository,
                        userRepository,
                        studyGroupRepository,
                        groupMemberRepository);


        // Step 3: Create a user and study group
        User user = new User();
        StudyGroup group = new StudyGroup();
        user.setUserId(1L);
        group.setGroupId(5L);

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(studyGroupRepository.findById(5L))
                .thenReturn(Optional.of(group));


        // Step 4: Create a join request
        JoinRequest request = new JoinRequest();
        request.setUser(user);
        request.setStudyGroup(group);

        // Step 5: Pretend no pending request exists
        when(repository.existsByUserAndStudyGroupAndStatus(
                user, group, "PENDING"))
                .thenReturn(false);

        // Step 6: Pretend the repository saves the request
        when(repository.save(request)).thenReturn(request);

        // Step 7: Call the method we're testing
        JoinRequest result = service.createJoinRequest(request);

        // Step 8: Verify correct behavior
        assertNotNull(result);
        assertEquals("PENDING", result.getStatus());

        // Step 9: Verify save was called exactly once
        verify(repository, times(1)).save(request);
    }

    @Test
    void shouldRejectRequestWhenUserDoesNotExist() {

        JoinRequestRepository repository =
                mock(JoinRequestRepository.class);

        UserRepository userRepository =
                mock(UserRepository.class);

        StudyGroupRepository studyGroupRepository =
                mock(StudyGroupRepository.class);


        GroupMemberRepository groupMemberRepository =
                mock(GroupMemberRepository.class);

        JoinRequestService service =
                new JoinRequestService(
                        repository,
                        userRepository,
                        studyGroupRepository,
                        groupMemberRepository);


        User user = new User();
        user.setUserId(999L);

        StudyGroup group = new StudyGroup();
        group.setGroupId(5L);

        JoinRequest request = new JoinRequest();
        request.setUser(user);
        request.setStudyGroup(group);

        when(userRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                JoinRequestNotFoundException.class,
                () -> service.createJoinRequest(request)
        );

        verify(repository, never()).save(any(JoinRequest.class));
    }

    @Test
    void shouldRejectRequestWhenStudyGroupDoesNotExist() {

        JoinRequestRepository repository =
                mock(JoinRequestRepository.class);

        UserRepository userRepository =
                mock(UserRepository.class);

        StudyGroupRepository studyGroupRepository =
                mock(StudyGroupRepository.class);


        GroupMemberRepository groupMemberRepository =
                mock(GroupMemberRepository.class);

        JoinRequestService service =
                new JoinRequestService(
                        repository,
                        userRepository,
                        studyGroupRepository,
                        groupMemberRepository);


        User user = new User();
        user.setUserId(1L);

        StudyGroup group = new StudyGroup();
        group.setGroupId(999L);

        JoinRequest request = new JoinRequest();
        request.setUser(user);
        request.setStudyGroup(group);

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(studyGroupRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                JoinRequestNotFoundException.class,
                () -> service.createJoinRequest(request)
        );

        verify(repository, never()).save(any(JoinRequest.class));
    }

    @Test
    void shouldRejectRequestWhenUserAlreadyMember() {

        // Create fake repositories
        JoinRequestRepository repository =
                mock(JoinRequestRepository.class);

        UserRepository userRepository =
                mock(UserRepository.class);

        StudyGroupRepository studyGroupRepository =
                mock(StudyGroupRepository.class);

        GroupMemberRepository groupMemberRepository =
                mock(GroupMemberRepository.class);

        // Create service with all four repositories
        JoinRequestService service =
                new JoinRequestService(
                        repository,
                        userRepository,
                        studyGroupRepository,
                        groupMemberRepository);

        // Create student and group
        User user = new User();
        user.setUserId(1L);

        StudyGroup group = new StudyGroup();
        group.setGroupId(5L);

        // Create a join request
        JoinRequest request = new JoinRequest();
        request.setUser(user);
        request.setStudyGroup(group);

        // Pretend user and group exist
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(studyGroupRepository.findById(5L))
                .thenReturn(Optional.of(group));


        // Pretend the student is already a member
        GroupMember membership = new GroupMember(user, group);

        when(groupMemberRepository.findByUserAndStudyGroup(
                user, group))
                .thenReturn(Optional.of(membership));

        // Existing members must not create a new request
        assertThrows(
                IllegalStateException.class,
                () -> service.createJoinRequest(request)
        );

        // Verify nothing was saved
        verify(repository, never())
                .save(any(JoinRequest.class));
    }

    @Test
    void shouldNotRejectAlreadyAcceptedRequest() {

        // Create fake repositories
        JoinRequestRepository repository =
                mock(JoinRequestRepository.class);

        UserRepository userRepository =
                mock(UserRepository.class);

        StudyGroupRepository studyGroupRepository =
                mock(StudyGroupRepository.class);

        GroupMemberRepository groupMemberRepository =
                mock(GroupMemberRepository.class);

        // Create service
        JoinRequestService service =
                new JoinRequestService(
                        repository,
                        userRepository,
                        studyGroupRepository,
                        groupMemberRepository);

        // Create an already accepted request
        JoinRequest request = new JoinRequest();
        request.setStatus("ACCEPTED");

        // Verify rejection is not allowed
        assertThrows(
                IllegalStateException.class,
                () -> service.rejectRequest(request)
        );

        // Make sure nothing was saved
        verify(repository, never())
                .save(any(JoinRequest.class));
    }

    @Test
    void shouldNotCancelAlreadyRejectedRequest() {

        // Create fake repositories
        JoinRequestRepository repository =
                mock(JoinRequestRepository.class);

        UserRepository userRepository =
                mock(UserRepository.class);

        StudyGroupRepository studyGroupRepository =
                mock(StudyGroupRepository.class);

        GroupMemberRepository groupMemberRepository =
                mock(GroupMemberRepository.class);

        // Create service
        JoinRequestService service =
                new JoinRequestService(
                        repository,
                        userRepository,
                        studyGroupRepository,
                        groupMemberRepository);

        // Create an already rejected request
        JoinRequest request = new JoinRequest();
        request.setStatus("REJECTED");

        // Verify cancellation is not allowed
        assertThrows(
                IllegalStateException.class,
                () -> service.cancelRequest(request)
        );

        // Make sure nothing was saved
        verify(repository, never())
                .save(any(JoinRequest.class));
    }


    @Test
    void shouldRejectPendingJoinRequest() {

        // Create fake repositories
        JoinRequestRepository repository =
                mock(JoinRequestRepository.class);

        UserRepository userRepository =
                mock(UserRepository.class);

        StudyGroupRepository studyGroupRepository =
                mock(StudyGroupRepository.class);

        GroupMemberRepository groupMemberRepository =
                mock(GroupMemberRepository.class);

        // Create our service
        JoinRequestService service =
                new JoinRequestService(
                        repository,
                        userRepository,
                        studyGroupRepository,
                        groupMemberRepository);

        // Create a pending join request
        JoinRequest request = new JoinRequest();
        request.setStatus("PENDING");

        // Pretend the repository saves the request
        when(repository.save(request)).thenReturn(request);

        // Reject the request
        JoinRequest result = service.rejectRequest(request);

        // Check status was updated
        assertEquals("REJECTED", result.getStatus());

        // Check response time was recorded
        assertNotNull(result.getRespondedAt());

        // Verify the request was saved
        verify(repository, times(1)).save(request);
    }


    @Test
    void shouldCancelPendingJoinRequest() {

        // Create fake repositories
        JoinRequestRepository repository =
                mock(JoinRequestRepository.class);

        UserRepository userRepository =
                mock(UserRepository.class);

        StudyGroupRepository studyGroupRepository =
                mock(StudyGroupRepository.class);

        GroupMemberRepository groupMemberRepository =
                mock(GroupMemberRepository.class);

        // Create our service
        JoinRequestService service =
                new JoinRequestService(
                        repository,
                        userRepository,
                        studyGroupRepository,
                        groupMemberRepository);

        // Create a pending request
        JoinRequest request = new JoinRequest();
        request.setStatus("PENDING");

        // Pretend the repository saves it
        when(repository.save(request)).thenReturn(request);

        // Cancel the request
        JoinRequest result = service.cancelRequest(request);

        // Check the status
        assertEquals("CANCELED", result.getStatus());

        // Check response time was recorded
        assertNotNull(result.getRespondedAt());

        // Verify save was called once
        verify(repository, times(1)).save(request);
    }

    @Test
    void shouldAcceptPendingRequest() {

        JoinRequestRepository repository =
                mock(JoinRequestRepository.class);

        UserRepository userRepository =
                mock(UserRepository.class);

        StudyGroupRepository studyGroupRepository =
                mock(StudyGroupRepository.class);

        GroupMemberRepository groupMemberRepository =
                mock(GroupMemberRepository.class);

        JoinRequestService service =
                new JoinRequestService(
                        repository,
                        userRepository,
                        studyGroupRepository,
                        groupMemberRepository);

        // Create student
        User user = new User();
        user.setUserId(1L);

        // Create group with capacity of 3
        StudyGroup group = new StudyGroup();
        group.setGroupId(5L);
        group.setMaxSize(3);

        // Create a pending request
        JoinRequest request = new JoinRequest();
        request.setUser(user);
        request.setStudyGroup(group);
        request.setStatus("PENDING");

        when(groupMemberRepository
                .findByUserAndStudyGroup(user, group))
                .thenReturn(java.util.Optional.empty());

        when(repository.save(request))
                .thenReturn(request);

        // Accept request
        JoinRequest result = service.acceptRequest(request);

        // Verify status changed
        assertEquals("ACCEPTED", result.getStatus());

        // Verify student added to group in memory
        assertEquals(1, group.getMembers().size());
        assertSame(user, group.getMembers().get(0).getUser());

        // Verify response timestamp
        assertNotNull(result.getRespondedAt());

        // Verify the saves were requested
        verify(studyGroupRepository).save(group);
        verify(repository).save(request);
    }

    @Test
    void shouldNotAcceptRequestWhenGroupIsFull() {

        JoinRequestRepository repository =
                mock(JoinRequestRepository.class);

        UserRepository userRepository =
                mock(UserRepository.class);

        StudyGroupRepository studyGroupRepository =
                mock(StudyGroupRepository.class);

        GroupMemberRepository groupMemberRepository =
                mock(GroupMemberRepository.class);

        JoinRequestService service =
                new JoinRequestService(
                        repository,
                        userRepository,
                        studyGroupRepository,
                        groupMemberRepository);

        // Student requesting to join
        User newUser = new User();
        newUser.setUserId(2L);

        // Create a group with capacity 1
        StudyGroup group = new StudyGroup();
        group.setGroupId(5L);
        group.setMaxSize(1);

        // Add another student to fill the group
        User existingUser = new User();
        existingUser.setUserId(1L);
        group.addMember(existingUser);

        // Create pending request
        JoinRequest request = new JoinRequest();
        request.setUser(newUser);
        request.setStudyGroup(group);
        request.setStatus("PENDING");

        when(groupMemberRepository
                .findByUserAndStudyGroup(newUser, group))
                .thenReturn(java.util.Optional.empty());

        // Acceptance should fail
        assertThrows(
                IllegalStateException.class,
                () -> service.acceptRequest(request)
        );

        // Request must remain pending
        assertEquals("PENDING", request.getStatus());

        // No additional member should be added
        assertEquals(1, group.getMembers().size());

        // Nothing should be saved
        verify(repository, never())
                .save(any(JoinRequest.class));

        verify(studyGroupRepository, never())
                .save(any(StudyGroup.class));
    }

}
