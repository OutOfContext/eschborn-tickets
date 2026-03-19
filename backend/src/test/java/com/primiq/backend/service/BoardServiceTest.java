package com.primiq.backend.service;

import com.primiq.backend.model.converter.BoardConverter;
import com.primiq.backend.model.creater.BoardCreater;
import com.primiq.backend.model.dao.Board;
import com.primiq.backend.model.repository.BoardRepository;
import com.primiq.backend.model.updater.BoardUpdater;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class BoardServiceTest {

    private BoardRepository repository;
    private BoardConverter converter;
    private BoardCreater creater;
    private BoardUpdater updater;
    private BoardService boardService;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(BoardRepository.class);
        converter = Mockito.mock(BoardConverter.class);
        creater = Mockito.mock(BoardCreater.class);
        updater = Mockito.mock(BoardUpdater.class);
        boardService = new BoardService(repository, converter, creater, updater);
    }

    @Test
    void repository_returnsInjectedRepository() {
        JpaRepository<Board, UUID> result = boardService.repository();
        assertSame(repository, result);
    }

    @Test
    void converter_returnsInjectedConverter() {
        assertSame(converter, boardService.converter());
    }

    @Test
    void entityCreater_returnsInjectedCreater() {
        assertSame(creater, boardService.entityCreater());
    }

    @Test
    void entityUpdater_returnsInjectedUpdater() {
        assertSame(updater, boardService.entityUpdater());
    }
}

