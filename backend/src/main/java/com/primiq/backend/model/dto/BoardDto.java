package com.primiq.backend.model.dto;

import lombok.Data;

@Data
public class BoardDto {
 private BoardType type;
 private String name;
 private String description;
}
