package com.base.tesis_backend.services;

import com.base.tesis_backend.entities.Category;
import com.base.tesis_backend.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    @Autowired
    private final CategoryRepository categoryRepository;

    //metodo que obtiene todas las categorias de la base de datos
    public List<Category> getAll() {
        return categoryRepository.findAll();
    }
}
