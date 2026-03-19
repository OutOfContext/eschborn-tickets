package com.primiq.backend.service;

import com.primiq.backend.model.converter.EntityConverter;
import com.primiq.backend.model.converter.ProjectConverter;
import com.primiq.backend.model.creater.EntityCreater;
import com.primiq.backend.model.creater.ProjectCreater;
import com.primiq.backend.model.dao.Project;
import com.primiq.backend.model.dto.ProjectDto;
import com.primiq.backend.model.repository.ProjectRepository;
import com.primiq.backend.model.updater.EntityUpdater;
import com.primiq.backend.model.updater.ProjectUpdater;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectService implements EntityService<String, Project, ProjectDto>{

    private final ProjectRepository repository;
    private final ProjectConverter converter;
    private final ProjectCreater entityCreater;
    private final ProjectUpdater entityUpdater;

    @Override
    public JpaRepository<Project, String> repository() {
        return repository;
    }

    @Override
    public EntityConverter<String, Project, ProjectDto> converter() {
        return converter;
    }

    @Override
    public EntityCreater<Project, ProjectDto> entityCreater() {
        return entityCreater;
    }

    @Override
    public EntityUpdater<Project, ProjectDto> entityUpdater() {
        return entityUpdater;
    }



}
