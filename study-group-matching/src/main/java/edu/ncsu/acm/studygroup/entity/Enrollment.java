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
 * Represents a student's enrollment in a specific course and section.
 * This class connects User, Course, and Section together.
 */
@Entity
@Table(name = "enrollments")
public class Enrollment {

    /**
     * Unique database identifier for the enrollment.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long enrollmentId;

    /**
     * User who owns this enrollment.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Course in which the user is enrolled.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    /**
     * Specific section selected by the user for this course.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id", nullable = false)
    private Section section;

    /**
     * Date and time when the enrollment was created.
     */
    private LocalDateTime enrolledAt;

    /**
     * Default constructor required by JPA.
     */
    public Enrollment() {
    }

    /**
     * Creates an enrollment connecting a user, course, and section.
     *
     * @param user user being enrolled
     * @param course course being added
     * @param section selected course section
     */
    public Enrollment(final User user,
                      final Course course,
                      final Section section) {
        this.user = user;
        this.course = course;
        this.section = section;
    }

    /**
     * Automatically records the enrollment creation time
     * before the object is first persisted.
     */
    @PrePersist
    protected void onCreate() {
        enrolledAt = LocalDateTime.now();
    }

    /**
     * Returns the unique enrollment identifier.
     *
     * @return enrollment ID
     */
    public Long getEnrollmentId() {
        return enrollmentId;
    }

    /**
     * Sets the unique enrollment identifier.
     *
     * @param enrollmentId enrollment ID
     */
    public void setEnrollmentId(final Long enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    /**
     * Returns the user associated with this enrollment.
     * This corresponds directly to getUser() in the UML.
     *
     * @return enrolled user
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the user associated with this enrollment.
     *
     * @param user enrolled user
     */
    public void setUser(final User user) {
        this.user = user;
    }

    /**
     * Returns the course associated with this enrollment.
     * This corresponds directly to getCourse() in the UML.
     *
     * @return enrolled course
     */
    public Course getCourse() {
        return course;
    }

    /**
     * Sets the course associated with this enrollment.
     *
     * @param course enrolled course
     */
    public void setCourse(final Course course) {
        this.course = course;
    }

    /**
     * Returns the selected section for this enrollment.
     * This corresponds directly to getSection() in the UML.
     *
     * @return enrolled section
     */
    public Section getSection() {
        return section;
    }

    /**
     * Sets the selected section for this enrollment.
     *
     * @param section enrolled section
     */
    public void setSection(final Section section) {
        this.section = section;
    }

    /**
     * Returns the date and time when this enrollment was created.
     *
     * @return enrollment creation time
     */
    public LocalDateTime getEnrolledAt() {
        return enrolledAt;
    }

    /**
     * Sets the enrollment creation time.
     *
     * @param enrolledAt enrollment creation time
     */
    public void setEnrolledAt(final LocalDateTime enrolledAt) {
        this.enrolledAt = enrolledAt;
    }
}