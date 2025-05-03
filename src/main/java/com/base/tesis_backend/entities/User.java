package com.base.tesis_backend.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity //mapea la java class a la base de datos
@Data //crea los getters y setters (tengo que agregar la version en el pom y agregar la tag version en annotationProcessorPaths y en exclude)
@Table(name = "users") //nombre de la tabla en la base de datos
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String email;

    private String password;

    private boolean isPremium;

    private boolean publicProfile;

    private LocalDateTime creationDate;

}
