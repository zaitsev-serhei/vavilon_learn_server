package com.language_practice_server.server_demo.web.dto;

import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public class TaskDto {
    private Long id;
    @NotNull
    private Long taskTemplateId;
    @NotNull
    private Long ownerId;
    //@NotNull
    private String instructions;
    private boolean deleted;

    private Instant createdAt;

    public TaskDto() {

    }

    public TaskDto(Long id, @NotNull Long taskTemplateId, @NotNull Long ownerId, String instructions) {
        this.id = id;
        this.taskTemplateId = taskTemplateId;
        this.ownerId = ownerId;
        this.instructions = instructions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTaskTemplateId() {
        return taskTemplateId;
    }

    public void setTaskTemplateId(Long taskTemplateId) {
        this.taskTemplateId = taskTemplateId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "TaskDto{" +
                "id=" + id +
                ", taskTemplateId=" + taskTemplateId +
                ", creatorId=" + ownerId +
                ", createdAt=" + createdAt +
                ", instructions='" + instructions + '\'' +
                ", isDeleted=" + deleted +
                '}';
    }
}
