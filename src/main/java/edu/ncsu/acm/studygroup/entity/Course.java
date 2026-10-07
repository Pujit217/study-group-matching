package edu.ncsu.acm.studygroup.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an academic course such as CSC 316.
 * A course can contain multiple sections, enrollments, and study groups.
 */
@Entity
@Table(name = "courses")
public class Course {

    /**
     * Unique database identifier for the course.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseId;

    /**
     * Official course code, such as CSC 316.
     */
    @Column(unique = true, nullable = false)
    private String courseCode;

    /**
     * Full name of the course.
     */
    private String courseName;

    /**
     * Academic department offering the course.
     */
    private String department;

    /**
     * Stores all sections belonging to this course.
     */
    @OneToMany(
            mappedBy = "course",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Section> sections = new ArrayList<>();

    /**
     * Stores all student enrollments associated with this course.
     */
    @OneToMany(
            mappedBy = "course",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Enrollment> enrollments = new ArrayList<>();

    /**
     * Stores all study groups that were created for this course.
     */
    @OneToMany(
            mappedBy = "course",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<StudyGroup> studyGroups = new ArrayList<>();

    /**
     * Default constructor required by JPA.
     */
    public Course() {
    }

    /**
     * Creates a course using its identifying academic information.
     *
     * @param courseCode course code
     * @param courseName course name
     * @param department academic department
     */
    public Course(final String courseCode,
                  final String courseName,
                  final String department) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.department = department;
    }

    /**
     * Returns the unique database identifier for this course.
     *
     * @return course ID
     */
    public Long getCourseId() {
        return courseId;
    }

    /**
     * Sets the unique database identifier for this course.
     *
     * @param courseId course ID
     */
    public void setCourseId(final Long courseId) {
        this.courseId = courseId;
    }

    /**
     * Returns the official course code.
     *
     * @return course code
     */
    public String getCourseCode() {
        return courseCode;
    }

    /**
     * Updates the official course code.
     *
     * @param courseCode new course code
     */
    public void setCourseCode(final String courseCode) {
        this.courseCode = courseCode;
    }

    /**
     * Returns the full name of the course.
     *
     * @return course name
     */
    public String getCourseName() {
        return courseName;
    }

    /**
     * Updates the full name of the course.
     *
     * @param courseName new course name
     */
    public void setCourseName(final String courseName) {
        this.courseName = courseName;
    }

    /**
     * Returns the department offering the course.
     *
     * @return department name
     */
    public String getDepartment() {
        return department;
    }

    /**
     * Updates the department offering the course.
     *
     * @param department new department name
     */
    public void setDepartment(final String department) {
        this.department = department;
    }

    /**
     * Returns all sections belonging to this course.
     * This method corresponds directly to getSections() in the UML.
     *
     * @return list of course sections
     */
    public List<Section> getSections() {
        return sections;
    }

    /**
     * Replaces the current list of sections.
     *
     * @param sections new section list
     */
    public void setSections(final List<Section> sections) {
        this.sections = sections;
    }

    /**
     * Returns all student enrollments associated with this course.
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
     * Returns all study groups created for this course.
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

    /**
     * Adds a section to this course and maintains the
     * relationship between the two entities.
     *
     * @param section section to add
     */
    public void addSection(final Section section) {
        sections.add(section);
        section.setCourse(this);
    }

    /**
     * Removes a section from this course.
     *
     * @param section section to remove
     */
    public void removeSection(final Section section) {
        sections.remove(section);
    }
}