package edu.ncsu.acm.studygroup.dto;

import java.time.LocalDateTime;

/**
 * Represents message information returned to the frontend.
 */
public record MessageResponseDto(
        Long messageId,
        Long studyGroupId,
        Long senderId,
        String senderName,
        String content,
        LocalDateTime sentAt,
        LocalDateTime editedAt
) {
}