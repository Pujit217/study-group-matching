package edu.ncsu.acm.studygroup.entity;

import jakarta.persistence.Column;
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
 * Represents a chat message sent inside a study group.
 * Each message belongs to one study group and has one sender.
 */
@Entity
@Table(name = "messages")
public class Message {

    /**
     * Unique database identifier for the message.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;

    /**
     * Study group in which this message was sent.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_group_id", nullable = false)
    private StudyGroup studyGroup;

    /**
     * User who sent the message.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    /**
     * Text content of the message.
     */
    @Column(nullable = false, length = 2000)
    private String content;

    /**
     * Date and time when the message was first sent.
     */
    private LocalDateTime sentAt;

    /**
     * Date and time when the message was last edited.
     * This remains null if the message has never been edited.
     */
    private LocalDateTime editedAt;

    /**
     * Default constructor required by JPA.
     */
    public Message() {
    }

    /**
     * Creates a message inside a study group.
     *
     * @param studyGroup group receiving the message
     * @param sender user sending the message
     * @param content message text
     */
    public Message(final StudyGroup studyGroup,
                   final User sender,
                   final String content) {
        this.studyGroup = studyGroup;
        this.sender = sender;
        this.content = content;
    }

    /**
     * Automatically records the time when the message is first saved.
     */
    @PrePersist
    protected void onCreate() {
        sentAt = LocalDateTime.now();
    }

    /**
     * Returns the unique message identifier.
     *
     * @return message ID
     */
    public Long getMessageId() {
        return messageId;
    }

    /**
     * Sets the unique message identifier.
     *
     * @param messageId message ID
     */
    public void setMessageId(final Long messageId) {
        this.messageId = messageId;
    }

    /**
     * Returns the study group containing this message.
     *
     * @return associated study group
     */
    public StudyGroup getStudyGroup() {
        return studyGroup;
    }

    /**
     * Sets the study group containing this message.
     *
     * @param studyGroup associated study group
     */
    public void setStudyGroup(final StudyGroup studyGroup) {
        this.studyGroup = studyGroup;
    }

    /**
     * Returns the user who sent this message.
     *
     * @return message sender
     */
    public User getSender() {
        return sender;
    }

    /**
     * Sets the user who sent this message.
     *
     * @param sender message sender
     */
    public void setSender(final User sender) {
        this.sender = sender;
    }

    /**
     * Returns the text content of this message.
     *
     * @return message content
     */
    public String getContent() {
        return content;
    }

    /**
     * Updates the text content of this message.
     *
     * @param content new message content
     */
    public void setContent(final String content) {
        this.content = content;
    }

    /**
     * Returns the time when the message was first sent.
     *
     * @return send time
     */
    public LocalDateTime getSentAt() {
        return sentAt;
    }

    /**
     * Sets the time when the message was first sent.
     *
     * @param sentAt send time
     */
    public void setSentAt(final LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }

    /**
     * Returns the most recent edit time for this message.
     *
     * @return edit time, or null if never edited
     */
    public LocalDateTime getEditedAt() {
        return editedAt;
    }

    /**
     * Sets the most recent edit time for this message.
     *
     * @param editedAt edit time
     */
    public void setEditedAt(final LocalDateTime editedAt) {
        this.editedAt = editedAt;
    }

    /**
     * Represents sending this message.
     * Persistence and membership validation will be handled by the service layer.
     */
    public void send() {
        if (sentAt == null) {
            sentAt = LocalDateTime.now();
        }
    }

    /**
     * Replaces the existing message content and records the edit time.
     * This corresponds directly to edit() in the UML.
     *
     * @param newContent replacement message content
     */
    public void edit(final String newContent) {
        content = newContent;
        editedAt = LocalDateTime.now();
    }

    /**
     * Clears the message content as a simple entity-level delete marker.
     * Actual database deletion will later be handled by the service/repository layer.
     */
    public void delete() {
        content = "";
        editedAt = LocalDateTime.now();
    }
}