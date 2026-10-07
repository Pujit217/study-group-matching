package edu.ncsu.acm.studygroup.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/**
 * Stores the user's general study preferences.
 * These preferences are used when recommending compatible study groups.
 */
@Entity
@Table(name = "study_preferences")
public class StudyPreference {

    /**
     * Unique database identifier for this preference profile.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long preferenceId;

    /**
     * User who owns this study-preference profile.
     * Each user can have at most one preference record.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    /**
     * General availability entered by the user.
     * Example: "Mon/Wed 5-7pm".
     */
    private String availability;

    /**
     * Describes how the user prefers to study.
     * Example: practice problems, discussion, or review.
     */
    private String studyStyle;

    /**
     * Preferred number of students in a study group.
     */
    private int preferredGroupSize;

    /**
     * Preferred meeting mode, such as in-person, online, or both.
     */
    private String studyMode;

    /**
     * Optional notes the user can provide about their study preferences.
     */
    private String additionalNotes;

    /**
     * Default constructor required by JPA.
     */
    public StudyPreference() {
    }

    /**
     * Creates a preference profile for the specified user.
     *
     * @param user user who owns the preferences
     */
    public StudyPreference(final User user) {
        this.user = user;
    }

    /**
     * Returns the unique preference identifier.
     *
     * @return preference ID
     */
    public Long getPreferenceId() {
        return preferenceId;
    }

    /**
     * Sets the unique preference identifier.
     *
     * @param preferenceId preference ID
     */
    public void setPreferenceId(final Long preferenceId) {
        this.preferenceId = preferenceId;
    }

    /**
     * Returns the user who owns these preferences.
     *
     * @return associated user
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the user who owns these preferences.
     *
     * @param user associated user
     */
    public void setUser(final User user) {
        this.user = user;
    }

    /**
     * Returns the user's availability.
     *
     * @return availability
     */
    public String getAvailability() {
        return availability;
    }

    /**
     * Updates the user's availability.
     *
     * @param availability new availability
     */
    public void setAvailability(final String availability) {
        this.availability = availability;
    }

    /**
     * Returns the user's preferred study style.
     *
     * @return study style
     */
    public String getStudyStyle() {
        return studyStyle;
    }

    /**
     * Updates the user's preferred study style.
     *
     * @param studyStyle new study style
     */
    public void setStudyStyle(final String studyStyle) {
        this.studyStyle = studyStyle;
    }

    /**
     * Returns the user's preferred group size.
     *
     * @return preferred group size
     */
    public int getPreferredGroupSize() {
        return preferredGroupSize;
    }

    /**
     * Updates the user's preferred group size.
     *
     * @param preferredGroupSize new preferred group size
     */
    public void setPreferredGroupSize(final int preferredGroupSize) {
        this.preferredGroupSize = preferredGroupSize;
    }

    /**
     * Returns the user's preferred study mode.
     *
     * @return study mode
     */
    public String getStudyMode() {
        return studyMode;
    }

    /**
     * Updates the user's preferred study mode.
     *
     * @param studyMode new study mode
     */
    public void setStudyMode(final String studyMode) {
        this.studyMode = studyMode;
    }

    /**
     * Returns any additional preference notes entered by the user.
     *
     * @return additional notes
     */
    public String getAdditionalNotes() {
        return additionalNotes;
    }

    /**
     * Updates the user's additional preference notes.
     *
     * @param additionalNotes new notes
     */
    public void setAdditionalNotes(final String additionalNotes) {
        this.additionalNotes = additionalNotes;
    }

    /**
     * Updates all major preference values at once.
     * This corresponds directly to updatePreferences() in the UML.
     *
     * @param availability updated availability
     * @param studyStyle updated study style
     * @param preferredGroupSize updated preferred group size
     * @param studyMode updated study mode
     * @param additionalNotes updated additional notes
     */
    public void updatePreferences(final String availability,
                                  final String studyStyle,
                                  final int preferredGroupSize,
                                  final String studyMode,
                                  final String additionalNotes) {
        this.availability = availability;
        this.studyStyle = studyStyle;
        this.preferredGroupSize = preferredGroupSize;
        this.studyMode = studyMode;
        this.additionalNotes = additionalNotes;
    }

    /**
     * Calculates a simple compatibility score between this user's
     * preferences and another user's preferences.
     *
     * @param other other preference profile
     * @return compatibility score between 0.0 and 1.0
     */
    public double getCompatibilityScore(
            final StudyPreference other) {

        if (other == null) {
            return 0.0;
        }

        double score = 0.0;
        int comparisons = 0;

        if (studyMode != null && other.studyMode != null) {
            comparisons++;
            if (studyMode.equalsIgnoreCase(other.studyMode)
                    || studyMode.equalsIgnoreCase("Both")
                    || other.studyMode.equalsIgnoreCase("Both")) {
                score++;
            }
        }

        if (studyStyle != null && other.studyStyle != null) {
            comparisons++;
            if (studyStyle.equalsIgnoreCase(other.studyStyle)) {
                score++;
            }
        }

        if (preferredGroupSize > 0
                && other.preferredGroupSize > 0) {
            comparisons++;

            if (Math.abs(preferredGroupSize
                    - other.preferredGroupSize) <= 1) {
                score++;
            }
        }

        if (availability != null && other.availability != null) {
            comparisons++;

            if (availability.equalsIgnoreCase(other.availability)) {
                score++;
            }
        }

        if (comparisons == 0) {
            return 0.0;
        }

        return score / comparisons;
    }
}