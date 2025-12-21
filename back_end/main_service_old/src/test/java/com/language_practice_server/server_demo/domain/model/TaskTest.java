package com.language_practice_server.server_demo.domain.model;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("domain")
public class TaskTest {
    @Test
    public void gettersAndSetterAndEquals() {
        Task task1 = new Task(1L, 100L, 200L, "instr");
        Task task2 = new Task(1L, 100L, 200L, "instr");
        Task task3 = new Task(2L, 200L, 300L, "instr");

        //getters
        assertEquals(1L, task1.getId());
        assertEquals(100L, task1.getTaskTemplateId());
        assertEquals(200L, task1.getOwnerId());
        assertTrue(task1.getInstructions().contains("instr"));

        //equals
        assertEquals(task1, task2);
        assertNotEquals(task1, task3);
        assertEquals(task1.hashCode(), task2.hashCode());
        assertNotEquals(task1.hashCode(), task3.hashCode());

        //toString()
        assertTrue(task1.toString().contains("Task"));
    }

    @Test
    public void equalsNullSafe() {
        Task task = new Task();
        task.setId(1L);

        assertNotEquals(task, null);
        assertNotEquals(task, new Object());
    }
}
