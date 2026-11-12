package edu.ncsu.acm.studygroup.controller;

import edu.ncsu.acm.studygroup.dto.CreateStudyGroupDto;
import edu.ncsu.acm.studygroup.entity.Course;
import edu.ncsu.acm.studygroup.entity.User;
import edu.ncsu.acm.studygroup.entity.StudyGroup;
import edu.ncsu.acm.studygroup.entity.GroupMember;

import edu.ncsu.acm.studygroup.service.StudyGroupService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for StudyGroupController class
 */
public class StudyGroupControllerTest {

    /**
     * study group service object for testing
     */
    private StudyGroupService studyGroupService;
    /**
     * study group controller object for testing
     */
    private StudyGroupController studyGroupController;

    /**
     * Creates a mock service and puts it into the controller before each test
     */
    @BeforeEach
    public void setUp() {

        studyGroupService = mock(StudyGroupService.class);

        studyGroupController =
                new StudyGroupController(studyGroupService);
    }

    /**
     * Creates a study group for testing
     * @return test study group
     */
    private StudyGroup createGroup() {

        return new StudyGroup(
                "CSC 216 Study Group",
                mock(Course.class),
                mock(User.class)
        );
    }

    /**
     * Tests returning all study groups
     */
    @Test
    public void getAllStudyGroups_returnsAllGroups() {

        List<StudyGroup> expectedGroups =
                List.of(createGroup());

        when(studyGroupService.getAllStudyGroups())
                .thenReturn(expectedGroups);

        List<StudyGroup> result =
                studyGroupController.getAllStudyGroups();

        assertEquals(expectedGroups, result);
        verify(studyGroupService).getAllStudyGroups();
    }

    /**
     * Tests returning an existing study group
     */
    @Test
    public void getStudyGroupById_returnsGroupWhenFound() {

        StudyGroup group = createGroup();

        when(studyGroupService.getStudyGroupById(1L))
                .thenReturn(Optional.of(group));

        ResponseEntity<StudyGroup> response =
                studyGroupController.getStudyGroupById(1L);

        assertEquals(200, response.getStatusCode().value());
        assertSame(group, response.getBody());
    }

    /**
     * Tests retrieving a study group that does not exist
     */
    @Test
    public void getStudyGroupById_returnsNotFoundWhenMissing() {

        when(studyGroupService.getStudyGroupById(1L))
                .thenReturn(Optional.empty());

        ResponseEntity<StudyGroup> response =
                studyGroupController.getStudyGroupById(1L);

        assertEquals(404, response.getStatusCode().value());
        assertNull(response.getBody());
    }

    /**
     * Tests creating a study group
     */
    @Test
    public void createStudyGroup_returnsCreatedGroup() {

        CreateStudyGroupDto dto = new CreateStudyGroupDto();

        StudyGroup createdGroup = createGroup();

        when(studyGroupService.createStudyGroup(dto))
                .thenReturn(createdGroup);

        ResponseEntity<StudyGroup> response =
                studyGroupController.createStudyGroup(dto);

        assertEquals(200, response.getStatusCode().value());
        assertSame(createdGroup, response.getBody());
        verify(studyGroupService).createStudyGroup(dto);
    }

    /**
     * Tests updating an existing study group
     */
    @Test
    public void updateStudyGroup_returnsUpdatedGroup() {

        StudyGroup existingGroup = createGroup();

        StudyGroup updatedGroup = new StudyGroup();

        updatedGroup.setName("Updated CSC 216 Group");
        updatedGroup.setDescription("Updated description.");
        updatedGroup.setMaxSize(8);
        updatedGroup.setStudyMode("Online");
        updatedGroup.setMeetingLocation("Zoom");
        updatedGroup.setMeetingTimes("Tuesday 7 PM");
        updatedGroup.setMeetingDurationMinutes(60);

        when(studyGroupService.getStudyGroupById(1L))
                .thenReturn(Optional.of(existingGroup));

        when(studyGroupService.updateStudyGroup(existingGroup))
                .thenReturn(existingGroup);

        ResponseEntity<StudyGroup> response =
                studyGroupController.updateStudyGroup(
                        1L,
                        updatedGroup
                );

        assertEquals(200, response.getStatusCode().value());
        assertSame(existingGroup, response.getBody());
        assertEquals(
                "Updated CSC 216 Group",
                existingGroup.getName()
        );
        assertEquals(
                "Updated description.",
                existingGroup.getDescription()
        );

        assertEquals(8, existingGroup.getMaxSize());
        assertEquals("Online", existingGroup.getStudyMode());
        verify(studyGroupService).updateStudyGroup(existingGroup);
    }

