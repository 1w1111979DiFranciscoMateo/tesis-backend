package com.base.tesis_backend.Dtos;

import com.base.tesis_backend.entities.Category;
import com.base.tesis_backend.entities.Platform;

import java.time.LocalDateTime;
import java.util.List;

//Un DTO es un Data Transfer Object, es una clase que sirve para conectar el front y el back
//Esta clase recibe el objeto como lo envia el front y despues se dividen las cosas necesarias
//para que cada una vaya a donde corresponde, en este caso username, email, password y
//creationDate van a la tabla User, categoriesIds va a la tabla UserCategory y
// userPlatforms va a la tabla UserPlatform.

public class UserRegisterDTO {
    public String username;
    public String email;
    public String password;
    public LocalDateTime creationDate;
    public List<Category> userCategories; //categorias seleccionadas
    public List<Platform> userPlatforms; //plataformas seleccionadas
}
