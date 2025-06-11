package com.base.tesis_backend.Dtos.PlatformDTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//Esto es un DTO para recibir la respuesda de TMDB cuando le preguntamos
//en que plataformas se encuentra un contenido audiovisual
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TmdbPlatformDTO {
    @JsonProperty("provider_id")
    private Long providerId;

    @JsonProperty("provider_name")
    private String providerName;

    @JsonProperty("provider_path")
    private String logoPath;
}
