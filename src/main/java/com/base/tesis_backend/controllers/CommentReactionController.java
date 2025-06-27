package com.base.tesis_backend.controllers;

import com.base.tesis_backend.entities.CommentReaction;
import com.base.tesis_backend.entities.CommentReaction.ReactionType;
import com.base.tesis_backend.services.CommentReactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/commentsReactions")
public class CommentReactionController {

    @Autowired
    private CommentReactionService commentReactionService;

    //endpoint para agregarle like a un comentario
    @PostMapping("/{commentId}/like")
    public ResponseEntity<?> likeComment(
            @PathVariable Long commentId,
            Authentication authentication){
        try {
            //Obtenemos el email del usuario a travez del JWT
            String userEmail = authentication.getName();

            Map<String, Object> result = commentReactionService.addOrUpdateReaction(userEmail, commentId, ReactionType.LIKE);

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    //endpoint para agregarle un dislike a un comentario
    @PostMapping("/{commentId}/dislike")
    public ResponseEntity<?> dislikeComment(
            @PathVariable Long commentId,
            Authentication authentication){
        try {
            //Obtenemos el email del usuario a travez del JWT
            String userEmail = authentication.getName();

            Map<String, Object> result = commentReactionService.addOrUpdateReaction(userEmail, commentId, ReactionType.DISLIKE);

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    //endpoint para obtener estadisticas de reacciones de un comentario
    @GetMapping("/{commentId}/commentReactionStats")
    public ResponseEntity<?> getCommentReactionStats(@PathVariable Long commentId){
        try {
            Map<String, Object> stats = commentReactionService.getCommentReactionStats(commentId);

            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    //endpoint para obtener la reaccion especifica del usuario logueado a un comentario
    @GetMapping("/{commentId}/my-reaction")
    public ResponseEntity<?> getMyReactionToComment(
            @PathVariable Long commentId,
            Authentication authentication){
        try {
            //obtenemos email a traves de JWT
            String userEmail = authentication.getName();

            Map<String, Object> reaction = commentReactionService.getUserReactionToComment(userEmail, commentId);

            return ResponseEntity.ok(reaction);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    
}
