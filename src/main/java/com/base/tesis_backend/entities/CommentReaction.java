package com.base.tesis_backend.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "commentsreactions",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "comment_id"}))
@EqualsAndHashCode(exclude = {"user", "comment"})
@ToString(exclude = {"user", "comment"})
public class CommentReaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;

    @Enumerated(EnumType.STRING)
    @Column(name = "reaction_type", nullable = false)
    private ReactionType reactionType;

    @CreationTimestamp
    @Column(name = "reaction_date", nullable = false, updatable = false)
    private LocalDateTime reactionDate;

    public enum ReactionType {
        LIKE, DISLIKE
    }
}
