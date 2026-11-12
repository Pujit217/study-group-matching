package edu.ncsu.acm.studygroup.service;

import edu.ncsu.acm.studygroup.entity.JoinRequest;
import edu.ncsu.acm.studygroup.entity.StudyGroup;
import edu.ncsu.acm.studygroup.entity.User;
import edu.ncsu.acm.studygroup.repository.JoinRequestRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    /**
     * Creates the service using constructor injection.
     *
     * @param joinRequestRepository repository used for join-request persistence
     */
    public JoinRequestService(
            final JoinRequestRepository joinRequestRepository) {
        this.joinRequestRepository = joinRequestRepository;
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
        return joinRequestRepository.save(joinRequest);
    }

    /**
     * Accepts a pending join request and saves the updated request.
     *
     * @param request request being accepted
     * @return updated join request
     */
    public JoinRequest acceptRequest(
            final JoinRequest request) {
        request.accept();
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
        request.reject();
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
        request.cancel();
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