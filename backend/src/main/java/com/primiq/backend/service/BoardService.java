package com.primiq.backend.service;

import com.primiq.backend.model.converter.BoardConverter;
import com.primiq.backend.model.converter.EntityConverter;
import com.primiq.backend.model.creater.BoardCreater;
import com.primiq.backend.model.creater.EntityCreater;
import com.primiq.backend.model.dao.Board;
import com.primiq.backend.model.dto.BoardDto;
import com.primiq.backend.model.repository.BoardRepository;
import com.primiq.backend.model.updater.BoardUpdater;
import com.primiq.backend.model.updater.EntityUpdater;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import java.util.UUID;


@Component
@RequiredArgsConstructor
public class BoardService implements EntityService<UUID, Board, BoardDto> {

    private final BoardRepository repository;
    private final BoardConverter converter;
    private final BoardCreater creater;
    private final BoardUpdater updater;

    @Override
    public JpaRepository<Board, UUID> repository() {
        return repository;
    }

    @Override
    public EntityConverter<UUID, Board, BoardDto> converter() {
        return converter;
    }

    @Override
    public EntityCreater<Board, BoardDto> entityCreater() {
        return creater;
    }

    @Override
    public EntityUpdater<Board, BoardDto> entityUpdater() {
        return updater;
    }

}
