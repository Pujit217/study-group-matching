package edu.ncsu.acm.studygroup.service;

import edu.ncsu.acm.studygroup.entity.Course;
import edu.ncsu.acm.studygroup.entity.Section;
import edu.ncsu.acm.studygroup.entity.StudyGroup;
import edu.ncsu.acm.studygroup.entity.User;
import edu.ncsu.acm.studygroup.repository.StudyGroupRepository;
import org.springframework.stereotype.Service;

import edu.ncsu.acm.studygroup.dto.CreateStudyGroupDto;
import edu.ncsu.acm.studygroup.entity.GroupMember;
import edu.ncsu.acm.studygroup.repository.GroupMemberRepository;
import edu.ncsu.acm.studygroup.repository.CourseRepository;
import edu.ncsu.acm.studygroup.repository.SectionRepository;
import edu.ncsu.acm.studygroup.repository.UserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

/**
 * Handles business logic related to creating, viewing,
 * updating, and managing study groups.
 */
@Service
@Transactional
public class StudyGroupService {

    /**
     * Repository used to access StudyGroup records.
     */
    private final StudyGroupRepository studyGroupRepository;
    /**
     * Repository used to access GroupMember records.
     */
    private final GroupMemberRepository groupMemberRepository;
    /**
     * Repository used to access Course records.
     */
    private final CourseRepository courseRepository;
    /**
     * Repository used to access Section records.
     */
    private final SectionRepository sectionRepository;
    /**
     * Repository used to access User records.
     */
    private final UserRepository userRepository;

    /**
     * Creates the service using constructor injection.
     *
     * @param studyGroupRepository repository used for study-group persistence
     */
    public StudyGroupService(
            final StudyGroupRepository studyGroupRepository,
            final GroupMemberRepository groupMemberRepository,
            final CourseRepository courseRepository,
            final SectionRepository sectionRepository,
            final UserRepository userRepository) {
        this.studyGroupRepository = studyGroupRepository;
        this.groupMemberRepository = groupMemberRepository;
        this.courseRepository = courseRepository;
        this.sectionRepository = sectionRepository;
        this.userRepository = userRepository;
    }

    /**
     * Returns all study groups currently stored in the database.
     *
     * @return list of all study groups
     */
    @Transactional(readOnly = true)
    public List<StudyGroup> getAllStudyGroups() {
        return studyGroupRepository.findAll();
    }

    /**
     * Finds a study group using its database identifier.
     *
     * @param groupId study-group ID
     * @return matching study group, if one exists
     */
    @Transactional(readOnly = true)
    public Optional<StudyGroup> getStudyGroupById(
            final Long groupId) {
        return studyGroupRepository.findById(groupId);
    }

