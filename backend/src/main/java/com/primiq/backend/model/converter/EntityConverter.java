package com.primiq.backend.model.converter;

import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Page;

public interface EntityConverter<ID, DAO, DTO> {

  Collection<DTO> convertAllToDto(Collection<DAO> all);
  Collection<DAO> convertAllToDao(Collection<DTO> all);

  Page<DTO> convertAllToDto(Page<DAO> all);
  Page<DAO> convertAllToDao(Page<DTO> all);



  DTO convertToDto(DAO dao);
  DAO convertToDao(DTO dao);
}
