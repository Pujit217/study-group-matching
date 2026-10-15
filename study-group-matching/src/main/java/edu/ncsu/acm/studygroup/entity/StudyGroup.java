package edu.ncsu.acm.studygroup.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a study group created for a specific course.
 * Stores group details, meeting information, members, requests, and messages.
 */
@Entity
@Table(name = "study_groups")
public class StudyGroup {

    /**
     * Unique database identifier for the study group.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupId;

    /**
     * Display name of the study group.
     */
    @Column(nullable = false)
    private String name;

    /**
     * Optional description explaining the group's purpose.
     */
    @Column(length = 1000)
    private String description;

    /**
     * Course for which this study group was created.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    /**
     * Optional section associated with this study group.
     * A group may include students from multiple sections.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id")
    private Section section;

    /**
     * User who originally created the study group.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    /**
     * Maximum number of students allowed in the group.
     */
    private int maxSize;

    /**
     * Meeting mode, such as in-person, online, or both.
     */
    private String studyMode;

    /**
     * Physical or virtual location where the group meets.
     */
    private String meetingLocation;

    /**
     * Human-readable meeting schedule for the group.
     * Example: "Mon 6pm, Wed 6pm".
     */
    private String meetingTimes;

    /**
     * Expected duration of each meeting in minutes.
     */
    private int meetingDurationMinutes;

    /**
     * Date and time when the study group was created.
     */
    private LocalDateTime createdAt;

