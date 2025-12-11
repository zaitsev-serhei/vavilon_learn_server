package com.language_practice_server.server_demo.service.impl;

import com.language_practice_server.server_demo.domain.model.Assignment;
import com.language_practice_server.server_demo.domain.repository.AssignmentRepository;
import com.language_practice_server.server_demo.service.AssignmentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AssignmentServiceImpl implements AssignmentService {
    private final AssignmentRepository repository;

    public AssignmentServiceImpl(AssignmentRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public Assignment createAssignment(Assignment assignment) {
        if (assignment.getOwnerId()==null|| assignment.getOwnerId()<=0) {
            throw new IllegalArgumentException("OwnerId can not be empty");
        }
        return repository.saveAssignment(assignment);
    }

    @Override
    @Transactional
    public Assignment updateAssignment(Assignment assignment) {
        if(assignment.getId()==null||assignment.getId()<=0){
            throw new IllegalArgumentException("Id is missing for an update");
        }
        return repository.saveAssignment(assignment);
    }

    @Override
    @Transactional
    public void delete(Long assignmentId) {
        if (assignmentId == null||assignmentId<=0){
            throw new IllegalArgumentException("Id is missing for deleting");
        }
        repository.delete(assignmentId);
    }

    @Override
    @Transactional(readOnly = true)
    public Assignment findById(Long assignmentId) {
        return repository.findById(assignmentId).orElseThrow();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Assignment> findActiveAssignmentByOwnerId(Long ownerId, Pageable pageable) {
        return repository.findActiveAssignmentsByOwnerId(ownerId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Assignment> findActiveAssignmentByAssigneeId(Long assigneeId, Pageable pageable) {
        return repository.findAssignmentsByAssigneeId(assigneeId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Assignment> findAllAssignments(Pageable pageable) {
        return repository.findAllAssignments(pageable);
    }
}
