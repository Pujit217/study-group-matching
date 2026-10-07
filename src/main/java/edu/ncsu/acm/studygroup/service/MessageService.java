package edu.ncsu.acm.studygroup.service;

import edu.ncsu.acm.studygroup.entity.Message;
import edu.ncsu.acm.studygroup.entity.StudyGroup;
import edu.ncsu.acm.studygroup.entity.User;
import edu.ncsu.acm.studygroup.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Handles business logic related to study-group chat messages.
 */
@Service
public class MessageService {

    /**
     * Repository used to access Message records.
     */
    private final MessageRepository messageRepository;

    /**
     * Creates the service using constructor injection.
     *
     * @param messageRepository repository used for message persistence
     */
    public MessageService(
            final MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    /**
     * Returns all messages currently stored in the database.
     *
     * @return list of all messages
     */
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    /**
     * Finds a message by its database identifier.
     *
     * @param messageId message ID
     * @return matching message, if one exists
     */
    public Optional<Message> getMessageById(
            final Long messageId) {
        return messageRepository.findById(messageId);
    }

    /**
     * Returns all messages belonging to a specific study group,
     * ordered from oldest to newest.
     *
     * @param studyGroup study group whose chat should be returned
     * @return ordered list of messages
     */
    public List<Message> getMessagesForGroup(
            final StudyGroup studyGroup) {
        return messageRepository
                .findByStudyGroupOrderBySentAtAsc(studyGroup);
    }

    /**
     * Returns all messages sent by a specific user.
     *
     * @param sender user whose messages should be returned
     * @return list of matching messages
     */
    public List<Message> getMessagesBySender(
            final User sender) {
        return messageRepository.findBySender(sender);
    }

    /**
     * Creates and stores a new chat message.
     *
     * @param message message to save
     * @return saved message
     */
    public Message sendMessage(final Message message) {
        message.send();
        return messageRepository.save(message);
    }

    /**
     * Updates the content of an existing message.
     *
     * @param message message being edited
     * @param newContent replacement message text
     * @return updated message
     */
    public Message editMessage(
            final Message message,
            final String newContent) {
        message.edit(newContent);
        return messageRepository.save(message);
    }

    /**
     * Deletes a message permanently from the database.
     *
     * @param messageId message ID
     */
    public void deleteMessage(final Long messageId) {
        messageRepository.deleteById(messageId);
    }
}