    /**
     * Stores all users who currently belong to this study group.
     */
    @OneToMany(
            mappedBy = "studyGroup",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<GroupMember> members = new ArrayList<>();

    /**
     * Stores all requests submitted by users who want to join this group.
     */
    @OneToMany(
            mappedBy = "studyGroup",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<JoinRequest> joinRequests = new ArrayList<>();

    /**
     * Stores all chat messages sent inside this study group.
     */
    @OneToMany(
            mappedBy = "studyGroup",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Message> messages = new ArrayList<>();

    /**
     * Default constructor required by JPA.
     */
    public StudyGroup() {
    }

    /**
     * Creates a basic study group for a course.
     *
     * @param name group name
     * @param course associated course
     * @param createdBy user creating the group
     */
    public StudyGroup(final String name,
                      final Course course,
                      final User createdBy) {
        this.name = name;
        this.course = course;
        this.createdBy = createdBy;
    }

    /**
     * Automatically records when the group is created.
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    /**
     * Returns the unique study-group identifier.
     *
     * @return group ID
     */
    public Long getGroupId() {
        return groupId;
    }

    /**
     * Sets the unique study-group identifier.
     *
     * @param groupId group ID
     */
    public void setGroupId(final Long groupId) {
        this.groupId = groupId;
    }

    /**
     * Returns the study-group name.
     *
     * @return group name
     */
    public String getName() {
        return name;
    }

    /**
     * Updates the study-group name.
     *
     * @param name new group name
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Returns the study-group description.
     *
     * @return group description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Updates the study-group description.
     *
     * @param description new group description
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * Returns the course associated with this group.
     *
     * @return course
     */
    public Course getCourse() {
        return course;
    }

    /**
     * Sets the course associated with this group.
     *
     * @param course associated course
     */
    public void setCourse(final Course course) {
        this.course = course;
    }

    /**
     * Returns the optional course section associated with this group.
     *
     * @return associated section or null
     */
    public Section getSection() {
        return section;
    }

    /**
     * Sets the optional section associated with this group.
     *
     * @param section associated section
     */
    public void setSection(final Section section) {
        this.section = section;
    }

    /**
     * Returns the user who created this study group.
     *
     * @return group creator
     */
    public User getCreatedBy() {
        return createdBy;
    }

    /**
     * Sets the user who created this study group.
     *
     * @param createdBy group creator
     */
    public void setCreatedBy(final User createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * Returns the maximum number of members allowed.
     *
     * @return maximum group size
     */
    public int getMaxSize() {
        return maxSize;
    }

    /**
     * Updates the maximum number of members allowed.
     *
     * @param maxSize new maximum group size
     */
    public void setMaxSize(final int maxSize) {
        this.maxSize = maxSize;
    }

    /**
     * Returns the group's preferred meeting mode.
     *
     * @return study mode
     */
    public String getStudyMode() {
        return studyMode;
    }

    /**
     * Updates the group's preferred meeting mode.
     *
     * @param studyMode new study mode
     */
    public void setStudyMode(final String studyMode) {
        this.studyMode = studyMode;
    }

    /**
     * Returns the group's meeting location.
     *
     * @return meeting location
     */
    public String getMeetingLocation() {
        return meetingLocation;
    }

    /**
     * Updates the group's meeting location.
     *
     * @param meetingLocation new meeting location
     */
    public void setMeetingLocation(
            final String meetingLocation) {
        this.meetingLocation = meetingLocation;
    }

    /**
     * Returns the group's meeting schedule.
     *
     * @return meeting times
     */
    public String getMeetingTimes() {
        return meetingTimes;
    }

    /**
     * Updates the group's meeting schedule.
     *
     * @param meetingTimes new meeting schedule
     */
    public void setMeetingTimes(final String meetingTimes) {
        this.meetingTimes = meetingTimes;
    }

    /**
     * Returns the expected meeting duration in minutes.
     *
     * @return meeting duration
     */
    public int getMeetingDurationMinutes() {
        return meetingDurationMinutes;
    }

    /**
     * Updates the expected meeting duration in minutes.
     *
     * @param meetingDurationMinutes new meeting duration
     */
    public void setMeetingDurationMinutes(
            final int meetingDurationMinutes) {
        this.meetingDurationMinutes = meetingDurationMinutes;
    }

    /**
     * Returns the time at which the study group was created.
     *
     * @return creation time
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the study-group creation time.
     *
     * @param createdAt creation time
     */
    public void setCreatedAt(final LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Returns all current group members.
     * This corresponds directly to getMembers() in the UML.
     *
     * @return list of group members
     */
    public List<GroupMember> getMembers() {
        return members;
    }

    /**
     * Replaces the current member list.
     *
     * @param members new member list
     */
    public void setMembers(final List<GroupMember> members) {
        this.members = members;
    }

    /**
     * Returns all join requests associated with this group.
     *
     * @return join-request list
     */
    public List<JoinRequest> getJoinRequests() {
        return joinRequests;
    }

    /**
     * Replaces the group's join-request list.
     *
     * @param joinRequests new join-request list
     */
    public void setJoinRequests(
            final List<JoinRequest> joinRequests) {
        this.joinRequests = joinRequests;
    }

    /**
     * Returns all chat messages associated with this group.
     *
     * @return message list
     */
    public List<Message> getMessages() {
        return messages;
    }

    /**
     * Replaces the group's current message list.
     *
     * @param messages new message list
     */
    public void setMessages(final List<Message> messages) {
        this.messages = messages;
    }

    /**
     * Adds a user to the study group.
     * This corresponds directly to addMember() in the UML.
     *
     * @param user user being added
     */
    public void addMember(final User user) {
        if (user == null || isFull()) {
            return;
        }

        final boolean alreadyMember = members.stream()
                .anyMatch(member -> member.getUser().equals(user));

        if (!alreadyMember) {
            final GroupMember member =
                    new GroupMember(user, this);

            members.add(member);
        }
    }

    /**
     * Removes a user from the study group.
     * This corresponds directly to removeMember() in the UML.
     *
     * @param user user being removed
     */
    public void removeMember(final User user) {
        members.removeIf(
                member -> member.getUser().equals(user)
        );
    }

    /**
     * Updates the major editable properties of this study group.
     * This corresponds directly to updateGroupInfo() in the UML.
     *
     * @param name updated group name
     * @param description updated description
     * @param maxSize updated maximum size
     * @param studyMode updated study mode
     * @param meetingLocation updated meeting location
     * @param meetingTimes updated meeting schedule
     * @param meetingDurationMinutes updated meeting duration
     */
    public void updateGroupInfo(
            final String name,
            final String description,
            final int maxSize,
            final String studyMode,
            final String meetingLocation,
            final String meetingTimes,
            final int meetingDurationMinutes) {

        this.name = name;
        this.description = description;
        this.maxSize = maxSize;
        this.studyMode = studyMode;
        this.meetingLocation = meetingLocation;
        this.meetingTimes = meetingTimes;
        this.meetingDurationMinutes =
                meetingDurationMinutes;
    }

    /**
     * Determines whether the study group has reached its maximum size.
     * This corresponds directly to isFull() in the UML.
     *
     * @return true if the group is full
     */
    public boolean isFull() {
        return maxSize > 0 && members.size() >= maxSize;
    }
}