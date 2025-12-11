package com.language_practice_server.server_demo.domain.model;

import com.language_practice_server.server_demo.common.enums.AssignmentStatus;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

public class Assignment {
    private Long id;
    private Long ownerId;
    private Long assigneeId;
    private Long taskId;
    private LocalDate dueDate;
    private AssignmentStatus status = AssignmentStatus.NEW;
    private boolean deleted = false;

    private Long createdBy;
    private Instant createdAt;
    private Instant lastModifiedAt;
    private Long lastModifiedBy;

    public Assignment() {
    }

    public Assignment(Long id, Long ownerId, Long taskId, AssignmentStatus status) {
        this.id = id;
        this.ownerId = ownerId;
        this.taskId = taskId;
        this.status = status;
    }

    public Assignment(Long id, Long ownerId, Long assigneeId, Long taskId, AssignmentStatus status) {
        this.id = id;
        this.ownerId = ownerId;
        this.assigneeId = assigneeId;
        this.taskId = taskId;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Assignment that = (Assignment) o;
        return getId().equals(that.getId()) &&
                getOwnerId().equals(that.getOwnerId()) &&
                Objects.equals(getAssigneeId(), that.getAssigneeId()) &&
                getTaskId().equals(that.getTaskId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getOwnerId(), getAssigneeId(), getTaskId());
    }

    @Override
    public String toString() {
        return "Assignment{" +
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
