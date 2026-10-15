package edu.ncsu.acm.studygroup.repository;

import edu.ncsu.acm.studygroup.entity.GroupMember;
import edu.ncsu.acm.studygroup.entity.StudyGroup;
import edu.ncsu.acm.studygroup.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Provides database access operations for GroupMember entities.
 */
public interface GroupMemberRepository
        extends JpaRepository<GroupMember, Long> {

    /**
     * Finds all group memberships belonging to a specific user.
     *
     * @param user user whose memberships should be returned
     * @return list of matching memberships
     */
    List<GroupMember> findByUser(User user);

    /**
     * Finds all memberships belonging to a specific study group.
     *
     * @param studyGroup study group whose members should be returned
     * @return list of matching memberships
     */
    List<GroupMember> findByStudyGroup(StudyGroup studyGroup);

    /**
     * Finds a membership connecting a specific user and study group.
     *
     * @param user user to search for
     * @param studyGroup study group to search for
     * @return matching membership, if one exists
     */
    Optional<GroupMember> findByUserAndStudyGroup(
            User user,
            StudyGroup studyGroup
    );
}