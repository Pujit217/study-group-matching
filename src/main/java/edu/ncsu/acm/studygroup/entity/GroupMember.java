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
 * Represents a user's membership in a study group.
 * This class connects User and StudyGroup after the user has joined the group.
 */
@Entity
@Table(name = "group_members")
public class GroupMember {

    /**
     * Unique database identifier for the group membership.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    /**
     * User who belongs to the study group.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Study group to which the user belongs.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_group_id", nullable = false)
    private StudyGroup studyGroup;

    /**
     * Date and time when the user joined the study group.
     */
    private LocalDateTime joinedAt;

    /**
     * Default constructor required by JPA.
     */
    public GroupMember() {
    }

    /**
     * Creates a membership connecting a user to a study group.
     *
     * @param user user joining the group
     * @param studyGroup study group being joined
     */
    public GroupMember(final User user,
                       final StudyGroup studyGroup) {
        this.user = user;
        this.studyGroup = studyGroup;
    }

    /**
     * Automatically records the membership creation time
     * before the object is first saved.
     */
    @PrePersist
    protected void onCreate() {
        joinedAt = LocalDateTime.now();
    }

    /**
     * Returns the unique membership identifier.
     *
     * @return member ID
     */
    public Long getMemberId() {
        return memberId;
    }

    /**
     * Sets the unique membership identifier.
     *
     * @param memberId member ID
     */
    public void setMemberId(final Long memberId) {
        this.memberId = memberId;
    }

    /**
     * Returns the user associated with this membership.
     *
     * @return group member user
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the user associated with this membership.
     *
     * @param user group member user
     */
    public void setUser(final User user) {
        this.user = user;
    }

    /**
     * Returns the study group associated with this membership.
     *
     * @return associated study group
     */
    public StudyGroup getStudyGroup() {
        return studyGroup;
    }

    /**
     * Sets the study group associated with this membership.
     *
     * @param studyGroup associated study group
     */
    public void setStudyGroup(final StudyGroup studyGroup) {
        this.studyGroup = studyGroup;
    }

    /**
     * Returns the date and time when the user joined the group.
     *
     * @return join time
     */
    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }

    /**
     * Sets the membership join time.
     *
     * @param joinedAt membership join time
     */
    public void setJoinedAt(final LocalDateTime joinedAt) {
        this.joinedAt = joinedAt;
    }
}