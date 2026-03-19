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

import java.util.*;

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
        project.setProjectname("Test Project");

        projectDto = new ProjectDto();
        projectDto.setProjectname("Test Project");
    }

    @Test
    void fetchAll_shouldReturnListOfProjectDtos() {
        // Given
        List<Project> projects = Arrays.asList(project);
        List<ProjectDto> expectedDtos = Arrays.asList(projectDto);
        
        when(repository.findAll()).thenReturn(projects);
        when(converter.convertAllToDto(projects)).thenReturn(expectedDtos);

        // When
        Collection<ProjectDto> result = projectService.fetchAll();

        // Then
        assertThat(result).isEqualTo(expectedDtos);
        verify(repository).findAll();
        verify(converter).convertAllToDto(projects);
    }

    @Test
    void fetchAllPaged_shouldReturnPagedProjectDtos() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        List<Project> projects = Arrays.asList(project);
        Page<Project> projectPage = new PageImpl<>(projects, pageable, projects.size());
        Page<ProjectDto> expectedPage = new PageImpl<>(Arrays.asList(projectDto), pageable, 1);
        
        when(repository.findAll(pageable)).thenReturn(projectPage);
        when(converter.convertAllToDto(projectPage)).thenReturn(expectedPage);

        // When
        Page<ProjectDto> result = projectService.fetchAll(pageable);

        // Then
        assertThat(result).isEqualTo(expectedPage);
        verify(repository).findAll(pageable);
        verify(converter).convertAllToDto(projectPage);
    }

    @Test
    void fetchOne_shouldReturnProjectDto_whenProjectExists() {
        // Given
        when(repository.findById(PROJECT_ID)).thenReturn(Optional.of(project));
        when(converter.convertToDto(project)).thenReturn(projectDto);

        // When
        ProjectDto result = projectService.fetchOne(PROJECT_ID);

        // Then
        assertThat(result).isEqualTo(projectDto);
        verify(repository).findById(PROJECT_ID);
        verify(converter).convertToDto(project);
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
        verify(converter, never()).convertToDto(any());
    }

    @Test
    void create_shouldReturnCreatedProjectDto() {
        // Given
        Project createdProject = new Project();
        createdProject.setProjectname("New Project");

        ProjectDto createdDto = new ProjectDto();
        createdDto.setProjectname("New Project");

        when(entityCreater.create(projectDto)).thenReturn(project);
        when(repository.save(project)).thenReturn(createdProject);
        when(converter.convertToDto(createdProject)).thenReturn(createdDto);

        // When
        ProjectDto result = projectService.create(projectDto);

        // Then
        assertThat(result).isEqualTo(createdDto);
        verify(entityCreater).create(projectDto);
        verify(repository).save(project);
        verify(converter).convertToDto(createdProject);
    }

    @Test
    void update_shouldReturnUpdatedProjectDto_whenProjectExists() {
        // Given
        ProjectDto updateRequest = new ProjectDto();
        updateRequest.setProjectname("Updated Project");

        Project updatedProject = new Project();
        updatedProject.setProjectname("Updated Project");
        updatedProject.setCreatedAt(project.getCreatedAt());

        ProjectDto expectedDto = new ProjectDto();
        expectedDto.setProjectname("Updated Project");

        when(repository.findById(PROJECT_ID)).thenReturn(Optional.of(project));
        when(entityUpdater.update(project, updateRequest)).thenReturn(updatedProject);
        when(repository.save(updatedProject)).thenReturn(updatedProject);
        when(converter.convertToDto(updatedProject)).thenReturn(expectedDto);

        // When
        ProjectDto result = projectService.update(PROJECT_ID, updateRequest);

        // Then
        assertThat(result).isEqualTo(expectedDto);
        verify(repository).findById(PROJECT_ID);
        verify(entityUpdater).update(project, updateRequest);
        verify(repository).save(updatedProject);
        verify(converter).convertToDto(updatedProject);
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
