package com.primiq.backend.unit;

import com.primiq.backend.model.converter.ProjectConverter;
import com.primiq.backend.model.creater.ProjectCreater;
import com.primiq.backend.model.dao.Project;
import com.primiq.backend.model.dto.ProjectDto;
import com.primiq.backend.model.repository.ProjectRepository;
import com.primiq.backend.model.updater.ProjectUpdater;
import com.primiq.backend.service.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {

    @Mock
    private ProjectRepository repository;

    @Mock
    private ProjectConverter converter;

    @Mock
    private ProjectCreater entityCreater;

    @Mock
    private ProjectUpdater entityUpdater;

    @InjectMocks
    private ProjectService projectService;

    private Project project;
    private ProjectDto projectDto;
    private final String PROJECT_ID = "test-project-id";

    @BeforeEach
    void setUp() {
        // Testdaten vorbereiten
        Date now = new Date();
        
        project = new Project();
        project.setId(PROJECT_ID);
        project.setProjectname("Test Project");
        project.setCreator("testuser");
        project.setCreatedAt(now);

        projectDto = new ProjectDto();
        projectDto.setProjectname("Test Project");
        projectDto.setCreator("testuser");
        projectDto.setCreatedAt(now);
    }

    @Test
    void fetchAll_shouldReturnListOfProjectDtos() {
        // Given
        List<Project> projects = Arrays.asList(project);
        List<ProjectDto> expectedDtos = Arrays.asList(projectDto);
        
        when(repository.findAll()).thenReturn(projects);
        when(converter.convertAll(projects)).thenReturn(expectedDtos);

        // When
        List<ProjectDto> result = projectService.fetchAll();

        // Then
        assertThat(result).isEqualTo(expectedDtos);
        verify(repository).findAll();
        verify(converter).convertAll(projects);
    }

    @Test
    void fetchAllPaged_shouldReturnPagedProjectDtos() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        List<Project> projects = Arrays.asList(project);
        Page<Project> projectPage = new PageImpl<>(projects, pageable, projects.size());
        Page<ProjectDto> expectedPage = new PageImpl<>(Arrays.asList(projectDto), pageable, 1);
        
        when(repository.findAll(pageable)).thenReturn(projectPage);
        when(converter.convertAll(projectPage)).thenReturn(expectedPage);

        // When
        Page<ProjectDto> result = projectService.fetchAll(pageable);

        // Then
        assertThat(result).isEqualTo(expectedPage);
        verify(repository).findAll(pageable);
        verify(converter).convertAll(projectPage);
    }

    @Test
    void fetchOne_shouldReturnProjectDto_whenProjectExists() {
        // Given
        when(repository.findById(PROJECT_ID)).thenReturn(Optional.of(project));
        when(converter.convert(project)).thenReturn(projectDto);

        // When
        ProjectDto result = projectService.fetchOne(PROJECT_ID);

        // Then
        assertThat(result).isEqualTo(projectDto);
        verify(repository).findById(PROJECT_ID);
        verify(converter).convert(project);
    }

    @Test
    void fetchOne_shouldReturnNull_whenProjectDoesNotExist() {
        // Given
        when(repository.findById(PROJECT_ID)).thenReturn(Optional.empty());

        // When
        ProjectDto result = projectService.fetchOne(PROJECT_ID);

        // Then
        assertThat(result).isNull();
        verify(repository).findById(PROJECT_ID);
        verify(converter, never()).convert(any());
    }

    @Test
    void create_shouldReturnCreatedProjectDto() {
        // Given
        Project createdProject = new Project();
        createdProject.setId(PROJECT_ID);
        createdProject.setProjectname("New Project");
        createdProject.setCreator("creator");
        createdProject.setCreatedAt(new Date());

        ProjectDto createdDto = new ProjectDto();
        createdDto.setProjectname("New Project");
        createdDto.setCreator("creator");
        createdDto.setCreatedAt(new Date());

        when(entityCreater.create(projectDto)).thenReturn(project);
        when(repository.save(project)).thenReturn(createdProject);
        when(converter.convert(createdProject)).thenReturn(createdDto);

        // When
        ProjectDto result = projectService.create(projectDto);

        // Then
        assertThat(result).isEqualTo(createdDto);
        verify(entityCreater).create(projectDto);
        verify(repository).save(project);
        verify(converter).convert(createdProject);
    }

    @Test
    void update_shouldReturnUpdatedProjectDto_whenProjectExists() {
        // Given
        ProjectDto updateRequest = new ProjectDto();
        updateRequest.setProjectname("Updated Project");
        updateRequest.setCreator("updater");

        Project updatedProject = new Project();
        updatedProject.setId(PROJECT_ID);
        updatedProject.setProjectname("Updated Project");
        updatedProject.setCreator("updater");
        updatedProject.setCreatedAt(project.getCreatedAt());

        ProjectDto expectedDto = new ProjectDto();
        expectedDto.setProjectname("Updated Project");
        expectedDto.setCreator("updater");
        expectedDto.setCreatedAt(project.getCreatedAt());

        when(repository.findById(PROJECT_ID)).thenReturn(Optional.of(project));
        when(entityUpdater.update(project, updateRequest)).thenReturn(updatedProject);
        when(repository.save(updatedProject)).thenReturn(updatedProject);
        when(converter.convert(updatedProject)).thenReturn(expectedDto);

        // When
        ProjectDto result = projectService.update(PROJECT_ID, updateRequest);

        // Then
        assertThat(result).isEqualTo(expectedDto);
        verify(repository).findById(PROJECT_ID);
        verify(entityUpdater).update(project, updateRequest);
        verify(repository).save(updatedProject);
        verify(converter).convert(updatedProject);
    }

    @Test
    void update_shouldThrowException_whenProjectDoesNotExist() {
        // Given
        ProjectDto updateRequest = new ProjectDto();
        updateRequest.setProjectname("Updated Project");

        when(repository.findById(PROJECT_ID)).thenReturn(Optional.empty());

        // When & Then
        try {
            projectService.update(PROJECT_ID, updateRequest);
        } catch (RuntimeException e) {
            assertThat(e.getMessage()).contains("Object with id [" + PROJECT_ID + "] not found");
        }

        verify(repository).findById(PROJECT_ID);
        verify(entityUpdater, never()).update(any(), any());
        verify(repository, never()).save(any());
    }

    @Test
    void delete_shouldReturnSuccessMessage() {
        // When
        String result = projectService.delete(PROJECT_ID);

        // Then
        assertThat(result).isEqualTo("Object with id [" + PROJECT_ID + "] deleted successfully");
        verify(repository).deleteById(PROJECT_ID);
    }
}
