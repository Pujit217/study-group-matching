package edu.ncsu.acm.studygroup.controller;

import edu.ncsu.acm.studygroup.dto.JoinRequestDto;
import edu.ncsu.acm.studygroup.dto.JoinRequestResponseDto;
import edu.ncsu.acm.studygroup.entity.JoinRequest;
import edu.ncsu.acm.studygroup.entity.StudyGroup;
import edu.ncsu.acm.studygroup.entity.User;
import edu.ncsu.acm.studygroup.service.JoinRequestService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import java.util.List;

/**
 * Provides REST API endpoints for managing requests
 * to join study groups.
 */
@RestController
@RequestMapping("/api/join-requests")
public class JoinRequestController {

    /**
     * Service used to perform join-request operations.
     */
    private final JoinRequestService joinRequestService;

    /**
     * Creates the controller using constructor injection.
     *
     * @param joinRequestService service used for join requests
     */
    public JoinRequestController(
            final JoinRequestService joinRequestService) {

        this.joinRequestService = joinRequestService;
    }

    /**
     * Returns all join requests.
     *
     * @return list of join requests
     */
    @GetMapping
    public List<JoinRequestResponseDto> getAllJoinRequests() {

        return joinRequestService
                .getAllJoinRequests()
                .stream()
                .map(JoinRequestResponseDto::fromEntity)
                .toList();
    }


    /**
     * Creates a new request to join a study group.
     *
     * @param request join request information
     * @return newly created request
     */

    @PostMapping
    public ResponseEntity<JoinRequestResponseDto> createJoinRequest(
            @Valid @RequestBody final JoinRequestDto dto) {

        // Create references using the incoming IDs
        User user = new User();
        user.setUserId(dto.getUserId());

        StudyGroup group = new StudyGroup();
        group.setGroupId(dto.getStudyGroupId());

        // Build a join request
        JoinRequest request = new JoinRequest(
                user,
                group,
                dto.getMessage()
        );

        // Service validates and loads real database entities
        JoinRequest savedRequest =
                joinRequestService.createJoinRequest(request);

        // Return a safe response with HTTP 201 Created
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(JoinRequestResponseDto.fromEntity(savedRequest));
    }


    /**
     * Accepts an existing pending join request.
     *
     * @param requestId join-request ID
     * @return updated request or 404
     */
    @PutMapping("/{requestId}/accept")
    public ResponseEntity<JoinRequestResponseDto> acceptRequest(
            @PathVariable final Long requestId) {

        return joinRequestService
                .getJoinRequestById(requestId)
                .map(request -> {

                    JoinRequest accepted =
                            joinRequestService.acceptRequest(request);

                    return ResponseEntity.ok(
                            JoinRequestResponseDto.fromEntity(accepted)
                    );
                })
                .orElseGet(() ->
                        ResponseEntity.notFound().build());
    }


    /**
     * Rejects an existing pending join request.
     *
     * @param requestId join-request ID
     * @return updated request or 404
     */
    @PutMapping("/{requestId}/reject")
    public ResponseEntity<JoinRequestResponseDto> rejectRequest(
            @PathVariable final Long requestId) {

        return joinRequestService
                .getJoinRequestById(requestId)
                .map(request -> {

                    JoinRequest rejected =
                            joinRequestService.rejectRequest(request);

                    return ResponseEntity.ok(
                            JoinRequestResponseDto.fromEntity(rejected)
                    );
                })
                .orElseGet(() ->
                        ResponseEntity.notFound().build());
    }


    /**
     * Cancels an existing pending join request.
     *
     * @param requestId join-request ID
     * @return updated request or 404
     */
    @PutMapping("/{requestId}/cancel")
    public ResponseEntity<JoinRequestResponseDto> cancelRequest(
            @PathVariable final Long requestId) {

        return joinRequestService
                .getJoinRequestById(requestId)
                .map(request -> {

                    JoinRequest canceled =
                            joinRequestService.cancelRequest(request);

                    return ResponseEntity.ok(
                            JoinRequestResponseDto.fromEntity(canceled)
                    );
                })
                .orElseGet(() ->
                        ResponseEntity.notFound().build());
    }

}