    /**
     * Tests updating a non-existent study group
     */
    @Test
    public void updateStudyGroup_returnsNotFoundWhenMissing() {

        StudyGroup updatedGroup = createGroup();

        when(studyGroupService.getStudyGroupById(1L))
                .thenReturn(Optional.empty());

        ResponseEntity<StudyGroup> response =
                studyGroupController.updateStudyGroup(
                        1L,
                        updatedGroup
                );

        assertEquals(404, response.getStatusCode().value());
        verify(studyGroupService, never())
                .updateStudyGroup(any(StudyGroup.class));
    }

    /**
     * Tests deleting a valid study group
     */
    @Test
    public void deleteStudyGroup_returnsNoContentWhenDeleted() {

        StudyGroup group = createGroup();

        when(studyGroupService.getStudyGroupById(1L))
                .thenReturn(Optional.of(group));

        ResponseEntity<Void> response =
                studyGroupController.deleteStudyGroup(1L);

        assertEquals(204, response.getStatusCode().value());
        verify(studyGroupService).deleteStudyGroup(1L);
    }

    /**
     * Tests deleting a non-existent  study group
     */
    @Test
    public void deleteStudyGroup_returnsNotFoundWhenMissing() {

        when(studyGroupService.getStudyGroupById(1L))
                .thenReturn(Optional.empty());

        ResponseEntity<Void> response =
                studyGroupController.deleteStudyGroup(1L);

        assertEquals(404, response.getStatusCode().value());
        verify(studyGroupService, never()).deleteStudyGroup(1L);
    }

    /**
     * Tests returning all members of a study group
     */
    @Test
    public void getGroupMembers_returnsAllMembers() {

        GroupMember member =
                new GroupMember(mock(User.class), createGroup());

        List<GroupMember> expectedMembers =
                List.of(member);

        when(studyGroupService.getGroupMembers(1L))
                .thenReturn(expectedMembers);

        List<GroupMember> result =
                studyGroupController.getGroupMembers(1L);

        assertEquals(expectedMembers, result);
        verify(studyGroupService).getGroupMembers(1L);
    }

    /**
     * Tests adding a member to a study group
     */
    @Test
    public void addMember_returnsAddedMember() {

        GroupMember member =
                new GroupMember(mock(User.class), createGroup());

        when(studyGroupService.addMember(1L, 10L))
                .thenReturn(member);

        ResponseEntity<GroupMember> response =
                studyGroupController.addMember(1L, 10L);

        assertEquals(200, response.getStatusCode().value());
        assertSame(member, response.getBody());
        verify(studyGroupService).addMember(1L, 10L);
    }

    /**
     * Tests removing a member from a group successfully
     */
    @Test
    public void removeMember_returnsNoContentWhenRemoved() {

        when(studyGroupService.removeMember(1L, 10L))
                .thenReturn(true);

        ResponseEntity<Void> response =
                studyGroupController.removeMember(1L, 10L);
        assertEquals(204, response.getStatusCode().value());
        verify(studyGroupService).removeMember(1L, 10L);
    }

    /**
     * Tests removing a user who is not a member of the group
     */
    @Test
    public void removeMember_returnsNotFoundWhenNotRemoved() {

        when(studyGroupService.removeMember(1L, 10L))
                .thenReturn(false);

        ResponseEntity<Void> response =
                studyGroupController.removeMember(1L, 10L);
        assertEquals(404, response.getStatusCode().value());
        verify(studyGroupService).removeMember(1L, 10L);
    }
}