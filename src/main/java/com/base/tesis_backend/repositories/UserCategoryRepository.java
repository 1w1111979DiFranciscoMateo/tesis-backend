package com.base.tesis_backend.repositories;

import com.base.tesis_backend.entities.UserCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//Esto es un repository, es la conexion entre la API y la base de datos
@Repository
public interface UserCategoryRepository extends JpaRepository<UserCategory, Long> {
}
