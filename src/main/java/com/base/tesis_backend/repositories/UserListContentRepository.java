package com.base.tesis_backend.repositories;

import com.base.tesis_backend.entities.UserList;
import com.base.tesis_backend.entities.UserListContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserListContentRepository extends JpaRepository<UserListContent, Long> {
    //metodo para encontrar los contenidos que pertenecen a una lista
    List<UserListContent> findByList(UserList list);
}
