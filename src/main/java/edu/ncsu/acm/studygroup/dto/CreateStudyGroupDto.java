package edu.ncsu.acm.studygroup.dto;

/**
 * Carries the information needed to create a study group.
 */
public class CreateStudyGroupDto {

    /** Name of the study group. */
    private String name;

    /** Optional group description. */
    private String description;

    /** Database ID of the related course. */
    private Long courseId;

    /** Optional database ID of the related section. */
    private Long sectionId;

    /** Database ID of the user creating the group. */
    private Long createdByUserId;

    /** Maximum number of group members. */
    private int maxSize;

    /** In-person, online, or both. */
    private String studyMode;

    /** Location where the group plans to meet. */
    private String meetingLocation;

    /** Human-readable meeting schedule. */
    private String meetingTimes;

    /** Expected meeting duration in minutes. */
    private int meetingDurationMinutes;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(final Long courseId) {
        this.courseId = courseId;
    }

    public Long getSectionId() {
        return sectionId;
    }

    public void setSectionId(final Long sectionId) {
        this.sectionId = sectionId;
    }

    public Long getCreatedByUserId() {
        return createdByUserId;
    }

    public void setCreatedByUserId(final Long createdByUserId) {
        this.createdByUserId = createdByUserId;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(final int maxSize) {
        this.maxSize = maxSize;
    }

    public String getStudyMode() {
        return studyMode;
    }

    public void setStudyMode(final String studyMode) {
        this.studyMode = studyMode;
    }

    public String getMeetingLocation() {
        return meetingLocation;
    }

    public void setMeetingLocation(final String meetingLocation) {
        this.meetingLocation = meetingLocation;
    }

    public String getMeetingTimes() {
        return meetingTimes;
    }

    public void setMeetingTimes(final String meetingTimes) {
        this.meetingTimes = meetingTimes;
    }

    public int getMeetingDurationMinutes() {
        return meetingDurationMinutes;
    }

    public void setMeetingDurationMinutes(
            final int meetingDurationMinutes) {
        this.meetingDurationMinutes = meetingDurationMinutes;
    }
}