    /**
     * Creates and stores a new study group.
     *
     * @param dto study group to save
     * @return saved study group
     */
    public StudyGroup createStudyGroup(
            final CreateStudyGroupDto dto) {

        if (dto.getName() == null || dto.getName().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Study group name is required."
            );
        }
        if (dto.getCourseId() == null
                || dto.getCreatedByUserId() == null) {

            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Course ID and creator ID are required."
            );
        }

        if (dto.getMaxSize() < 1) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Maximum group size must be at least 1."
            );
        }

        Course course = courseRepository.findById(dto.getCourseId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Course not found."
                ));

        User creator = userRepository.findById(
                        dto.getCreatedByUserId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Creator not found."
                ));
        Section section = null;

        if (dto.getSectionId() != null) {
            section = sectionRepository.findById(dto.getSectionId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Section not found."
                    ));
        }
        StudyGroup group = new StudyGroup(
                dto.getName().trim(),
                course,
                creator
        );

        group.setDescription(dto.getDescription());
        group.setSection(section);
        group.setMaxSize(dto.getMaxSize());
        group.setStudyMode(dto.getStudyMode());
        group.setMeetingLocation(dto.getMeetingLocation());
        group.setMeetingTimes(dto.getMeetingTimes());

        group.setMeetingDurationMinutes(
                dto.getMeetingDurationMinutes()
        );
        StudyGroup savedGroup = studyGroupRepository.save(group);
        GroupMember membership = new GroupMember(
                creator,
                savedGroup
        );

        GroupMember savedMembership =
                groupMemberRepository.save(membership);

        savedGroup.getMembers().add(savedMembership);

        return savedGroup;
    }

    /**
     * Saves changes made to an existing study group.
     *
     * @param studyGroup group containing updated information
     * @return updated study group
     */
    public StudyGroup updateStudyGroup(
            final StudyGroup studyGroup) {
        return studyGroupRepository.save(studyGroup);
    }

    /**
     * Deletes a study group using its database identifier.
     *
     * @param groupId study-group ID
     */
    public boolean deleteStudyGroup(final Long groupId) {
        if (!studyGroupRepository.existsById(groupId)){
            return false;
        }
        studyGroupRepository.deleteById(groupId);
        return true;
    }

    /**
     * Returns all members in a study group
     * @param groupId the group id of the study group
     * @return the list of all group members in study group with given group id
     */
    @Transactional(readOnly = true)
    public List<GroupMember> getGroupMembers(
            final Long groupId) {

        StudyGroup group = findGroup(groupId);

        return groupMemberRepository.findByStudyGroup(group);
    }

    /**
     * adds member and returns the added membership
     * @param groupId the group to search for
     * @param userId the user to insert
     * @return the newly added member
     */
    public GroupMember addMember(
            final Long groupId,
            final Long userId) {

        StudyGroup group = findGroup(groupId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found."
                ));
        if (groupMemberRepository
                .findByUserAndStudyGroup(user, group)
                .isPresent()) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "User is already a member of this group."
            );
        }
        long currentMembers =
                groupMemberRepository.countByStudyGroup(group);

        if (group.getMaxSize() > 0
                && currentMembers >= group.getMaxSize()) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Study group is full."
            );
        }

        GroupMember membership = new GroupMember(user, group);
        GroupMember savedMembership = groupMemberRepository.save(membership);
        group.getMembers().add(savedMembership);
        return savedMembership;
    }

    /**
     * Removes member and returns true if successfully removed
     * @param groupId the group to search for
     * @param userId the user to search for
     * @return true if member successfully removed
     */
    public boolean removeMember(
            final Long groupId,
            final Long userId) {

        StudyGroup group = findGroup(groupId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found."
                ));

        Optional<GroupMember> membership =
                groupMemberRepository.findByUserAndStudyGroup(
                        user,
                        group
                );

        if (membership.isEmpty()) {
            return false;
        }

        GroupMember existingMembership = membership.get();

        group.getMembers().remove(existingMembership);
        groupMemberRepository.delete(existingMembership);

        return true;
    }

    /**
     * Returns all study groups associated with a course.
     *
     * @param course target course
     * @return matching study groups
     */
    public List<StudyGroup> getStudyGroupsByCourse(
            final Course course) {
        return studyGroupRepository.findByCourse(course);
    }

    /**
     * Returns study groups associated with a specific section.
     *
     * @param section target course section
     * @return matching study groups
     */
    public List<StudyGroup> getStudyGroupsBySection(
            final Section section) {
        return studyGroupRepository.findBySection(section);
    }

    /**
     * Returns study groups created by a specific user.
     *
     * @param user group creator
     * @return study groups created by the user
     */
    public List<StudyGroup> getStudyGroupsCreatedBy(
            final User user) {
        return studyGroupRepository.findByCreatedBy(user);
    }

    /**
     * Checks whether a study group has room for another member.
     *
     * @param studyGroup group being checked
     * @return true if another member can join
     */
    public boolean hasAvailableSpace(
            final StudyGroup studyGroup) {
        return !studyGroup.isFull();
    }

    /**
     * Finds a group by given group id
     * @param groupId to search for the group
     * @return the study group if found.
     */
    private StudyGroup findGroup(final Long groupId) {

        return studyGroupRepository.findById(groupId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Study group not found."
                ));
    }
}