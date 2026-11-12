package edu.ncsu.acm.studygroup.repository;

import edu.ncsu.acm.studygroup.entity.JoinRequest;
import edu.ncsu.acm.studygroup.entity.StudyGroup;
import edu.ncsu.acm.studygroup.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Provides database access operations for JoinRequest entities.
 */
public interface JoinRequestRepository
        extends JpaRepository<JoinRequest, Long> {

    /**
     * Finds all join requests submitted by a specific user.
     *
     * @param user user whose requests should be returned
     * @return list of matching join requests
     */
    List<JoinRequest> findByUser(User user);

    /**
     * Finds all join requests submitted to a specific study group.
     *
     * @param studyGroup target study group
     * @return list of matching join requests
     */
    List<JoinRequest> findByStudyGroup(StudyGroup studyGroup);

    /**
     * Finds all join requests with the specified status.
     *
     * @param status request status such as PENDING or ACCEPTED
     * @return list of matching join requests
     */
    List<JoinRequest> findByStatus(String status);

    /**
     * Finds join requests for a study group with a specific status.
     *
     * @param studyGroup target study group
     * @param status request status
     * @return list of matching join requests
     */
    List<JoinRequest> findByStudyGroupAndStatus(
            StudyGroup studyGroup,
            String status
    );
}