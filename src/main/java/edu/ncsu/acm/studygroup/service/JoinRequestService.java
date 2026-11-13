package edu.ncsu.acm.studygroup.service;

import edu.ncsu.acm.studygroup.entity.JoinRequest;
import edu.ncsu.acm.studygroup.entity.StudyGroup;
import edu.ncsu.acm.studygroup.entity.User;
import edu.ncsu.acm.studygroup.repository.JoinRequestRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import edu.ncsu.acm.studygroup.repository.StudyGroupRepository;
import edu.ncsu.acm.studygroup.repository.UserRepository;
import edu.ncsu.acm.studygroup.repository.GroupMemberRepository;
import org.springframework.transaction.annotation.Transactional;


/**
 * Handles business logic related to study-group join requests.
 * This includes creating, viewing, accepting, rejecting, and canceling requests.
 */
@Service
public class JoinRequestService {

    /**
     * Repository used to access JoinRequest records.
     */
    private final JoinRequestRepository joinRequestRepository;
    private final UserRepository userRepository;
    private final StudyGroupRepository studyGroupRepository;
    private final GroupMemberRepository groupMemberRepository;

    /**
     * Creates the service using constructor injection.
     *
     * @param joinRequestRepository repository used for join-request persistence
     */
    public JoinRequestService(
            final JoinRequestRepository joinRequestRepository,
            final UserRepository userRepository,
            final StudyGroupRepository studyGroupRepository,
            final GroupMemberRepository groupMemberRepository) {

        this.joinRequestRepository = joinRequestRepository;
        this.userRepository = userRepository;
        this.studyGroupRepository = studyGroupRepository;
        this.groupMemberRepository = groupMemberRepository;
    }

    /**
     * Returns all join requests currently stored in the database.
     *
     * @return list of all join requests
     */
    public List<JoinRequest> getAllJoinRequests() {
        return joinRequestRepository.findAll();
    }

    /**
     * Finds a join request by its database identifier.
     *
     * @param requestId join-request ID
     * @return matching request, if one exists
     */
    public Optional<JoinRequest> getJoinRequestById(
            final Long requestId) {
        return joinRequestRepository.findById(requestId);
    }

    /**
     * Returns all join requests submitted by a specific user.
     *
     * @param user user whose requests should be returned
     * @return list of matching requests
     */
    public List<JoinRequest> getRequestsByUser(
            final User user) {
        return joinRequestRepository.findByUser(user);
    }

    /**
     * Returns all join requests submitted to a specific study group.
     *
     * @param studyGroup target study group
     * @return list of matching requests
     */
    public List<JoinRequest> getRequestsByStudyGroup(
            final StudyGroup studyGroup) {
        return joinRequestRepository.findByStudyGroup(studyGroup);
    }

    /**
     * Returns all pending requests for a specific study group.
     *
     * @param studyGroup target study group
     * @return list of pending requests
     */
    public List<JoinRequest> getPendingRequests(
            final StudyGroup studyGroup) {
        return joinRequestRepository
                .findByStudyGroupAndStatus(studyGroup, "PENDING");
    }

    /**
     * Creates and stores a new join request.
     *
     * @param joinRequest request to save
     * @return saved join request
     */


