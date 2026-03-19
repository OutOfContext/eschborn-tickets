package com.primiq.backend.model.converter;

import com.primiq.backend.model.dao.Project;
import com.primiq.backend.model.dto.ProjectDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ProjectConverter implements EntityConverter<String, Project, ProjectDto>{


    private final BoardConverter boardConverter;

    @Override
    public Collection<ProjectDto> convertAllToDto(Collection<Project> all) {
        return List.of();
    }

    @Override
    public Collection<Project> convertAllToDao(Collection<ProjectDto> all) {
        return List.of();
    }

    @Override
    public Page<ProjectDto> convertAllToDto(Page<Project> all) {
        return null;
    }

    @Override
    public Page<Project> convertAllToDao(Page<ProjectDto> all) {
        return null;
    }

    @Override
    public ProjectDto convertToDto(Project project) {
        ProjectDto projectDto = new ProjectDto();
        projectDto.setDescription(project.getDescription());
        projectDto.setProjectname(project.getProjectname());
        projectDto.setBoards(boardConverter.convertAllToDto(project.getBoards()));
        return projectDto;
    }

    @Override
    public Project convertToDao(ProjectDto dto) {
        Project project = new Project();
        project.setDescription(dto.getDescription());
        project.setProjectname(dto.getProjectname());
        project.setBoards(boardConverter.convertAllToDao(dto.getBoards()));
        return project;

    }
}
