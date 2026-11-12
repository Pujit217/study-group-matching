package edu.ncsu.acm.studygroup.dto;

/**
 * Carries study-preference information between the frontend and backend.
 */
public class StudyPreferenceDto {

    /** User whose preferences are being updated. */
    private Long userId;

    /** General availability of the user. */
    private String availability;

    /** Preferred way of studying. */
    private String studyStyle;

    /** Preferred number of group members. */
    private int preferredGroupSize;

    /** In-person, online, or both. */
    private String studyMode;

    /** Optional extra preference information. */
    private String additionalNotes;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(final Long userId) {
        this.userId = userId;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(final String availability) {
        this.availability = availability;
    }

    public String getStudyStyle() {
        return studyStyle;
    }

    public void setStudyStyle(final String studyStyle) {
        this.studyStyle = studyStyle;
    }

    public int getPreferredGroupSize() {
        return preferredGroupSize;
    }

    public void setPreferredGroupSize(
            final int preferredGroupSize) {
        this.preferredGroupSize = preferredGroupSize;
    }

    public String getStudyMode() {
        return studyMode;
    }

    public void setStudyMode(final String studyMode) {
        this.studyMode = studyMode;
    }

    public String getAdditionalNotes() {
        return additionalNotes;
    }

    public void setAdditionalNotes(final String additionalNotes) {
        this.additionalNotes = additionalNotes;
    }
}