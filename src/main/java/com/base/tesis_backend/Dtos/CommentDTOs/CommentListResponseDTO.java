package com.base.tesis_backend.Dtos.CommentDTOs;

import lombok.Data;

import java.util.List;

//DTo para devolverle al frontend los comentarios por paginas
//asi se implementa paginado
@Data
public class CommentListResponseDTO {
    private List<CommentResponseDTO> comments;
    private int currentPage;
    private int totalPages;
    private long totalElements;
    private int pageSize;
    private boolean hasNext;
    private boolean hasPrevious;
}
