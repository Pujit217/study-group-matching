package edu.ncsu.acm.studygroup.dto;

/**
 * Carries the information needed to request membership in a study group.
 */
public class JoinRequestDto {

    /** User sending the request. */
    private Long userId;

    /** Study group the user wants to join. */
    private Long studyGroupId;

    /** Optional message sent with the request. */
    private String message;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(final Long userId) {
        this.userId = userId;
    }

    public Long getStudyGroupId() {
        return studyGroupId;
    }

    public void setStudyGroupId(final Long studyGroupId) {
        this.studyGroupId = studyGroupId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }
}