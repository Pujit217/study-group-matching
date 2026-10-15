package edu.ncsu.acm.studygroup.controller;

import edu.ncsu.acm.studygroup.entity.StudyGroup;
import edu.ncsu.acm.studygroup.service.StudyGroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Provides REST API endpoints for creating and managing study groups.
 */
@RestController
@RequestMapping("/api/study-groups")
public class StudyGroupController {

    /**
     * Service used to perform study-group operations.
     */
    private final StudyGroupService studyGroupService;

    /**
     * Creates the controller using constructor injection.
     *
     * @param studyGroupService service used for study-group operations
     */
    public StudyGroupController(
            final StudyGroupService studyGroupService) {

        this.studyGroupService = studyGroupService;
    }

    /**
     * Returns all study groups.
     *
     * @return list of study groups
     */
    @GetMapping
    public List<StudyGroup> getAllStudyGroups() {
        return studyGroupService.getAllStudyGroups();
    }

    /**
     * Returns one study group by database ID.
     *
     * @param groupId study-group ID
     * @return matching study group or 404
     */
    @GetMapping("/{groupId}")
    public ResponseEntity<StudyGroup> getStudyGroupById(
            @PathVariable final Long groupId) {

        return studyGroupService.getStudyGroupById(groupId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Creates a new study group.
     *
     * @param studyGroup study-group information
     * @return newly created study group
     */
    @PostMapping
    public ResponseEntity<StudyGroup> createStudyGroup(
            @RequestBody final StudyGroup studyGroup) {

        return ResponseEntity.ok(
                studyGroupService.createStudyGroup(studyGroup)
        );
    }

    /**
     * Updates an existing study group.
     *
     * @param groupId ID of the group being updated
     * @param updatedGroup new group information
     * @return updated group or 404
     */
    @PutMapping("/{groupId}")
    public ResponseEntity<StudyGroup> updateStudyGroup(
            @PathVariable final Long groupId,
            @RequestBody final StudyGroup updatedGroup) {

        return studyGroupService.getStudyGroupById(groupId)
                .map(existingGroup -> {

                    existingGroup.updateGroupInfo(
                            updatedGroup.getName(),
                            updatedGroup.getDescription(),
                            updatedGroup.getMaxSize(),
                            updatedGroup.getStudyMode(),
                            updatedGroup.getMeetingLocation(),
                            updatedGroup.getMeetingTimes(),
                            updatedGroup.getMeetingDurationMinutes()
                    );

                    return ResponseEntity.ok(
                            studyGroupService
                                    .updateStudyGroup(existingGroup)
                    );
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Deletes a study group.
     *
     * @param groupId study-group ID
     * @return 204 after successful deletion
     */
    @DeleteMapping("/{groupId}")
    public ResponseEntity<Void> deleteStudyGroup(
            @PathVariable final Long groupId) {

        if (studyGroupService
                .getStudyGroupById(groupId)
                .isEmpty()) {

            return ResponseEntity.notFound().build();
        }

        studyGroupService.deleteStudyGroup(groupId);

        return ResponseEntity.noContent().build();
    }
}