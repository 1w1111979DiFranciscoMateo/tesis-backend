package com.base.tesis_backend.controllers;

import com.base.tesis_backend.entities.Category;
import com.base.tesis_backend.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor

public class CategoryController {
    @Autowired
    private final CategoryService categoryService;

    //Endpoint que devuelve todas las categorias de la base de datos
    @GetMapping("/getAll")
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAll());
    }

}
