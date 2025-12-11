package com.language_practice_server.server_demo.domain.repository;

import com.language_practice_server.server_demo.domain.model.Assignment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface AssignmentRepository {
    Assignment saveAssignment(Assignment assignment);

    Assignment updateAssignment(Assignment assignment);

    void delete(Long id);

    Optional<Assignment> findById(Long id);

    Page<Assignment> findActiveAssignmentsByOwnerId(Long ownerId, Pageable pageable);

    Page<Assignment> findAllAssignments(Pageable pageable);

    Page<Assignment> findAssignmentsByAssigneeId(Long assigneeId, Pageable pageable);
}
