package com.primiq.backend.exception;

import java.time.Instant;
import lombok.Data;

@Data
public class ApiError {
  private String errorCode;
  private String message;
  private Instant timestamp = Instant.now();

  public ApiError(String errorCode, String message) {
    this.errorCode = errorCode;
    this.message = message;
  }
}
