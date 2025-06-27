package com.base.tesis_backend.services;

import com.base.tesis_backend.entities.Comment;
import com.base.tesis_backend.entities.CommentReaction;
import com.base.tesis_backend.entities.CommentReaction.ReactionType;
import com.base.tesis_backend.entities.User;
import com.base.tesis_backend.repositories.CommentReactionRepository;
import com.base.tesis_backend.repositories.CommentRepository;
import com.base.tesis_backend.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class CommentReactionService {

    @Autowired
    private CommentReactionRepository commentReactionRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;

    //metodo para agregar o cambiar la reaccion de un usuario a un comentario
    public Map<String, Object> addOrUpdateReaction(String userEmail, Long commentId, ReactionType reactionType) {
        //Verificamos si el usuario existe
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        //Verificamos si el comentario existe
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comentario no encontrado"));

        //Buscamos si ya existe una reaccion de este usuario a ese comentario
        Optional<CommentReaction> existingReaction = commentReactionRepository
                .findByUserEmailAndCommentId(userEmail, commentId);

        if (existingReaction.isPresent()) {
            CommentReaction reaction = existingReaction.get();

            //Si es la misma reaccion la que obtenemos de la base de datos a la que
            //recibimos como parametro, la eliminamos.(toggle)
            if(reaction.getReactionType() == reactionType){
                commentReactionRepository.delete(reaction);
                updateCommentNetReactions(commentId);
                return createResponse("removed", reactionType, commentId);
            } else {
                //Si es una reaccion diferente, la actualizamos
                reaction.setReactionType(reactionType);
                commentReactionRepository.save(reaction);
                updateCommentNetReactions(commentId);
                return createResponse("updated", reactionType, commentId);
            }
        } else {
            //si no existe una reaccion, creamos una
            CommentReaction newReaction = new CommentReaction();
            newReaction.setUser(user);
            newReaction.setComment(comment);
            newReaction.setReactionType(reactionType);

            commentReactionRepository.save(newReaction);
            updateCommentNetReactions(commentId);
            return createResponse("added", reactionType, commentId);
        }
    }

    //metodo para Obtener Estadisticas de Reacciones de un comentario
    public Map<String, Object> getCommentReactionStats(Long commentId){
        Long likes = commentReactionRepository.countByCommentIdAndReactionType(commentId, ReactionType.LIKE);
        Long dislikes = commentReactionRepository.countByCommentIdAndReactionType(commentId, ReactionType.DISLIKE);

        Map<String, Object> stats = new HashMap<>();
        stats.put("likes", likes);
        stats.put("dislikes", dislikes);
        stats.put("netReactions", likes - dislikes);

        return stats;
    }

    //metodo para obtener la reaccion de un usuario especifico a un comentario
    public Map<String, Object> getUserReactionToComment(String userEmail, Long commentId) {
        Optional<CommentReaction> reaction = commentReactionRepository.findByUserEmailAndCommentId(userEmail, commentId);

        Map<String, Object> result = new HashMap<>();
        if (reaction.isPresent()) {
            result.put("hasReacted", true);
            result.put("reactionType", reaction.get().getReactionType().toString().toLowerCase());
        } else {
            result.put("hasReacted", false);
            result.put("reactionType", null);
        }

        return result;
    }

    //metodo para Actualizar el campo de Net_Reactions del comentario
    private void updateCommentNetReactions(Long commentId){
        Long likes = commentReactionRepository.countByCommentIdAndReactionType(commentId, ReactionType.LIKE);
        Long dislikes = commentReactionRepository.countByCommentIdAndReactionType(commentId, ReactionType.DISLIKE);
        long netReactions = likes - dislikes;

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comentario no encontrado"));

        comment.setNetReactions((int) netReactions);
        commentRepository.save(comment);
    }

    //metodo para crear una respuesta estandarizada
    private Map<String, Object> createResponse(String action, ReactionType reactionType, Long commentId){
        Map<String, Object> response = new HashMap<>();
        response.put("action", action);
        response.put("reactionType", reactionType.toString().toLowerCase());
        response.put("stats", getCommentReactionStats(commentId));

        return response;
    }
}
