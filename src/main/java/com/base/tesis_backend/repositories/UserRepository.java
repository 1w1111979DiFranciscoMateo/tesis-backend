package com.base.tesis_backend.repositories;

import com.base.tesis_backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

//Esto es un Repository, es la conexion entre la api y la base de datos
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    //Para buscar un usuario por email.
    Optional<User> findByEmail(String email);

    //metodo para las estadisticas de Admin (usuarios activos)
    @Query("SELECT COUNT(DISTINCT u.id) FROM User u LEFT JOIN Comment c ON u.id = c.user.id LEFT JOIN ContentRating r on u.id = r.user.id WHERE c.id IS NOT NULL OR r.id IS NOT NULL")
    long countUsersWithCommentsOrRatings();

    //metodo para saber la cantidad de usuarios que se crearon hace 2 semanas
    long countByCreationDateAfter(LocalDateTime date);
}
