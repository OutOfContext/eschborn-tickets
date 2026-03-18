package com.primiq.backend.model.creater;

import com.primiq.backend.model.dao.Board;
import com.primiq.backend.model.dao.KanbanContent;
import com.primiq.backend.model.dao.NoteContent;
import com.primiq.backend.model.dao.ScrumContent;
import com.primiq.backend.model.dto.BoardDto;
import com.primiq.backend.model.dto.BoardType;
import org.springframework.stereotype.Component;

@Component
public class BoardCreater implements EntityCreater<Board, BoardDto> {

    @Override
    public Board create(BoardDto request) {
        Board.BoardBuilder<?, ?> boardBuilder = Board.builder()
                .name(request.getName())
                .description(request.getDescription())
                .type(request.getType());

        switch (request.getType())
        {
            case BoardType.KANBAN:boardBuilder.boardContent(new KanbanContent());
            case BoardType.SCRUM: boardBuilder.boardContent(new ScrumContent());
            case BoardType.NOTE: boardBuilder.boardContent(new NoteContent());

            default: boardBuilder.boardContent(new KanbanContent());
        }

        return boardBuilder.build();
    }
}
