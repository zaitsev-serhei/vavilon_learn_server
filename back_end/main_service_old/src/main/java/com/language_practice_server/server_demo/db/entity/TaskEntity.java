package com.language_practice_server.server_demo.db.entity;

import com.language_practice_server.server_demo.audit.BaseAuditableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name = "tasks")
public class TaskEntity extends BaseAuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "template_id", nullable = false)
    private Long taskTemplateId;
    @Column(name = "creator_id", nullable = false)
    private Long ownerId;
    @Lob
    private String instructions;
    @Column(name = "deleted")
    private boolean deleted = false;

    public TaskEntity() {
    }

    public TaskEntity(Long id, Long taskTemplateId, Long ownerId) {
        this.id = id;
        this.taskTemplateId = taskTemplateId;
        this.ownerId = ownerId;
    }

    public TaskEntity(Long id, Long taskTemplateId, Long ownerId, String instructions) {
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

    @Override
    public String toString() {
        return "TaskEntity{" +
                "id=" + id +
                ", taskTemplateId=" + taskTemplateId +
                ", creatorId=" + ownerId +
                '}';
    }
}
