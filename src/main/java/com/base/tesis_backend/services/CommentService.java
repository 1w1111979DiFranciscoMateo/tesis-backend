package com.base.tesis_backend.services;

import com.base.tesis_backend.Dtos.CommentDTOs.CommentListResponseDTO;
import com.base.tesis_backend.Dtos.CommentDTOs.CommentResponseDTO;
import com.base.tesis_backend.Dtos.CommentDTOs.CreateCommentDTO;
import com.base.tesis_backend.Dtos.CommentDTOs.UpdateCommentDTO;
import com.base.tesis_backend.entities.AudioVisualContent;
import com.base.tesis_backend.entities.Comment;
import com.base.tesis_backend.entities.User;
import com.base.tesis_backend.repositories.AudioVisualContentRepository;
import com.base.tesis_backend.repositories.CommentRepository;
import com.base.tesis_backend.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    //Injects
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final AudioVisualContentRepository audioVisualContentRepository;

    //metodo para crear un comentario nuevo en un contenido audiovisual.
    //S el contenido audiovisual no esta en la base de datos, se guardan los
    //valores de ese contenido
    //(Para mantener la regla de si el usuario tiene una interaccion con un contenido
    // este contenido tiene que estar en la base de datos)
    @Transactional
    public CommentResponseDTO createComment(CreateCommentDTO createCommentDTO, String email) {
        //Verificamos que el usuario exista por email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        //Buscamos si el contenido audiovisual ya esta en la base de datos, o lo
        //creamos si no existe
        AudioVisualContent content = audioVisualContentRepository.findById(createCommentDTO.getContentId())
                .orElseGet(() -> {
                    //Si no existe lo creo y lo guardo
                    AudioVisualContent newContent = new AudioVisualContent();
                    newContent.setId(createCommentDTO.getContentId());
                    newContent.setTitle(createCommentDTO.getTitle());
                    newContent.setPosterPath(createCommentDTO.getPosterPath());
                    newContent.setRating(createCommentDTO.getRating());
                    newContent.setType(createCommentDTO.getType());
                    return audioVisualContentRepository.save(newContent);
                });

        //Creamos el comentario
        Comment comment = new Comment();
        comment.setUser(user);
        comment.setContent(content);
        comment.setText(createCommentDTO.getText().trim());
        comment.setNetReactions(0);

        Comment savedComment = commentRepository.save(comment);

        //temporal
        System.out.println("Comentario creado: " + savedComment.toString());

        return convertToResponseDto(savedComment);
    }

    //Metodo para enviar al frontend todos los comentarios que pertenecen a un contenido
    //audiovisual por paginas
    @Transactional
    public CommentListResponseDTO getCommentsByContent(Long contentId, int page, int size, String sortBy){
        //Verificas que el contenido exista
            if(!audioVisualContentRepository.existsById(contentId)){
                throw new RuntimeException("Contenido audiovisual no encontrado");
            }

        Pageable pageable = PageRequest.of(page, size);
        Page<Comment> commentsPage;

        //Determinar el tipo de ordenamiento
        if("popular".equalsIgnoreCase(sortBy)){
            commentsPage = commentRepository.findByContentIdOrderByNetReactionsDesc(contentId, pageable);
        } else {
            commentsPage = commentRepository.findByContentIdOrderByCreationDateDesc(contentId, pageable);
        }

        List<CommentResponseDTO> comments = commentsPage.getContent().stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());

        CommentListResponseDTO response = new CommentListResponseDTO();
        response.setComments(comments);
        response.setCurrentPage(commentsPage.getNumber());
        response.setTotalPages(commentsPage.getTotalPages());
        response.setTotalElements(commentsPage.getTotalElements());
        response.setPageSize(commentsPage.getSize());
        response.setHasNext(commentsPage.hasNext());
        response.setHasPrevious(commentsPage.hasPrevious());

        return response;
    }

    //metodo para actualizar el texto de un comentario (solo valido si el
    // commentario es perteneciente al usuario logueado)
    @Transactional
    public CommentResponseDTO updateComment(Long commentId, UpdateCommentDTO updatedComment, String email){
        //Verificamos que el usuario exista por email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        //Verificamos que exista el comentario por id
        Comment comment = commentRepository.findByIdWithRelations(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        //Verificamos que el usuario sea el propietario del comentario a actualizar
        if(!comment.getUser().getId().equals(user.getId())){
            throw new RuntimeException("El usuario logueado no es propietario del comentario a editar");
        }

        comment.setText(updatedComment.getText().trim());
        Comment updated = commentRepository.save(comment);

        return convertToResponseDto(updated);
    }

    //metodo para eliminar un comentario (solo valido si el comentario
    // es perteneciente al usuario logueado)
    @Transactional
    public void deleteComment(Long commentId, String email){
        //Verificamos que el usuario exista por email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        //Verificamos que exista el comentario por id
        Comment comment = commentRepository.findByIdWithRelations(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        //Verificamos que el usuario sea el propietario del comentario a eliminar
        if(!comment.getUser().getId().equals(user.getId())){
            throw new RuntimeException("El usuario logueado no es propietario del comentario a editar");
        }

        commentRepository.delete(comment);
    }

    //DTO para manejar las response de los metodos del service, no es necesario pero
    //facilita el no escribir siempre lo mismo
    private CommentResponseDTO convertToResponseDto(Comment comment){
        CommentResponseDTO dto = new CommentResponseDTO();
        dto.setId(comment.getId());
        dto.setContentId(comment.getContent().getId());
        dto.setText(comment.getText());
        dto.setNetReactions(comment.getNetReactions());
        dto.setCreationDate(comment.getCreationDate());
        dto.setUpdatedDate(comment.getUpdatedDate());
        dto.setEdited(comment.getUpdatedDate() != null &&
                comment.getUpdatedDate().isAfter(comment.getCreationDate().plusMinutes(1)));

        //Convertir informacion basica del usuario
        User userDto = new User();
        userDto.setId(comment.getUser().getId());
        userDto.setUsername(comment.getUser().getRealUsername());
        userDto.setEmail(comment.getUser().getEmail());
        dto.setUser(userDto);

        return dto;
    }
}
