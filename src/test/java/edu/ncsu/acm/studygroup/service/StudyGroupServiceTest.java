package edu.ncsu.acm.studygroup.service;

import edu.ncsu.acm.studygroup.dto.CreateStudyGroupDto;
import edu.ncsu.acm.studygroup.entity.Course;
import edu.ncsu.acm.studygroup.entity.Section;
import edu.ncsu.acm.studygroup.entity.StudyGroup;
import edu.ncsu.acm.studygroup.entity.User;
import edu.ncsu.acm.studygroup.entity.GroupMember;

import edu.ncsu.acm.studygroup.repository.StudyGroupRepository;
import edu.ncsu.acm.studygroup.repository.GroupMemberRepository;
import edu.ncsu.acm.studygroup.repository.CourseRepository;
import edu.ncsu.acm.studygroup.repository.SectionRepository;
import edu.ncsu.acm.studygroup.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * tests for StudyGroupService
 */
public class StudyGroupServiceTest {

    /** study group repository object for testing */
    private StudyGroupRepository studyGroupRepository;
    /** group member repository object for testing */
    private GroupMemberRepository groupMemberRepository;
    /** course repository object for testing */
    private CourseRepository courseRepository;
    /** section repository object for testing */
    private SectionRepository sectionRepository;
    /** user repository object for testing */
    private UserRepository userRepository;
    /** study group service object for testing */
    private StudyGroupService studyGroupService;

    /**
     * Initializes mock repositories and creates a study group service instance before each test
     */
    @BeforeEach
    public void setUp() {

        studyGroupRepository = mock(StudyGroupRepository.class);
        groupMemberRepository = mock(GroupMemberRepository.class);
        courseRepository = mock(CourseRepository.class);
        sectionRepository = mock(SectionRepository.class);
        userRepository = mock(UserRepository.class);

        studyGroupService = new StudyGroupService(
                studyGroupRepository,
                groupMemberRepository,
                courseRepository,
                sectionRepository,
                userRepository
        );
    }

    /**
     * Creates a study group for testing purposes
     * @param maxSize maximum group size
     * @return test study group
     */
    private StudyGroup createGroup(final int maxSize) {

        Course course = mock(Course.class);
        User creator = mock(User.class);

        StudyGroup group =
                new StudyGroup("CSC 216 Study Group", course, creator);

        group.setMaxSize(maxSize);

        return group;
    }

    /**
     * Creates a valid study-group creation dto
     * @return test creation dto
     */
    private CreateStudyGroupDto createValidDto() {

        CreateStudyGroupDto dto = new CreateStudyGroupDto();

        dto.setName("CSC 216 Study Group");
        dto.setDescription("Study group for CSC 216.");
        dto.setCourseId(1L);
        dto.setSectionId(2L);
        dto.setCreatedByUserId(10L);
        dto.setMaxSize(6);
        dto.setStudyMode("In-person");
        dto.setMeetingLocation("Hunt Library");
        dto.setMeetingTimes("Monday 6 PM");
        dto.setMeetingDurationMinutes(90);

        return dto;
    }

    /**
     * Tests returning all the study groups
     */
    @Test
    void getAllStudyGroups_returnsAllGroups() {

        List<StudyGroup> expectedGroups =
                List.of(createGroup(5));

        when(studyGroupRepository.findAll())
                .thenReturn(expectedGroups);

        List<StudyGroup> result =
                studyGroupService.getAllStudyGroups();

        assertEquals(expectedGroups, result);

        verify(studyGroupRepository).findAll();
    }

    /**
     * Tests returning a study group using the group id
     */
    @Test
    void getStudyGroupById_returnsGroupWhenFound() {

        StudyGroup group = createGroup(5);

        when(studyGroupRepository.findById(1L))
                .thenReturn(Optional.of(group));

        Optional<StudyGroup> result =
                studyGroupService.getStudyGroupById(1L);

        assertTrue(result.isPresent());
        assertSame(group, result.get());

        verify(studyGroupRepository).findById(1L);
    }

