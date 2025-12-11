package com.language_practice_server.server_demo.db.entity;

import com.language_practice_server.server_demo.common.enums.TaskType;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("entity")
public class TaskTemplateEntityTest {
    @Test
    public void gettersAndToString() {
        TaskTemplateEntity template1 = new TaskTemplateEntity(1L, "Test 1", TaskType.TEST, 1L);

        //getters
        assertEquals(1L, template1.getId());
        assertTrue(template1.getTitle().contains("Test 1"));
        assertEquals(TaskType.TEST, template1.getTaskType());
        assertEquals(1L, template1.getOwnerId());

        //toString
        assertTrue(template1.toString().contains("TaskTemplateEntity"));
    }

    @Test
    public void equalsNullSafe() {
        TaskTemplateEntity template = new TaskTemplateEntity();
        template.setId(1L);

        assertNotEquals(template, null);
        assertNotEquals(template, new Object());
    }
}
