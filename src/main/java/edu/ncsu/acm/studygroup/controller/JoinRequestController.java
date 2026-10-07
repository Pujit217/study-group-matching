package edu.ncsu.acm.studygroup.controller;

import edu.ncsu.acm.studygroup.entity.JoinRequest;
import edu.ncsu.acm.studygroup.service.JoinRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public List<JoinRequest> getAllJoinRequests() {
        return joinRequestService.getAllJoinRequests();
    }

    /**
     * Creates a new request to join a study group.
     *
     * @param request join request information
     * @return newly created request
     */
    @PostMapping
    public ResponseEntity<JoinRequest> createJoinRequest(
            @RequestBody final JoinRequest request) {

        return ResponseEntity.ok(
                joinRequestService.createJoinRequest(request)
        );
    }

    /**
     * Accepts an existing pending join request.
     *
     * @param requestId join-request ID
     * @return updated request or 404
     */
    @PutMapping("/{requestId}/accept")
    public ResponseEntity<JoinRequest> acceptRequest(
            @PathVariable final Long requestId) {

        return joinRequestService
                .getJoinRequestById(requestId)
                .map(request ->
                        ResponseEntity.ok(
                                joinRequestService
                                        .acceptRequest(request)
                        )
                )
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Rejects an existing pending join request.
     *
     * @param requestId join-request ID
     * @return updated request or 404
     */
    @PutMapping("/{requestId}/reject")
    public ResponseEntity<JoinRequest> rejectRequest(
            @PathVariable final Long requestId) {

        return joinRequestService
                .getJoinRequestById(requestId)
                .map(request ->
                        ResponseEntity.ok(
                                joinRequestService
                                        .rejectRequest(request)
                        )
                )
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Cancels an existing pending join request.
     *
     * @param requestId join-request ID
     * @return updated request or 404
     */
    @PutMapping("/{requestId}/cancel")
    public ResponseEntity<JoinRequest> cancelRequest(
            @PathVariable final Long requestId) {

        return joinRequestService
                .getJoinRequestById(requestId)
                .map(request ->
                        ResponseEntity.ok(
                                joinRequestService
                                        .cancelRequest(request)
                        )
                )
                .orElse(ResponseEntity.notFound().build());
    }
}