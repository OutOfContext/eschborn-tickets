package com.primiq.backend.exception;

import java.time.Instant;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Map<String, Object>> handleBadRequest(IllegalArgumentException ex) {
    HttpStatus status = ex.getMessage() != null && ex.getMessage().contains("nicht gefunden")
      ? HttpStatus.NOT_FOUND
      : HttpStatus.BAD_REQUEST;

    return ResponseEntity.status(status).body(Map.of(
      "status", status.value(),
      "error", status.getReasonPhrase(),
      "message", ex.getMessage(),
      "timestamp", Instant.now().toString()
    ));
  }

  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ResponseEntity<Map<String, Object>> handleMissingRequestParam(MissingServletRequestParameterException ex) {
    return ResponseEntity.badRequest().body(Map.of(
      "status", HttpStatus.BAD_REQUEST.value(),
      "error", HttpStatus.BAD_REQUEST.getReasonPhrase(),
      "message", "Pflichtparameter fehlt: " + ex.getParameterName(),
      "timestamp", Instant.now().toString()
    ));
  }

  @ExceptionHandler(IllegalStateException.class)
  public ResponseEntity<Map<String, Object>> handleServerError(IllegalStateException ex) {
    return ResponseEntity.internalServerError().body(Map.of(
      "status", HttpStatus.INTERNAL_SERVER_ERROR.value(),
      "error", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
      "message", ex.getMessage(),
      "timestamp", Instant.now().toString()
    ));
  }
}

