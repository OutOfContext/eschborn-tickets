package com.primiq.backend.model.dao;

import com.primiq.backend.model.dao.base.AbstractBudget;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "budgets")
@Data
public class Budget extends AbstractBudget {
    
    private String description;
    
}
