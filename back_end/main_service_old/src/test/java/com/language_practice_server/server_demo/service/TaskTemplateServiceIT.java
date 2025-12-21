package com.language_practice_server.server_demo.service;

import com.language_practice_server.server_demo.common.enums.TaskType;
import com.language_practice_server.server_demo.domain.model.TaskTemplate;
import com.language_practice_server.server_demo.domain.repository.TaskTemplateRepository;
import java.util.List;
import java.util.Optional;
import com.language_practice_server.server_demo.web.security.config.JwtAuthenticationFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("service")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
public class TaskTemplateServiceIT {

    @Autowired
    private TaskTemplateService service;

    @Autowired
    private TaskTemplateRepository repository;

    /*
        setting up mock Authentication to allow SecurityAudit configuration for proper DB access
     */
    @BeforeEach
    public void setUp(){
        JwtAuthenticationFilter.AuthenticatedUser principal = new JwtAuthenticationFilter.AuthenticatedUser(111L, "test user");
        var auth = new UsernamePasswordAuthenticationToken(principal, null, List.of(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    public void createTemplateReturnsTemplateWithId() {
        TaskTemplate template = new TaskTemplate();
        template.setOwnerId(1L);
        template.setTaskType(TaskType.TEST);
        template.setTitle("test 1");

        TaskTemplate saved = service.createTemplate(template);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getTitle()).isEqualTo(template.getTitle());
        assertThat(saved.getOwnerId()).isEqualTo(template.getOwnerId());
    }

    @Test
    public void createTemplateThrowsErrorWhenTitleIsBlank() {
        TaskTemplate template = new TaskTemplate();
        template.setTaskType(TaskType.TEST);
        template.setOwnerId(1L);

        assertThrows(IllegalArgumentException.class, () -> service.createTemplate(template));
    }

    @Test
    public void createTemplateThrowsErrorWhenOwnerIdIsMissing() {
        TaskTemplate template = new TaskTemplate();
        template.setTaskType(TaskType.TEST);
        template.setTitle("Test 1");

        assertThrows(IllegalArgumentException.class, () -> service.createTemplate(template));
    }

    @Test
    public void updateReturnsTemplateWithUpdatedFields() {
        TaskTemplate template = new TaskTemplate(null, "Test 1", "", TaskType.TEST, 100L);

        TaskTemplate saved = service.createTemplate(template);

        assertThat(saved.getId()).isNotNull();

        saved.setTitle("Updated Title");
        saved.setDescription("Test");
        saved.setTaskType(TaskType.READING);

        TaskTemplate updated = service.updateTemplate(saved);

        assertThat(updated.getOwnerId()).isEqualTo(saved.getOwnerId());
        assertThat(updated.getTitle()).contains("Updated Title");
        assertThat(updated.getDescription()).contains("Test");
        assertThat(updated.getTaskType()).isEqualTo(saved.getTaskType());
    }

    @Test
    public void findByIdReturnsSavedTemplate() {
        TaskTemplate template = new TaskTemplate(null, "Test 1", "", TaskType.TEST, 100L);

        TaskTemplate saved = service.createTemplate(template);
        TaskTemplate result = service.findTemplateById(saved.getId());

        assertThat(result.getId()).isNotNull();
        assertThat(result.getTitle()).isEqualTo(template.getTitle());
        assertThat(result.getOwnerId()).isEqualTo(template.getOwnerId());
        assertThat(result.getTaskType()).isEqualTo(template.getTaskType());

        assertThat(result.getLastModifiedBy()).isNotNull();
        assertThat(result.getCreatedBy()).isNotNull();
    }

    @Test
    public void deleteMarksTemplateAsDeleted() {
        TaskTemplate template = new TaskTemplate(null, "Test 1", "", TaskType.TEST, 100L);

        TaskTemplate saved = service.createTemplate(template);
        service.deleteTemplate(saved.getId());

        Optional<TaskTemplate> result = repository.findTaskTemplateById(saved.getId());

        assertThat(result).isPresent();
        assertTrue(result.get().isDeleted());
    }

    @Test
    public void findByOwnerIdReturnsPageOfTemplates() {
        TaskTemplate template1 = new TaskTemplate(null, "Test 1", "", TaskType.TEST, 100L);
        TaskTemplate template2 = new TaskTemplate(null, "Test 2", "", TaskType.TEST, 100L);
        TaskTemplate template3 = new TaskTemplate(null, "Test 3", "", TaskType.TEST, 200L);

        service.createTemplate(template1);
        service.createTemplate(template2);
        service.createTemplate(template3);

        Page<TaskTemplate> result = service.findAllTaskTemplateByOwnerId(100L, PageRequest.of(0, 10));

        assertThat(result.getContent()).isNotEmpty();
        assertThat(result.getContent()).allMatch(r -> r.getOwnerId().equals(100L));
        assertThat(result.getContent().size()).isEqualTo(2);
    }
}