    public JoinRequest createJoinRequest(
            final JoinRequest joinRequest) {

        // 1. Check for missing user or group
        if (joinRequest == null
                || joinRequest.getUser() == null
                || joinRequest.getStudyGroup() == null) {
            throw new IllegalArgumentException(
                    "Join request must have a user and study group");
        }

        // 2. Get the user's database ID
        Long userId = joinRequest.getUser().getUserId();

        // 3. Get the study group's database ID
        Long groupId = joinRequest.getStudyGroup().getGroupId();

        // 4. Make sure both IDs are provided
        if (userId == null || groupId == null) {
            throw new IllegalArgumentException(
                    "User ID and study group ID are required");
        }

        User savedUser = userRepository.findById(userId)
                .orElseThrow(() ->
                        new JoinRequestNotFoundException(
                                "User does not exist"));

        StudyGroup savedGroup = studyGroupRepository.findById(groupId)
                .orElseThrow(() ->
                        new JoinRequestNotFoundException(
                                "Study group does not exist"));


        // Use the actual entities fetched from the database
        joinRequest.setUser(savedUser);
        joinRequest.setStudyGroup(savedGroup);


        // Check whether the student is already a member
        boolean alreadyMember = groupMemberRepository
                .findByUserAndStudyGroup(
                        joinRequest.getUser(),
                        joinRequest.getStudyGroup()
                )
                .isPresent();

        // Existing members cannot request to join again
        if (alreadyMember) {
            throw new IllegalStateException(
                    "User is already a member of this study group");
        }


        // 7. Check whether a pending request already exists
        boolean alreadyRequested = joinRequestRepository
                .existsByUserAndStudyGroupAndStatus(
                        joinRequest.getUser(),
                        joinRequest.getStudyGroup(),
                        "PENDING"
                );

        if (alreadyRequested) {
            throw new IllegalStateException(
                    "You already have a pending request for this group");
        }

        // 8. Save the request
        joinRequest.setStatus("PENDING");
        return joinRequestRepository.save(joinRequest);
    }



    /**
     * Accepts a pending join request and saves the updated request.
     *
     * @param request request being accepted
     * @return updated join request
     */
    @Transactional
    public JoinRequest acceptRequest(
            final JoinRequest request) {

        // 1. Check if request exists
        if (request == null) {
            throw new IllegalArgumentException(
                    "Join request cannot be null");
        }

        // 2. Only pending requests can be accepted
        if (!"PENDING".equals(request.getStatus())) {
            throw new IllegalStateException(
                    "Only pending requests can be accepted");
        }

        // 3. Get the student and study group
        User user = request.getUser();
        StudyGroup group = request.getStudyGroup();

        if (user == null || group == null) {
            throw new IllegalArgumentException(
                    "Request must have a user and study group");
        }

        // 4. Check if student is already a member
        boolean alreadyMember = groupMemberRepository
                .findByUserAndStudyGroup(user, group)
                .isPresent();

        if (alreadyMember) {
            throw new IllegalStateException(
                    "User is already a group member");
        }

        // 5. Check whether the group is full
        if (group.isFull()) {
            throw new IllegalStateException(
                    "Study group is already full");
        }

        // 6. Accept the request and add the member
        request.accept();

        // 7. Save the updated group and its members
        studyGroupRepository.save(group);

        // 8. Save the updated request
        return joinRequestRepository.save(request);
    }


    /**
     * Rejects a pending join request and saves the updated request.
     *
     * @param request request being rejected
     * @return updated join request
     */

    public JoinRequest rejectRequest(
            final JoinRequest request) {

        // Make sure the request is not null
        if (request == null) {
            throw new IllegalArgumentException(
                    "Join request cannot be null");
        }

        // Only pending requests can be rejected
        if (!"PENDING".equals(request.getStatus())) {
            throw new IllegalStateException(
                    "Only pending join requests can be rejected");
        }

        // Reject the request
        request.reject();

        // Save the updated request
        return joinRequestRepository.save(request);
    }


    /**
     * Cancels a pending join request and saves the updated request.
     *
     * @param request request being canceled
     * @return updated join request
     */

    public JoinRequest cancelRequest(
            final JoinRequest request) {

        // Make sure the request exists
        if (request == null) {
            throw new IllegalArgumentException(
                    "Join request cannot be null");
        }

        // Only pending requests can be canceled
        if (!"PENDING".equals(request.getStatus())) {
            throw new IllegalStateException(
                    "Only pending join requests can be canceled");
        }

        // Cancel the request
        request.cancel();

        // Save the updated request
        return joinRequestRepository.save(request);
    }


    /**
     * Deletes a join request by its database identifier.
     *
     * @param requestId join-request ID
     */
    public void deleteJoinRequest(final Long requestId) {
        joinRequestRepository.deleteById(requestId);
    }
}