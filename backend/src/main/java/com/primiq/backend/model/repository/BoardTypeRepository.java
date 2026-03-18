package com.primiq.backend.model.repository;

import com.primiq.backend.model.dto.BoardType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardTypeRepository extends JpaRepository<BoardType, Long> {
}
