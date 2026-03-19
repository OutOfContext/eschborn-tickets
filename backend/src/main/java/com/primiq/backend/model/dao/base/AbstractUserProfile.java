package com.primiq.backend.model.dao.base;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class AbstractUserProfile {

  private String email;

}
