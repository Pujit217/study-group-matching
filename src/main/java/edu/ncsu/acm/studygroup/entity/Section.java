package edu.ncsu.acm.studygroup.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a specific section of an academic course.
 * Sections store details such as section number, instructor, and semester.
 */
@Entity
@Table(name = "sections")
public class Section {

    /**
     * Unique database identifier for this section.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sectionId;

    /**
     * Section number, such as 001 or 002.
     */
    private String sectionNumber;

    /**
     * Name of the instructor teaching this section.
     */
    private String instructor;

    /**
     * Semester in which the section is being offered.
     */
    private String semester;

    /**
     * Course to which this section belongs.
     * Multiple sections may belong to the same course.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    /**
     * Stores all student enrollments associated with this section.
     */
    @OneToMany(
            mappedBy = "section",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Enrollment> enrollments = new ArrayList<>();

    /**
     * Stores study groups that specifically target this section.
     * A study group may also exist without a specific section.
     */
    @OneToMany(mappedBy = "section")
    private List<StudyGroup> studyGroups = new ArrayList<>();

    /**
     * Default constructor required by JPA.
     */
    public Section() {
    }

    /**
     * Creates a course section with its basic academic information.
     *
     * @param sectionNumber section number
     * @param instructor    instructor name
     * @param semester      academic semester
     * @param course        course containing this section
     */
    public Section(final String sectionNumber,
                   final String instructor,
                   final String semester,
                   final Course course) {
        this.sectionNumber = sectionNumber;
        this.instructor = instructor;
        this.semester = semester;
        this.course = course;
    }

    /**
     * Returns the unique database identifier for this section.
     *
     * @return section ID
     */
    public Long getSectionId() {
        return sectionId;
    }

    /**
     * Sets the unique database identifier for this section.
     *
     * @param sectionId section ID
     */
    public void setSectionId(final Long sectionId) {
        this.sectionId = sectionId;
    }

    /**
     * Returns the section number.
     *
     * @return section number
     */
    public String getSectionNumber() {
        return sectionNumber;
    }

    /**
     * Updates the section number.
     *
     * @param sectionNumber new section number
     */
    public void setSectionNumber(final String sectionNumber) {
        this.sectionNumber = sectionNumber;
    }

    /**
     * Returns the name of the instructor teaching this section.
     *
     * @return instructor name
     */
    public String getInstructor() {
        return instructor;
    }

    /**
     * Updates the instructor teaching this section.
     *
     * @param instructor new instructor name
     */
    public void setInstructor(final String instructor) {
        this.instructor = instructor;
    }

    /**
     * Returns the semester in which the section is offered.
     *
     * @return semester
     */
    public String getSemester() {
        return semester;
    }

    /**
     * Updates the semester in which the section is offered.
     *
     * @param semester new semester
     */
    public void setSemester(final String semester) {
        this.semester = semester;
    }

    /**
     * Returns the course to which this section belongs.
     * This method corresponds directly to getCourse() in the UML.
     *
     * @return parent course
     */
    public Course getCourse() {
        return course;
    }

    /**
     * Sets the course to which this section belongs.
     *
     * @param course parent course
     */
    public void setCourse(final Course course) {
        this.course = course;
    }

    /**
     * Returns all enrollments belonging to this section.
     * This method corresponds directly to getEnrollments() in the UML.
     *
     * @return list of enrollments
     */
    public List<Enrollment> getEnrollments() {
        return enrollments;
    }

    /**
     * Replaces the current list of enrollments.
     *
     * @param enrollments new enrollment list
     */
    public void setEnrollments(
            final List<Enrollment> enrollments) {
        this.enrollments = enrollments;
    }

    /**
     * Returns all study groups associated specifically with this section.
     * This method corresponds directly to getStudyGroups() in the UML.
     *
     * @return list of study groups
     */
    public List<StudyGroup> getStudyGroups() {
        return studyGroups;
    }

    /**
     * Replaces the current list of study groups.
     *
     * @param studyGroups new study-group list
     */
    public void setStudyGroups(
            final List<StudyGroup> studyGroups) {
        this.studyGroups = studyGroups;
    }
}