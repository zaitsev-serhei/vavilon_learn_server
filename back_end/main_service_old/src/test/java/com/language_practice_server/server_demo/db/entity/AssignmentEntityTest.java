package com.language_practice_server.server_demo.db.entity;

import com.language_practice_server.server_demo.common.enums.AssignmentStatus;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("entity")
public class AssignmentEntityTest {
    @Test
    public void gettersAndToString() {
        AssignmentEntity assignment1 = new AssignmentEntity(1L, 100L, 101L, 123L, AssignmentStatus.NEW);

        //getters
        assertEquals(1L, assignment1.getId());
        assertEquals(100L, assignment1.getOwnerId());
        assertEquals(101L, assignment1.getAssigneeId());
        assertEquals(123L, assignment1.getTaskId());
        assertEquals(AssignmentStatus.NEW, assignment1.getStatus());
        assertFalse(assignment1.isDeleted());

        //toString()
        assertTrue(assignment1.toString().contains("AssignmentEntity"));
    }
}
