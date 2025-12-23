package com.language_practice_server.server_demo.db.entity;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("entity")
public class TaskEntityTest {
    @Test
    public void gettersAndToString() {
        TaskEntity task1 = new TaskEntity(1L, 1L, 100L, "instr");

        //getters
        assertEquals(1L, task1.getId());
        assertEquals(1L, task1.getTaskTemplateId());
        assertEquals(100L, task1.getOwnerId());
        assertTrue(task1.getInstructions().contains("instr"));

        //toString
        assertTrue(task1.toString().contains("TaskEntity"));

    }

    @Test
    public void equalsNullSafe() {
        TaskEntity task = new TaskEntity();
        task.setId(1L);

        assertNotEquals(task, null);
        assertNotEquals(task, new Object());
    }
}
