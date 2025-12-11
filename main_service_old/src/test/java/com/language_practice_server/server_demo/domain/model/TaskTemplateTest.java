package com.language_practice_server.server_demo.domain.model;

import com.language_practice_server.server_demo.common.enums.TaskDifficulty;
import com.language_practice_server.server_demo.common.enums.TaskType;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("domain")
public class TaskTemplateTest {
    @Test
    public void gettersAndSetterAndEquals() {
        TaskTemplate template1 = new TaskTemplate(1L, "Test 1", "description", TaskType.TEST, TaskDifficulty.EASY, 100L);
        TaskTemplate template2 = new TaskTemplate(1L, "Test 1", "description", TaskType.TEST, TaskDifficulty.EASY, 100L);
        TaskTemplate template3 = new TaskTemplate(2L, "Test 2", "description", TaskType.TEST, TaskDifficulty.EASY, 200L);

        //getters
        assertEquals(1L, template1.getId());
        assertTrue(template1.getTitle().contains("Test 1"));
        assertTrue(template1.getDescription().contains("description"));
        assertEquals(TaskType.TEST, template1.getTaskType());
        assertEquals(TaskDifficulty.EASY, template1.getDifficulty());
        assertEquals(100L, template1.getOwnerId());



        //equals
        assertEquals(template1, template2);
        assertNotEquals(template1, template3);
        assertEquals(template1.hashCode(), template2.hashCode());
        assertNotEquals(template1.hashCode(), template3.hashCode());

        //toString()
        assertTrue(template1.toString().contains("TaskTemplate"));
    }

    @Test
    public void equalsNullSafe() {
        TaskTemplate template = new TaskTemplate();
        template.setId(1L);

        assertNotEquals(template, null);
        assertNotEquals(template, new Object());
    }
}
