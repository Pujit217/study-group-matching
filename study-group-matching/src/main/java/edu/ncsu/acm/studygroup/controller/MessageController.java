package edu.ncsu.acm.studygroup.controller;

import edu.ncsu.acm.studygroup.entity.Message;
import edu.ncsu.acm.studygroup.entity.StudyGroup;
import edu.ncsu.acm.studygroup.service.MessageService;
import edu.ncsu.acm.studygroup.service.StudyGroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Provides REST API endpoints for study-group chat messages.
 */
@RestController
@RequestMapping("/api/messages")
public class MessageController {

    /**
     * Service used for message operations.
     */
    private final MessageService messageService;

    /**
     * Service used to find study groups.
     */
    private final StudyGroupService studyGroupService;

    /**
     * Creates the controller using constructor injection.
     *
     * @param messageService service used for messages
     * @param studyGroupService service used for study groups
     */
    public MessageController(
            final MessageService messageService,
            final StudyGroupService studyGroupService) {

        this.messageService = messageService;
        this.studyGroupService = studyGroupService;
    }

    /**
     * Returns all messages belonging to a study group.
     *
     * @param groupId study-group ID
     * @return ordered group chat messages or 404
     */
    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<Message>> getGroupMessages(
            @PathVariable final Long groupId) {

        return studyGroupService.getStudyGroupById(groupId)
                .map(group ->
                        ResponseEntity.ok(
                                messageService
                                        .getMessagesForGroup(group)
                        )
                )
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Creates and stores a new chat message.
     *
     * @param message message information
     * @return saved message
     */
    @PostMapping
    public ResponseEntity<Message> sendMessage(
            @RequestBody final Message message) {

        return ResponseEntity.ok(
                messageService.sendMessage(message)
        );
    }

    /**
     * Updates the text of an existing message.
     *
     * @param messageId message ID
     * @param newContent replacement message content
     * @return updated message or 404
     */
    @PutMapping("/{messageId}")
    public ResponseEntity<Message> editMessage(
            @PathVariable final Long messageId,
            @RequestBody final String newContent) {

        return messageService.getMessageById(messageId)
                .map(message ->
                        ResponseEntity.ok(
                                messageService.editMessage(
                                        message,
                                        newContent
                                )
                        )
                )
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Deletes a message.
     *
     * @param messageId message ID
     * @return 204 after successful deletion
     */
    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> deleteMessage(
            @PathVariable final Long messageId) {

        if (messageService
                .getMessageById(messageId)
                .isEmpty()) {

            return ResponseEntity.notFound().build();
        }

        messageService.deleteMessage(messageId);

        return ResponseEntity.noContent().build();
    }
}