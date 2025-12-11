package com.language_practice_server.server_demo.domain.model;

import com.language_practice_server.server_demo.common.enums.TaskDifficulty;
import com.language_practice_server.server_demo.common.enums.TaskType;

import java.time.Instant;
import java.util.Objects;

public class TaskTemplate {
    private Long id;
    private String title;
    private String description;
    private TaskType taskType;
    private TaskDifficulty difficulty;
    private String languageFrom;
    private String languageTo;
    private String instructions;
    private Long ownerId;
    private boolean deleted;

    private Instant createdAt;
    private Instant lastUpdatedAt;
    private Long createdBy;
    private Long lastModifiedBy;

    public TaskTemplate() {
    }

    public TaskTemplate(Long id, String title, String description, TaskType taskType,
                        TaskDifficulty difficulty, Long ownerId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.taskType = taskType;
        this.difficulty = difficulty;
        this.ownerId = ownerId;
    }

    public TaskTemplate(Long id, String title, String description, TaskType taskType, Long ownerId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.taskType = taskType;
        this.ownerId = ownerId;
    }

    public TaskTemplate(Long id, String title, String description, TaskType taskType,
                        TaskDifficulty difficulty, String languageFrom, String languageTo,
                        String instructions, Long ownerId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.taskType = taskType;
        this.difficulty = difficulty;
        this.languageFrom = languageFrom;
        this.languageTo = languageTo;
        this.instructions = instructions;
        this.ownerId = ownerId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    public TaskDifficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(TaskDifficulty difficulty) {
        this.difficulty = difficulty;
    }

    public String getLanguageFrom() {
        return languageFrom;
    }

    public void setLanguageFrom(String languageFrom) {
        this.languageFrom = languageFrom;
    }

    public String getLanguageTo() {
        return languageTo;
    }

    public void setLanguageTo(String languageTo) {
        this.languageTo = languageTo;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
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

    public Instant getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    public void setLastUpdatedAt(Instant lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Long getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(Long lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskTemplate that = (TaskTemplate) o;
        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "TaskTemplate{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", taskType=" + taskType +
                ", difficulty=" + difficulty +
                ", languageFrom='" + languageFrom + '\'' +
                ", languageTo='" + languageTo + '\'' +
                ", creatorId=" + ownerId +
                ", createdAt=" + createdAt +
                '}';
    }
}
