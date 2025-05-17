package com.base.tesis_backend.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "platforms")
public class Platform {

    //el id biene de TMDB, por eso no es GeneratedValue
    @Id
    private Long id;

    private String name;

    //la url del logo de la plataforma (para el front)
    private String logoPath;
}
