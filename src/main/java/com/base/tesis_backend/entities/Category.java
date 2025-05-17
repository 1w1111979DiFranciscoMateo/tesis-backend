package com.base.tesis_backend.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "categories")
public class Category {

    //el id biene de TMDB, por eso no es GeneratedValue
    @Id
    private Long id;

    private String name;
}
