package com.language_practice_server.server_demo.db.entity;

import com.language_practice_server.server_demo.audit.BaseAuditableEntity;
import com.language_practice_server.server_demo.common.enums.TaskDifficulty;
import com.language_practice_server.server_demo.common.enums.TaskType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name = "task_templates")
public class TaskTemplateEntity extends BaseAuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title", nullable = false)
    private String title;
    private String description;
    @Enumerated(EnumType.STRING)
    @Column(name = "task_type", nullable = false)
    private TaskType taskType;
    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty")
    private TaskDifficulty difficulty;
    @Column(name = "language_from")
    private String languageFrom;
    @Column(name = "language_to")
    private String languageTo;
    @Lob
    private String instructions;
    @Column(name = "creator_id", nullable = false)
    private Long ownerId;
    @Column(name = "deleted")
    private boolean deleted = false;

    public TaskTemplateEntity() {
    }

    public TaskTemplateEntity(Long id, String title, String description, TaskType taskType,
                              TaskDifficulty difficulty, Long ownerId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.taskType = taskType;
        this.difficulty = difficulty;
        this.ownerId = ownerId;
    }

    public TaskTemplateEntity(Long id, String title, TaskType taskType, Long ownerId) {
        this.id = id;
        this.title = title;
        this.taskType = taskType;
        this.ownerId = ownerId;
    }

    public TaskTemplateEntity(Long id, String title, String description, TaskType taskType,
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

    @Override
    public String toString() {
        return "TaskTemplateEntity{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", taskType=" + taskType +
                ", difficulty=" + difficulty +
                ", creatorId=" + ownerId +
                ", createdAt=" + super.getCreatedAt() +
                '}';
    }
}
