package com.primiq.backend.model.dao;

import com.primiq.backend.model.dao.base.AbstractBoardContent;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class BoardContent<T> extends AbstractBoardContent {

    private T content;

    public BoardContent(T content)  {
        this.content = content;
    }


}
