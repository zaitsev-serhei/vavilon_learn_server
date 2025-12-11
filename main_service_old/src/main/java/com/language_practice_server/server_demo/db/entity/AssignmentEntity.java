package com.language_practice_server.server_demo.db.entity;

import com.language_practice_server.server_demo.audit.BaseAuditableEntity;
import com.language_practice_server.server_demo.common.enums.AssignmentStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "assignments")
public class AssignmentEntity extends BaseAuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "owner_id")
    private Long ownerId;
    @Column(name = "assignee_id")
    private Long assigneeId;
    @Column(name = "task_id")
    private Long taskId;
    @Column(name = "due_date")
    private LocalDate dueDate;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private AssignmentStatus status = AssignmentStatus.NEW;
    @Column(name = "deleted")
    private boolean deleted = false;

    public AssignmentEntity() {
    }

    public AssignmentEntity(Long id, Long ownerId, Long assigneeId, Long taskId, AssignmentStatus status) {
        this.id = id;
        this.ownerId = ownerId;
        this.assigneeId = assigneeId;
        this.taskId = taskId;
        this.status = status;
    }

    public AssignmentEntity(Long id, Long ownerId, Long assigneeId, Long taskId, LocalDate dueDate, AssignmentStatus status) {
        this.id = id;
        this.ownerId = ownerId;
        this.assigneeId = assigneeId;
        this.taskId = taskId;
        this.dueDate = dueDate;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Long getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(Long assigneeId) {
        this.assigneeId = assigneeId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public AssignmentStatus getStatus() {
        return status;
    }

    public void setStatus(AssignmentStatus status) {
        this.status = status;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "AssignmentEntity{" +
                "id=" + id +
                ", ownerId=" + ownerId +
                ", assigneeId=" + assigneeId +
                ", taskId=" + taskId +
                ", dueDate=" + dueDate +
                ", status=" + status +
                ", isDeleted=" + deleted +
                '}';
    }
}