    /**
     * Tests creating a study group and adding the creator as first member
     */
    @Test
    void createStudyGroup_createsGroupAndAddsCreator() {

        CreateStudyGroupDto dto = createValidDto();

        Course course = mock(Course.class);
        Section section = mock(Section.class);
        User creator = mock(User.class);

        when(courseRepository.findById(1L))
                .thenReturn(Optional.of(course));

        when(sectionRepository.findById(2L))
                .thenReturn(Optional.of(section));

        when(userRepository.findById(10L))
                .thenReturn(Optional.of(creator));

        when(studyGroupRepository.save(any(StudyGroup.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(groupMemberRepository.save(any(GroupMember.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        StudyGroup result =
                studyGroupService.createStudyGroup(dto);

        assertEquals("CSC 216 Study Group", result.getName());
        assertEquals("Study group for CSC 216.", result.getDescription());
        assertSame(course, result.getCourse());
        assertSame(section, result.getSection());
        assertSame(creator, result.getCreatedBy());
        assertEquals(6, result.getMaxSize());
        assertEquals("In-person", result.getStudyMode());
        assertEquals("Hunt Library", result.getMeetingLocation());
        assertEquals("Monday 6 PM", result.getMeetingTimes());
        assertEquals(90, result.getMeetingDurationMinutes());
        assertEquals(1, result.getMembers().size());

        GroupMember member = result.getMembers().get(0);

        assertSame(creator, member.getUser());
        assertSame(result, member.getStudyGroup());
        verify(studyGroupRepository).save(any(StudyGroup.class));
        verify(groupMemberRepository).save(any(GroupMember.class));
    }

    /**
     * Tests to ensure that blank group name is rejected
     */
    @Test
    public void createStudyGroup_throwsExceptionWhenNameIsBlank() {

        CreateStudyGroupDto dto = createValidDto();
        dto.setName(" ");

        ResponseStatusException exception =
                assertThrows(
                        ResponseStatusException.class,
                        () -> studyGroupService.createStudyGroup(dto)
                );

        assertEquals(400, exception.getStatusCode().value());

        verifyNoInteractions(
                studyGroupRepository,
                groupMemberRepository,
                courseRepository,
                sectionRepository,
                userRepository
        );
    }

    /**
     * Tests that creating a group with a nonexistent course returns an exception
     */
    @Test
    public void createStudyGroup_throwsExceptionWhenCourseDoesNotExist() {

        CreateStudyGroupDto dto = createValidDto();

        when(courseRepository.findById(1L))
                .thenReturn(Optional.empty());

        ResponseStatusException exception =
                assertThrows(
                        ResponseStatusException.class,
                        () -> studyGroupService.createStudyGroup(dto)
                );

        assertEquals(404, exception.getStatusCode().value());

        verify(studyGroupRepository, never())
                .save(any(StudyGroup.class));
    }

    /**
     * Tests updating an existing study group
     */
    @Test
    public void updateStudyGroup_savesUpdatedGroup() {

        StudyGroup group = createGroup(5);

        when(studyGroupRepository.save(group))
                .thenReturn(group);

        StudyGroup result =
                studyGroupService.updateStudyGroup(group);

        assertSame(group, result);

        verify(studyGroupRepository).save(group);
    }

    /**
     * Tests deleting an existing study group
     */
    @Test
    public void deleteStudyGroup_returnsTrueWhenGroupExists() {

        when(studyGroupRepository.existsById(1L))
                .thenReturn(true);

        boolean result =
                studyGroupService.deleteStudyGroup(1L);

        assertTrue(result);

        verify(studyGroupRepository).deleteById(1L);
    }

    /**
     * Tests deleting a non-existent study group
     */
    @Test
    public void deleteStudyGroup_returnsFalseWhenGroupDoesNotExist() {

        when(studyGroupRepository.existsById(1L))
                .thenReturn(false);

        boolean result =
                studyGroupService.deleteStudyGroup(1L);

        assertFalse(result);

        verify(studyGroupRepository, never()).deleteById(1L);
    }

    /**
     * Tests returning all members of a study group
     */
    @Test
    public void getGroupMembers_returnsGroupMembers() {

        StudyGroup group = createGroup(5);

        GroupMember member =
                new GroupMember(mock(User.class), group);

        List<GroupMember> expectedMembers =
                List.of(member);

        when(studyGroupRepository.findById(1L))
                .thenReturn(Optional.of(group));

        when(groupMemberRepository.findByStudyGroup(group))
                .thenReturn(expectedMembers);

        List<GroupMember> result =
                studyGroupService.getGroupMembers(1L);

        assertEquals(expectedMembers, result);

        verify(groupMemberRepository).findByStudyGroup(group);
    }

    /**
     * Tests successfully adding a user to a study group
     */
    @Test
    public void addMember_addsUserSuccessfully() {

        StudyGroup group = createGroup(5);
        User user = mock(User.class);

        when(studyGroupRepository.findById(1L))
                .thenReturn(Optional.of(group));

        when(userRepository.findById(10L))
                .thenReturn(Optional.of(user));

        when(groupMemberRepository.findByUserAndStudyGroup(user, group))
                .thenReturn(Optional.empty());

        when(groupMemberRepository.countByStudyGroup(group))
                .thenReturn(1L);

        when(groupMemberRepository.save(any(GroupMember.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        GroupMember result =
                studyGroupService.addMember(1L, 10L);

        assertNotNull(result);
        assertSame(user, result.getUser());
        assertSame(group, result.getStudyGroup());

        assertEquals(1, group.getMembers().size());
        assertSame(result, group.getMembers().get(0));

        verify(groupMemberRepository).save(any(GroupMember.class));
    }

    /**
     * Tests that the program rejects it when an existing member is added
     */
    @Test
    public void addMember_throwsExceptionWhenUserIsAlreadyMember() {

        StudyGroup group = createGroup(5);
        User user = mock(User.class);

        GroupMember existingMembership =
                new GroupMember(user, group);

        when(studyGroupRepository.findById(1L))
                .thenReturn(Optional.of(group));

        when(userRepository.findById(10L))
                .thenReturn(Optional.of(user));

        when(groupMemberRepository.findByUserAndStudyGroup(user, group))
                .thenReturn(Optional.of(existingMembership));

        ResponseStatusException exception =
                assertThrows(
                        ResponseStatusException.class,
                        () -> studyGroupService.addMember(1L, 10L)
                );

        assertEquals(409, exception.getStatusCode().value());

        verify(groupMemberRepository, never())
                .save(any(GroupMember.class));
    }

    /**
     * Tests that adding a member to an already full group doesn't work
     */
    @Test
    public void addMember_throwsExceptionWhenGroupIsFull() {

        StudyGroup group = createGroup(2);
        User user = mock(User.class);

        when(studyGroupRepository.findById(1L))
                .thenReturn(Optional.of(group));

        when(userRepository.findById(10L))
                .thenReturn(Optional.of(user));

        when(groupMemberRepository.findByUserAndStudyGroup(user, group))
                .thenReturn(Optional.empty());

        when(groupMemberRepository.countByStudyGroup(group))
                .thenReturn(2L);

        ResponseStatusException exception =
                assertThrows(
                        ResponseStatusException.class,
                        () -> studyGroupService.addMember(1L, 10L)
                );

        assertEquals(409, exception.getStatusCode().value());

        verify(groupMemberRepository, never())
                .save(any(GroupMember.class));
    }

    /**
     * Tests removing a member from a study group
     */
    @Test
    public void removeMember_removesUserSuccessfully() {

        StudyGroup group = createGroup(5);
        User user = mock(User.class);

        GroupMember membership =
                new GroupMember(user, group);

        group.getMembers().add(membership);

        when(studyGroupRepository.findById(1L))
                .thenReturn(Optional.of(group));

        when(userRepository.findById(10L))
                .thenReturn(Optional.of(user));

        when(groupMemberRepository.findByUserAndStudyGroup(user, group))
                .thenReturn(Optional.of(membership));

        boolean result =
                studyGroupService.removeMember(1L, 10L);

        assertTrue(result);
        assertTrue(group.getMembers().isEmpty());
    }

    /**
     * Tests removing a user who is not a member of the given group
     */
    @Test
    public void removeMember_returnsFalseWhenUserIsNotMember() {

        StudyGroup group = createGroup(5);
        User user = mock(User.class);

        when(studyGroupRepository.findById(1L))
                .thenReturn(Optional.of(group));

        when(userRepository.findById(10L))
                .thenReturn(Optional.of(user));

        when(groupMemberRepository.findByUserAndStudyGroup(user, group))
                .thenReturn(Optional.empty());

        boolean result =
                studyGroupService.removeMember(1L, 10L);

        assertFalse(result);
        assertTrue(group.getMembers().isEmpty());
    }

    /**
     * Tests checking whether a group has available space
     */
    @Test
    public void hasAvailableSpace_returnsCorrectResult() {

        StudyGroup group = createGroup(2);

        assertTrue(studyGroupService.hasAvailableSpace(group));

        group.addMember(mock(User.class));
        assertTrue(studyGroupService.hasAvailableSpace(group));

        group.addMember(mock(User.class));
        assertFalse(studyGroupService.hasAvailableSpace(group));
    }
}