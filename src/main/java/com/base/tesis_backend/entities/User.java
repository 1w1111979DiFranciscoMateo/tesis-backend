package com.base.tesis_backend.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

@Entity //mapea la java class a la base de datos
@Data //crea los getters y setters (tengo que agregar la version en el pom y agregar la tag version en annotationProcessorPaths y en exclude)
@Table(name = "users") //nombre de la tabla en la base de datos
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String email;

    private String password;

    private boolean isPremium;

    private boolean publicProfile;

    private LocalDateTime creationDate;

    //Metodos requeridos por UserDetails para el tema de la seguirdad con JWT
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList(); //mas adelante puedo agregar roles aca
    }

    //usamos el email como "username" para el login (nos fuerza Spring Security a hacer este override)
    @Override
    public String getUsername(){
        return email;
    }

    //estos metodos son para que no me de error 403 en la llamada
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
