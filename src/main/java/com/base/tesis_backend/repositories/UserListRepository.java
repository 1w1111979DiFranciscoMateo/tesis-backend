package com.base.tesis_backend.repositories;

import com.base.tesis_backend.entities.User;
import com.base.tesis_backend.entities.UserList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserListRepository extends JpaRepository<UserList, Long> {
    List<UserList> findByUser(User user);

    //metodo para validacion de que no existan 2 listas con el mismo nombre
    boolean existsByNameAndUser(String name, User user);

    //metodo para buscar si existe una lista con el nombre del parametro
    boolean existsByNameIgnoreCaseAndUserEmail(String name, String email);
}
