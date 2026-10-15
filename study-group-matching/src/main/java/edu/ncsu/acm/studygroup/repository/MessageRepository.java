package edu.ncsu.acm.studygroup.repository;

import edu.ncsu.acm.studygroup.entity.Message;
import edu.ncsu.acm.studygroup.entity.StudyGroup;
import edu.ncsu.acm.studygroup.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Provides database access operations for Message entities.
 */
public interface MessageRepository
        extends JpaRepository<Message, Long> {

    /**
     * Finds all messages belonging to a specific study group.
     *
     * @param studyGroup group whose messages should be returned
     * @return list of matching messages
     */
    List<Message> findByStudyGroup(StudyGroup studyGroup);

    /**
     * Finds all messages sent by a specific user.
     *
     * @param sender user who sent the messages
     * @return list of matching messages
     */
    List<Message> findBySender(User sender);

    /**
     * Finds all messages in a study group ordered from oldest to newest.
     *
     * @param studyGroup group whose messages should be returned
     * @return messages ordered by send time
     */
    List<Message> findByStudyGroupOrderBySentAtAsc(
            StudyGroup studyGroup
    );
}