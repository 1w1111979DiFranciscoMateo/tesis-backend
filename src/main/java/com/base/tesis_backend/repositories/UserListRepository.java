package com.base.tesis_backend.repositories;

import com.base.tesis_backend.entities.User;
import com.base.tesis_backend.entities.UserList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserListRepository extends JpaRepository<UserList, Long> {
    List<UserList> findByUser(User user);

    //metodo para validacion de que no existan 2 listas con el mismo nombre
    boolean existsByNameAndUser(String name, User user);

    //metodo para buscar si existe una lista con el nombre del parametro
    boolean existsByNameIgnoreCaseAndUserEmail(String name, String email);

    //metodo para buscar una lista por id y por email del usuario, es para la pantalla
    //la pantalla del detalle de una lista
    Optional<UserList> findByIdAndUserEmail(Long id, String email);
}
