package com.base.tesis_backend.repositories;

import com.base.tesis_backend.entities.User;
import com.base.tesis_backend.entities.UserPlatform;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserPlatformRepository extends JpaRepository<UserPlatform, Long> {
    //esto es un metodo para encontrar los ids de las plataformas que un usuario tiene
    //como disponibles
    @Query("SELECT up.platform.id FROM UserPlatform up WHERE up.user.id = :userId")
    List<Long> findPlatformIdsByUserId(@Param("userId") Long userId);

    //este metodo es para encontrar las plataformas de un usuario por usuario.
    List<UserPlatform> findByUser(User user);

    //este metodo es para eliminar las plataformas de un usuario (para el editar perfil)
    void deleteByUser(User user);

    //metodo para saber cuales son las plataformas mas usadas por los usuarios
    //es para el Admin
    @Query("SELECT up.platform.name, COUNT(up.user.id) FROM UserPlatform up GROUP BY up.platform.name ORDER BY COUNT(up.user.id) DESC")
    List<Object[]> countUsersByPlatformName();
}
