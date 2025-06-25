package com.base.tesis_backend.services;

import com.base.tesis_backend.entities.Category;
import com.base.tesis_backend.entities.User;
import com.base.tesis_backend.entities.UserCategory;
import com.base.tesis_backend.repositories.UserCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    //esto es para usar el metodo del repository para encontrar los ids
    //de las categorias que un usuario tiene como favoritas
    public List<Long> findCategoryIdsByUserIds(Long userId){
        return userCategoryRepository.findCategoryIdsByUserId(userId);
    }

    //este metodo es para encontrar las categorias favoritas de un usuario y
    //devolverlas con todos los datos.
    public List<Category> getCategoriesByUser(User user) {
        return userCategoryRepository.findByUser(user)
                .stream()
                .map(UserCategory::getCategory)
                .collect(Collectors.toList());
    }

    //este metodo es para actualizar las categorias preferidas de un usuario
    public void updateUserCategories(User user, List<Category> categories){
        //Borro las categorias que tenia guardadas
        userCategoryRepository.deleteByUser(user);

        //Creo las nuevas relaciones
        List<UserCategory> newAssociations = categories.stream()
                .map(category -> {
                    UserCategory uc = new UserCategory();
                    uc.setUser(user);
                    uc.setCategory(category);
                    return uc;
                }).toList();
        userCategoryRepository.saveAll(newAssociations);
    }

    //-------------------------------------------------------
    //A PARTIR DE ACA EMPIEZA LA LOGICA PARA EL USER ADMIN---
    //-------------------------------------------------------

    //metodo para saber cuantos usuarios tienen como favoritos cada categoria / genero
    public Map<String, Long> countUsersByCategory(){
        List<Object[]> results = userCategoryRepository.countUsersByCategoryName();
        Map<String, Long> categoryCountMap = new LinkedHashMap<>();

        for(Object[] row : results){
            String categoryName = (String) row[0];
            Long count = (Long) row[1];
            categoryCountMap.put(categoryName, count);
        }

        return categoryCountMap;
    }

}
