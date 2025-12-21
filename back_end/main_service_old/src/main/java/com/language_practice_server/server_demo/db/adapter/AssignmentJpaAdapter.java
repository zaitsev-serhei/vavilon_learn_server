package com.language_practice_server.server_demo.db.adapter;

import com.language_practice_server.server_demo.db.entity.AssignmentEntity;
import com.language_practice_server.server_demo.db.repository.AssignmentRepositoryJpa;
import com.language_practice_server.server_demo.domain.model.Assignment;
import com.language_practice_server.server_demo.domain.repository.AssignmentRepository;
import com.language_practice_server.server_demo.mapper.AssignmentMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class AssignmentJpaAdapter implements AssignmentRepository {
    private final AssignmentRepositoryJpa repositoryJpa;
    private final AssignmentMapper assignmentMapper;

    public AssignmentJpaAdapter(AssignmentRepositoryJpa repositoryJpa, AssignmentMapper assignmentMapper) {
        this.repositoryJpa = repositoryJpa;
        this.assignmentMapper = assignmentMapper;
    }

    @Override
    public Assignment saveAssignment(Assignment assignment) {
        AssignmentEntity entity = assignmentMapper.toEntity(assignment);
        return assignmentMapper.toDomain(repositoryJpa.save(entity));
    }

    @Override
    public Assignment updateAssignment(Assignment assignment) {
        AssignmentEntity entity = assignmentMapper.toEntity(assignment);
        return assignmentMapper.toDomain(repositoryJpa.save(entity));
    }

    @Override
    public void delete(Long id) {
        AssignmentEntity entity = repositoryJpa.findById(id).get();
        entity.setDeleted(true);
        repositoryJpa.save(entity);
    }

    @Override
    public Optional<Assignment> findById(Long id) {
        return repositoryJpa.findById(id).map(assignmentMapper::toDomain);
    }

    @Override
    public Page<Assignment> findActiveAssignmentsByOwnerId(Long ownerId, Pageable pageable) {
        return repositoryJpa.findByOwnerIdAndDeletedFalse(ownerId, pageable).map(assignmentMapper::toDomain);
    }

    @Override
    public Page<Assignment> findAllAssignments(Pageable pageable) {
        return repositoryJpa.findAll(pageable).map(assignmentMapper::toDomain);
    }

    @Override
    public Page<Assignment> findAssignmentsByAssigneeId(Long assigneeId, Pageable pageable) {
        return repositoryJpa.findByAssigneeId(assigneeId, pageable).map(assignmentMapper::toDomain);
    }
}
