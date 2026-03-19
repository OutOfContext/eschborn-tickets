package com.primiq.backend.controller;

import com.primiq.backend.model.dto.Message;
import com.primiq.backend.service.EntityService;

import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface CrudController<ID, DAO, DTO>{

  EntityService<ID, DAO, DTO> service();

  @GetMapping("/all")
  default Message<Collection<DTO>> fetchAll() {
    return Message.of(service().fetchAll());
  }

  @GetMapping("/all/paged")
  default Message<Page<DTO>> fetchAllPaged(Pageable pageable) {
    return Message.of(service().fetchAll(pageable));
  }

  @GetMapping("/{id}")
  default Message<DTO> fetchOne(@PathVariable ID id) {
    return Message.of(service().fetchOne(id));
  }

  @PostMapping("/create")
  default Message<DTO> create(@RequestBody DTO request) {
    return Message.of(service().create(request));
  }

  @PutMapping("/update/{id}")
  default Message<DTO> update(@PathVariable ID id, @RequestBody DTO request) {
    return Message.of(service().update(id, request));
  }

  @DeleteMapping("/delete/{id}")
  default Message<String> delete(@PathVariable ID id) {
    return Message.of(service().delete(id));
  }
}
