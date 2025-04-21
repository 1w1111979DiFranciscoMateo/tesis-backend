package com.base.tesis_backend.repositories;

import com.base.tesis_backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

//Esto es un Repository, es la conexion entre la api y la base de datos
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    //Para buscar un usuario por email.
    Optional<User> findByEmail(String email);
}
