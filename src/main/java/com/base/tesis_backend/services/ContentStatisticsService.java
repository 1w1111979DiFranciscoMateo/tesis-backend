package com.base.tesis_backend.services;

import com.base.tesis_backend.Dtos.AdminDTOs.ContentAvgRatingDTO;
import com.base.tesis_backend.Dtos.AdminDTOs.ContentKpiStatsDTO;
import com.base.tesis_backend.Dtos.AdminDTOs.ContentStatsDTO;
import com.base.tesis_backend.repositories.AudioVisualContentRepository;
import com.base.tesis_backend.repositories.CommentRepository;
import com.base.tesis_backend.repositories.ContentRatingRepository;
import com.base.tesis_backend.repositories.UserListContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ContentStatisticsService {

    //Injections
    private final ContentRatingRepository contentRatingRepository;
    private final CommentRepository commentRepository;
    private final UserListContentRepository contentListRepository;
    private final AudioVisualContentRepository contentRepository;

    //metodo para obtener los contenidos mas puntuados (cantidad de votos)
    public List<ContentStatsDTO> getMostRatedContents(int limit){
        return contentRatingRepository.findMostRatedContents(PageRequest.of(0, limit));
    }

    //Contenidos más comentados
    public List<ContentStatsDTO> getMostCommentedContents(int limit) {
        return commentRepository.findMostCommentedContents(limit);
    }

    //Contenidos con mejor puntuación promedio (solo con mínimo 5 votos)
    public List<ContentAvgRatingDTO> getTopRatedContents(int limit) {
        return contentRatingRepository.findTopRatedContents(2, PageRequest.of(0, limit)); // mínimo 5 votos
    }

    //metodo para devolver todos los kpis al front de estadisticas de contenidos
    public ContentKpiStatsDTO getContentKpiStats(){
        //total de contenidos en la base de datos
        long total = contentRepository.count();

        long rated = contentRatingRepository.findDistinctContentCount();
        long commented = commentRepository.findDistinctContentCount();
        long inLists = contentListRepository.countDistinctContentIds();

        double porcentageRated = total > 0 ? (rated * 100.0) / total : 0.0;
        double porcentageCommented = total > 0 ? (commented * 100.0) / total : 0.0;
        double porcentageInLists = total > 0 ? (inLists * 100.0) / total : 0.0;

        return new ContentKpiStatsDTO(total, porcentageRated, porcentageCommented, porcentageInLists);
    }
}
