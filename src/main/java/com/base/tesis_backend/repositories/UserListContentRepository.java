package com.base.tesis_backend.repositories;

import com.base.tesis_backend.entities.AudioVisualContent;
import com.base.tesis_backend.entities.UserList;
import com.base.tesis_backend.entities.UserListContent;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserListContentRepository extends JpaRepository<UserListContent, Long> {
    //metodo para encontrar los contenidos que pertenecen a una lista
    List<UserListContent> findByList(UserList list);
    //metodo para encontrar si un contenido esta dentro de una lista
    Optional<UserListContent> findByListAndContent(UserList list, AudioVisualContent content);
    //metodo para encontrar todas las relaciones entre un contenido audiovisual
    //y cona lista del usuario logueado
    List<UserListContent> findByContent(AudioVisualContent content);
    //metodo para eliminar las relaciones que existen entre una lista y su contenido
    @Transactional
    void deleteByList(UserList list);
}
