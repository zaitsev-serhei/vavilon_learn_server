package com.language_practice_server.server_demo.service;

import com.language_practice_server.server_demo.domain.model.Assignment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AssignmentService {
    Assignment createAssignment(Assignment assignment);

    Assignment updateAssignment(Assignment assignment);

    void delete(Long assignmentId);

    Assignment findById(Long assignmentId);

    Page<Assignment> findActiveAssignmentByOwnerId(Long ownerId, Pageable pageable);

    Page<Assignment> findActiveAssignmentByAssigneeId(Long assigneeId, Pageable pageable);

    Page<Assignment> findAllAssignments(Pageable pageable);
}
