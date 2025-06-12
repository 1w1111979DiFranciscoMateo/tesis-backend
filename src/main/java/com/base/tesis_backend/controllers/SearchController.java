package com.base.tesis_backend.controllers;

import com.base.tesis_backend.Dtos.SearchDTOs.SearchContentDTO;
import com.base.tesis_backend.services.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {
    @Autowired
    private SearchService searchService;

    //endpoint que recibe el titulo a buscar y devuelve un array con todos los resultados
    //tanto para peliculas como para series
    @GetMapping()
    public List<SearchContentDTO> searchContent(@RequestParam("query") String query, Authentication authentication) {
        return searchService.getSearchResults(query);
    }

}
