package com.primiq.backend.controller;

import com.primiq.backend.model.dao.Project;
import com.primiq.backend.model.dto.ProjectDto;
import com.primiq.backend.service.EntityService;
import com.primiq.backend.service.ProjectService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RequestMapping("/api/projects")
@RestController
public class ProjectsController implements CrudController<String, Project, ProjectDto> {


    private final ProjectService projectService;

    public ProjectsController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Override
    public EntityService<String, Project, ProjectDto> service() {
        return this.projectService;
    }
    //public ProjectDto createProject(@RequestBody ProjectDto project) {
    //    return "Api Called";
    //}

}
