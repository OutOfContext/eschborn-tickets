package com.primiq.backend.model.dao;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.FetchType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
public class BoardLane extends BaseEntity {
    private String name;
    private String title;
    private String description;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Ticket> tickets;

    private String type;
    // Optional: Enum für Typ, falls verschiedene Lane-Typen
    // private BoardLaneTypeEnum type;

    /* private List<FilterView> Views; */
}
