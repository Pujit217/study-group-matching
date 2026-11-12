package edu.ncsu.acm.studygroup.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

/**
 * Represents a user's request to join a study group.
 * Stores the requesting user, target group, request status, and timestamps.
 */
@Entity
@Table(name = "join_requests")
public class JoinRequest {

    /**
     * Unique database identifier for the join request.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long requestId;

    /**
     * User who submitted the join request.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Study group the user wants to join.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_group_id", nullable = false)
    private StudyGroup studyGroup;

    /**
     * Current status of the request.
     * Expected values include PENDING, ACCEPTED, REJECTED, or CANCELED.
     */
    private String status;

    /**
     * Optional message submitted by the user with the join request.
     */
    private String message;

    /**
     * Date and time when the request was created.
     */
    private LocalDateTime requestedAt;

    /**
     * Date and time when the request was accepted or rejected.
     */
    private LocalDateTime respondedAt;

    /**
     * Default constructor required by JPA.
     */
    public JoinRequest() {
    }

    /**
     * Creates a pending request for a user to join a study group.
     *
     * @param user requesting user
     * @param studyGroup target study group
     * @param message optional request message
     */
    public JoinRequest(final User user,
                       final StudyGroup studyGroup,
                       final String message) {
        this.user = user;
        this.studyGroup = studyGroup;
        this.message = message;
        this.status = "PENDING";
    }

    /**
     * Automatically records the request creation time.
     * Also assigns PENDING if no status has been provided.
     */
    @PrePersist
    protected void onCreate() {
        requestedAt = LocalDateTime.now();

        if (status == null) {
            status = "PENDING";
        }
    }

    /**
     * Returns the unique join-request identifier.
     *
     * @return request ID
     */
    public Long getRequestId() {
        return requestId;
    }

    /**
     * Sets the unique join-request identifier.
     *
     * @param requestId request ID
     */
    public void setRequestId(final Long requestId) {
        this.requestId = requestId;
    }

    /**
     * Returns the user who submitted this request.
     *
     * @return requesting user
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the user who submitted this request.
     *
     * @param user requesting user
     */
    public void setUser(final User user) {
        this.user = user;
    }

    /**
     * Returns the study group targeted by this request.
     *
     * @return target study group
     */
    public StudyGroup getStudyGroup() {
        return studyGroup;
    }

    /**
     * Sets the study group targeted by this request.
     *
     * @param studyGroup target study group
     */
    public void setStudyGroup(final StudyGroup studyGroup) {
        this.studyGroup = studyGroup;
    }

    /**
     * Returns the current status of this join request.
     *
     * @return request status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the current status of this join request.
     *
     * @param status new request status
     */
    public void setStatus(final String status) {
        this.status = status;
    }

    /**
     * Returns the optional request message.
     *
     * @return request message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Updates the optional request message.
     *
     * @param message new request message
     */
    public void setMessage(final String message) {
        this.message = message;
    }

    /**
     * Returns the date and time when the request was created.
     *
     * @return request creation time
     */
    public LocalDateTime getRequestedAt() {
        return requestedAt;
    }

    /**
     * Sets the request creation time.
     *
     * @param requestedAt request creation time
     */
    public void setRequestedAt(final LocalDateTime requestedAt) {
        this.requestedAt = requestedAt;
    }

    /**
     * Returns the date and time when the request was responded to.
     *
     * @return response time
     */
    public LocalDateTime getRespondedAt() {
        return respondedAt;
    }

    /**
     * Sets the response time for this request.
     *
     * @param respondedAt response time
     */
    public void setRespondedAt(final LocalDateTime respondedAt) {
        this.respondedAt = respondedAt;
    }

    /**
     * Accepts the join request and adds the user to the study group.
     * This corresponds directly to accept() in the UML.
     */
    public void accept() {
        if (!"PENDING".equals(status)) {
            return;
        }

        if (studyGroup != null && !studyGroup.isFull()) {
            studyGroup.addMember(user);
            status = "ACCEPTED";
            respondedAt = LocalDateTime.now();
        }
    }

    /**
     * Rejects the join request without adding the user to the group.
     * This corresponds directly to reject() in the UML.
     */
    public void reject() {
        if (!"PENDING".equals(status)) {
            return;
        }

        status = "REJECTED";
        respondedAt = LocalDateTime.now();
    }

    /**
     * Cancels the join request before a group creator responds.
     * This corresponds directly to cancel() in the UML.
     */
    public void cancel() {
        if (!"PENDING".equals(status)) {
            return;
        }

        status = "CANCELED";
        respondedAt = LocalDateTime.now();
    }
}