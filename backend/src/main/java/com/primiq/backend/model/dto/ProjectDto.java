package com.primiq.backend.model.dto;

import lombok.Data;

import java.util.Collection;
import java.util.Date;


@Data
public class ProjectDto {

    private String projectname;
    private String description;
    private Collection<BoardDto> boards;


}
