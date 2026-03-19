package com.primiq.backend.service;

import com.primiq.backend.model.converter.EntityConverter;
import com.primiq.backend.model.creater.EntityCreater;
import com.primiq.backend.model.updater.EntityUpdater;
import jakarta.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntityService<ID, DAO, DTO> {

  JpaRepository<DAO, ID> repository();
  EntityConverter<ID, DAO, DTO> converter();
  EntityCreater<DAO, DTO> entityCreater();
  EntityUpdater<DAO, DTO> entityUpdater();

  default DAO persistEntity(DAO entity) {
    return repository().save(entity);
  }

  default Collection<DTO> fetchAll() {
    return converter().convertAll(repository().findAll());
  }

  default Page<DTO> fetchAll(Pageable pageable) {
    return converter().convertAll(repository().findAll(pageable));
  }

  default DTO fetchOne(ID id) {
    Optional<DAO> maybeResult = repository().findById(id);
    return maybeResult.map(converter()::convert).orElse(null);
  }

  @Transactional
  default DTO create(DTO request) {
    DAO entity = entityCreater().create(request);
    DAO saved = persistEntity(entity);
    return converter().convert(saved);
  }

  @Transactional
  default DTO update(ID id, DTO request) {
    Optional<DAO> maybeResult = repository().findById(id);
    DAO savedResult = maybeResult.map(result -> {
      DAO updatedEntity = entityUpdater().update(result, request);
      return persistEntity(updatedEntity);
    }).orElseThrow(() -> new RuntimeException(String.format("Object with id [%s] not found", id)));
    return converter().convert(savedResult);
  }

  default String delete(ID id){
    repository().deleteById(id);
    return String.format("Object with id [%s] deleted successfully", id);
  }

  default void deleteAll(){
    repository().deleteAll();
  }


}
