package edu.ncsu.acm.studygroup.repository;

import edu.ncsu.acm.studygroup.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByUser_UserId(Long userId);
    Optional<Enrollment> findByUser_UserIdAndCourse_CourseId(Long userId, Long courseId);
    Optional<Enrollment> findByUser_UserIdAndSection_SectionId(Long userId, Long sectionId);
}