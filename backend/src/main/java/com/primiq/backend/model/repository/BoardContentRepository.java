package com.primiq.backend.model.repository;

import com.primiq.backend.model.dao.BoardContent;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface BoardContentRepository extends JpaRepository<BoardContent<?>, UUID> {
    // Das Repository benötigt keine weiteren Methoden, außer du willst spezielle Queries.
    // Für Standard-CRUD reicht diese Definition.
}
