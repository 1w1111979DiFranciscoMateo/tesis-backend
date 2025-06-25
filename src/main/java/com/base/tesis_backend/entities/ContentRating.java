package com.base.tesis_backend.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "contentrating",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "content_id"}))
@EqualsAndHashCode(exclude = {"user", "content"})
@ToString(exclude = {"user", "content"})
public class ContentRating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_id", nullable = false)
    private AudioVisualContent content;

    @Column(nullable = false, precision = 3, scale = 1)
    @DecimalMin(value = "1.0", message = "Rating must be between 1.0 and 10.0")
    @DecimalMax(value = "10.0", message = "Rating must be between 1.0 and 10.0")
    private BigDecimal rating;

    @CreationTimestamp
    @Column(name = "creation_date", nullable = false, updatable = false)
    private LocalDateTime creationDate;
}
