package com.primiq.backend.model.creater;

import com.primiq.backend.model.dao.Board;
import com.primiq.backend.model.dao.KanbanContent;
import com.primiq.backend.model.dao.NoteContent;
import com.primiq.backend.model.dao.ScrumContent;
import com.primiq.backend.model.dto.BoardDto;
import com.primiq.backend.model.dto.BoardTypeDto;
import org.springframework.stereotype.Component;

@Component
public class BoardCreater implements EntityCreater<Board, BoardDto> {

    @Override
    public Board create(BoardDto request) {
        Board.BoardBuilder<?, ?> boardBuilder = Board.builder()
                .name(request.getName())
                .description(request.getDescription())
                .type(com.primiq.backend.model.dao.BoardTypeEnum.valueOf(request.getType().name()));

        switch (request.getType()) {
            case KANBAN:
                boardBuilder.boardContent(new KanbanContent());
                break;
            case SCRUM:
                boardBuilder.boardContent(new ScrumContent());
                break;
            case NOTE:
                boardBuilder.boardContent(new NoteContent());
                break;
            default:
                boardBuilder.boardContent(new KanbanContent());
                break;
        }

        return boardBuilder.build();
    }
}
