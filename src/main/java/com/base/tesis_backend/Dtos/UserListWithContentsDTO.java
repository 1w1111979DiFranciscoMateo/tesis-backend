package com.base.tesis_backend.Dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserListWithContentsDTO {
    private Long id;
    private String name;
    private String description;
    @JsonProperty("isPublic")
    private boolean isPublic;
    private LocalDateTime creationDate;
    private List<AudioVisualContentDTO> contents;
}
