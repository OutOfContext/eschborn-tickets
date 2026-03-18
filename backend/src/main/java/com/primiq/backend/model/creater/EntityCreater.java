package com.primiq.backend.model.creater;

public interface EntityCreater<DAO, DTO> {

  DAO create(DTO request);
}
