package com.language_practice_server.server_demo.web.dto;

import com.language_practice_server.server_demo.common.enums.AssignmentStatus;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.time.LocalDate;

public class AssignmentDto {
    private Long id;
    @NotNull
    private Long ownerId;
    private Long assigneeId;
    @NotNull
    private Long taskId;
    private LocalDate dueDate;
    private AssignmentStatus status = AssignmentStatus.NEW;
    private boolean deleted = false;

    private Long createdBy;
    private Instant createdAt;
    private Instant lastModifiedAt;
    private Long lastModifiedBy;

    public AssignmentDto() {
    }

    public AssignmentDto(Long id, @NotNull Long ownerId, Long assigneeId, @NotNull Long taskId) {
        this.id = id;
        this.ownerId = ownerId;
        this.assigneeId = assigneeId;
        this.taskId = taskId;
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

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
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

    public Instant getLastModifiedAt() {
        return lastModifiedAt;
    }

    public void setLastModifiedAt(Instant lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }

    public Long getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(Long lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public String toString() {
        return "AssignmentDto{" +
                "id=" + id +
                ", ownerId=" + ownerId +
                ", assigneeId=" + assigneeId +
                ", taskId=" + taskId +
                ", createdAt=" + createdAt +
                ", dueDate=" + dueDate +
                ", status=" + status +
                ", isDeleted=" + deleted +
                '}';
    }
}
