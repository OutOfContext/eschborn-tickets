package com.primiq.backend.model.converter;

import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Page;

public interface EntityConverter<ID, DAO, DTO> {

  Collection<DTO> convertAllToDto(List<DAO> all);
  Collection<DAO> convertAllToDao(List<DTO> all);

  Page<DTO> convertAllToDto(Page<DAO> all);
  Page<DAO> convertAllToDao(Page<DTO> all);

  DAO convertToDao(DTO dao);
  DTO convertToDto(DAO dao);
}
