package com.base.tesis_backend.repositories;

import com.base.tesis_backend.Dtos.AdminDTOs.ContentAvgRatingDTO;
import com.base.tesis_backend.Dtos.AdminDTOs.ContentStatsDTO;
import com.base.tesis_backend.entities.ContentRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface ContentRatingRepository extends JpaRepository<ContentRating, Long> {
    //metodo para encontrar un rating de un usuario en un contenido audiovisual
    Optional<ContentRating> findByUserIdAndContentId(Long userId, Long contentId);

    //metodo para obtener el average rating de un contenido audiovisual
    @Query("SELECT AVG(cr.rating) FROM ContentRating cr WHERE cr.content.id = :contentId")
    BigDecimal getAverageRatingByContentId(@Param("contentId") Long contentId);

    //metodo para obtener el total de votos que tiene un contenido audiovisual
    @Query("SELECT COUNT(cr) FROM ContentRating cr WHERE cr.content.id = :contentId")
    Long getTotalRatingsByContentId(@Param("contentId") Long contentId);

    //metodo para saver si un usuario dejo un rating en un contenido audiovisual
    boolean existsByUserIdAndContentId(Long userId, Long contentId);

    //metodo para encontrar los contenidos mas puntuados (cantidad de ratings)
    @Query("SELECT NEW com.base.tesis_backend.Dtos.AdminDTOs.ContentStatsDTO(cr.content.title, COUNT(cr)) " +
            "FROM ContentRating cr GROUP BY cr.content.title ORDER BY COUNT(cr) DESC")
    List<ContentStatsDTO> findMostRatedContents(Pageable pageable);

    //metodo para encontrar los contenidos mejor puntuados
    @Query("SELECT NEW com.base.tesis_backend.Dtos.AdminDTOs.ContentAvgRatingDTO(cr.content.title, AVG(cr.rating)) " +
            "FROM ContentRating cr GROUP BY cr.content.title " +
            "HAVING COUNT(cr) >= :minVotes ORDER BY AVG(cr.rating) DESC")
    List<ContentAvgRatingDTO> findTopRatedContents(@Param("minVotes") long minVotes, Pageable pageable);

    //metodo para contar la cantidad de contenidos que tienen al menos 1 comentario
    @Query("SELECT COUNT(DISTINCT cr.content.id) FROM ContentRating cr")
    Long findDistinctContentCount();
}
