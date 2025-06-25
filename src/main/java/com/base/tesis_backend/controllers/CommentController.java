package com.base.tesis_backend.controllers;

import com.base.tesis_backend.Dtos.CommentDTOs.CommentListResponseDTO;
import com.base.tesis_backend.Dtos.CommentDTOs.CommentResponseDTO;
import com.base.tesis_backend.Dtos.CommentDTOs.CreateCommentDTO;
import com.base.tesis_backend.Dtos.CommentDTOs.UpdateCommentDTO;
import com.base.tesis_backend.services.CommentService;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.Parameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {
    //Injects
    private final CommentService commentService;

    //endpoint para recibir los valores del nuevo comentario a crear
    @PostMapping("/newComment")
    public ResponseEntity<CommentResponseDTO> createComment(
            @RequestBody CreateCommentDTO createCommentDTO,
            Authentication authentication){

        //Obtenemos el email a travez del JWT
        String email = authentication.getName();

        CommentResponseDTO comment = commentService.createComment(createCommentDTO, email);
        return new ResponseEntity<>(comment, HttpStatus.CREATED);
    }

    //endpoint para obtener todos los comentarios de un contenido por pagina
    @GetMapping("/getComments/{contentId}")
    public ResponseEntity<CommentListResponseDTO> getCommentsByContent(
            @PathVariable Long contentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "popular") String sortBy){

        CommentListResponseDTO comments = commentService.getCommentsByContent(contentId, page, size, sortBy);
        return ResponseEntity.ok(comments);
    }

    //endpoint para actualizar un comentario de un usuario logueado
    @PutMapping("/updateComment/{commentId}")
    public ResponseEntity<CommentResponseDTO> updateComment(
            @PathVariable Long commentId,
            @RequestBody UpdateCommentDTO updateCommentDTO,
            Authentication authentication){

        // Obtenemos el email a través del JWT
        String email = authentication.getName();

        CommentResponseDTO comment = commentService.updateComment(commentId, updateCommentDTO, email);
        return ResponseEntity.ok(comment);
    }

    //endpoint para eliminar un comentario de un usuario logueado
    @DeleteMapping("/deleteComment/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long commentId,
            Authentication authentication){

        // Obtenemos el email a través del JWT
        String email = authentication.getName();

        commentService.deleteComment(commentId, email);
        return ResponseEntity.noContent().build();
    }
}
