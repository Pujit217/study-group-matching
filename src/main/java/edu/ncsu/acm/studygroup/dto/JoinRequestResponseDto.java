
package edu.ncsu.acm.studygroup.dto;

import edu.ncsu.acm.studygroup.entity.JoinRequest;
import java.time.LocalDateTime;

public record JoinRequestResponseDto(
        Long requestId,
        Long userId,
        Long studyGroupId,
        String status,
        String message,
        LocalDateTime requestedAt,
        LocalDateTime respondedAt
) {

    public static JoinRequestResponseDto fromEntity(
            JoinRequest request) {

        return new JoinRequestResponseDto(
                request.getRequestId(),
                request.getUser().getUserId(),
                request.getStudyGroup().getGroupId(),
                request.getStatus(),
                request.getMessage(),
                request.getRequestedAt(),
                request.getRespondedAt()
        );
    }
}
