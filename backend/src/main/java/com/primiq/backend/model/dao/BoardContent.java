package com.primiq.backend.model.dao;

import com.primiq.backend.model.dao.base.AbstractBoardContent;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Entity
@NoArgsConstructor
public class BoardContent<T> extends AbstractBoardContent {

    @jakarta.persistence.Transient
    private T content;
    @Transient
    private Collection<Ticket> tickets;

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    public Collection<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(Collection<Ticket> tickets) {
        this.tickets = tickets;
    }
}
