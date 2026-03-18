package com.primiq.backend.model.converter;

import java.util.List;
import org.springframework.data.domain.Page;

public interface EntityConverter<ID, DAO, DTO> {

  List<DTO> convertAll(List<DAO> all);

  Page<DTO> convertAll(Page<DAO> all);

  DTO convert(DAO dao);
}
