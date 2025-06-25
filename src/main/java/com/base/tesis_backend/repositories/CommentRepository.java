package com.base.tesis_backend.repositories;

import com.base.tesis_backend.Dtos.AdminDTOs.ContentStatsDTO;
import com.base.tesis_backend.entities.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    //metodo para obtener comentarios por contenido audiovisual con paginacion
    @Query("SELECT c FROM Comment c WHERE c.content.id =:contentId ORDER BY c.creationDate DESC")
    Page<Comment> findByContentIdOrderByCreationDateDesc(@Param("contentId") Long contentId, Pageable pageable);

    //metodo para obtener comentarios por contenido audiovisual ordenados por reacciones (mas populares)
    @Query("select c FROM Comment c WHERE c.content.id = :contentId ORDER BY c.netReactions desc, c.creationDate DESC")
    Page<Comment> findByContentIdOrderByNetReactionsDesc(@Param("contentId") Long contentId, Pageable pageable);

    //Obtener un comentario en especifico con sus relaciones
    @Query("SELECT c FROM Comment c LEFT JOIN FETCH c.user LEFT JOIN FETCH c.content WHERE c.id = :contentId")
    Optional<Comment> findByIdWithRelations(@Param("contentId") Long contentId);

    //metodo para encontrar los contenidos que tienen mas comentarios
    @Query("SELECT new com.base.tesis_backend.Dtos.AdminDTOs.ContentStatsDTO(c.content.title, COUNT(c)) " +
            "FROM Comment c GROUP BY c.content.title ORDER BY COUNT(c) DESC")
    List<ContentStatsDTO> findMostCommentedContents(Pageable pageable);

    default List<ContentStatsDTO> findMostCommentedContents(int limit) {
        return findMostCommentedContents(PageRequest.of(0, limit));
    }

    //metodo para obtener la cantidad de contenidos que tienen aunquesea 1 comentario
    @Query("SELECT COUNT(DISTINCT c.content.id) FROM Comment c")
    Long findDistinctContentCount();
}
