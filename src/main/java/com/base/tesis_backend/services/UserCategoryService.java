package com.base.tesis_backend.services;

import com.base.tesis_backend.entities.Category;
import com.base.tesis_backend.entities.User;
import com.base.tesis_backend.entities.UserCategory;
import com.base.tesis_backend.repositories.UserCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//Esto es un Service, aca va la logica de la aplicacion (especificamente la
// logica de userCategory)
@Service
public class UserCategoryService {
    @Autowired
    private UserCategoryRepository userCategoryRepository;

    //metodo para guardar que un usuario le gusta una categoria especifica
    public void addUserCategory(User user, Category category) {
        UserCategory userCategory = new UserCategory();
        userCategory.setUser(user);
        userCategory.setCategory(category);
        userCategoryRepository.save(userCategory);
    }
}
