package edu.ncsu.acm.studygroup.dto;

/**
 * Carries the information needed to send a study-group chat message.
 */
public class MessageDto {

    /** Study group receiving the message. */
    private Long studyGroupId;

    /** User sending the message. */
    private Long senderId;

    /** Message text. */
    private String content;

    public Long getStudyGroupId() {
        return studyGroupId;
    }

    public void setStudyGroupId(final Long studyGroupId) {
        this.studyGroupId = studyGroupId;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(final Long senderId) {
        this.senderId = senderId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(final String content) {
        this.content = content;
    }
}