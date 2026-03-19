package com.primiq.backend.model.dao;

import com.primiq.backend.model.dao.base.AbstractBoard;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Date;

@Entity
@Table(name="projects")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Project extends BaseEntity{

    private String projectname;
    private String description;
    private Collection<Board> boards;

}
