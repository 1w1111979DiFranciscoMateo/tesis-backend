package com.base.tesis_backend.repositories;

import com.base.tesis_backend.entities.User;
import com.base.tesis_backend.entities.UserCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

//Esto es un repository, es la conexion entre la API y la base de datos
@Repository
public interface UserCategoryRepository extends JpaRepository<UserCategory, Long> {
    //esto es un metodo para encontrar los ids de las categorias que un usuario tiene
    //como favoritas
    @Query("SELECT uc.category.id FROM UserCategory uc WHERE uc.user.id = :userId")
    List<Long> findCategoryIdsByUserId(@Param("userId") Long userId);

    //este metodo es para encontrar las categorias de un usuario por usuario
    List<UserCategory> findByUser(User user);

    //este metodo es para eliminar las categorias de un usuario (para el editar perfil)
    void deleteByUser(User user);
}
