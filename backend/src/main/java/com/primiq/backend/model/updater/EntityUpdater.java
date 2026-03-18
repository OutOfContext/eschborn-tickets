package com.primiq.backend.model.updater;

public interface EntityUpdater<DAO, DTO> {

  DAO update(DAO result, DTO request);
}
