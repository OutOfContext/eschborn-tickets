package com.primiq.backend.model.updater;

import com.primiq.backend.model.dao.Project;
import com.primiq.backend.model.dto.ProjectDto;
import org.springframework.stereotype.Component;

@Component
public class ProjectUpdater implements EntityUpdater<Project, ProjectDto>{
    @Override
    public Project update(Project result, ProjectDto request) {
        // Aktualisiere nur die Felder, die geändert werden dürfen
        // id und createdAt sollten typischerweise nicht geändert werden

        if (request.getProjectname() != null) {
            result.setProjectname(request.getProjectname());
        }



        // createdAt wird nicht aktualisiert - es ist ein Timestamp

        return result;
    }
}
