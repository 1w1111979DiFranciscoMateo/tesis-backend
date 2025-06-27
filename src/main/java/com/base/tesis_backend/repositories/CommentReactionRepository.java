package com.base.tesis_backend.repositories;

import com.base.tesis_backend.entities.CommentReaction;
import com.base.tesis_backend.entities.CommentReaction.ReactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentReactionRepository extends JpaRepository<CommentReaction, Long> {

    //metodo para buscar una reaccion especifica de un usuario a un comentario
    @Query("SELECT cr FROM CommentReaction cr WHERE cr.user.email = :userEmail AND cr.comment.id = :commentId")
    Optional<CommentReaction> findByUserEmailAndCommentId(@Param("userEmail") String userEmail, @Param("commentId") Long commentId);

    //metodo para contar los likes/dislikes de un comentario
    @Query("SELECT COUNT(cr) FROM CommentReaction cr WHERE cr.comment.id = :commentId AND cr.reactionType = :reactionType")
    Long countByCommentIdAndReactionType(@Param("commentId") Long commentId, @Param("reactionType") ReactionType reactionType);

    //metodo para verificar si un usuario ya reaccionó a un comentario
    @Query("SELECT COUNT(cr) > 0 FROM CommentReaction cr WHERE cr.user.email = :userEmail AND cr.comment.id = :commentId")
    boolean existsByUserEmailAndCommentId(@Param("userEmail") String userEmail, @Param("commentId") Long commentId);

    //metodo para Eliminar una reaccion especifica
    @Query("DELETE FROM CommentReaction cr WHERE cr.user.email = :userEmail AND cr.comment.id = :commentId")
    void deleteByUserEmailAndCommentId(@Param("userEmail") String userEmail, @Param("commentId") Long commentId);
}
