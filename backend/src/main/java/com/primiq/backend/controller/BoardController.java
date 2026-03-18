package com.primiq.backend.controller;

import com.primiq.backend.model.dao.Board;
import com.primiq.backend.model.dto.BoardDto;
import com.primiq.backend.model.dto.KanbanBoardDto;
import com.primiq.backend.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardController implements  CrudController<UUID, Board, BoardDto>{

    private final BoardService boardService;

    @Override
    public BoardService service() {
        return boardService;
    }
}
