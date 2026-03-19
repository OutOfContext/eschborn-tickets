package com.primiq.backend.model.creater;

import com.primiq.backend.model.converter.BoardConverter;
import com.primiq.backend.model.dao.Project;
import com.primiq.backend.model.dto.ProjectDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
@RequiredArgsConstructor
public class ProjectCreater implements EntityCreater<Project, ProjectDto>{

    private final BoardConverter boardConverter;

    public Project create(ProjectDto projectDto) {
        Project project = new Project();
        project.setDescription(projectDto.getDescription());
        project.setBoards(boardConverter.convertAllToDao(projectDto.getBoards()));
        project.setProjectname(projectDto.getProjectname());
        return project;
    }
}
