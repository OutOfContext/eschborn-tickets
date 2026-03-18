package com.primiq.backend.model.repository;

import com.primiq.backend.model.dao.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BoardRepository extends JpaRepository<Board, UUID> {
}
