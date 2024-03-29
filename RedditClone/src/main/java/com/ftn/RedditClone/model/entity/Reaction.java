package com.ftn.RedditClone.model.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.FetchType.LAZY;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Reaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ReactionType type;

    private LocalDate timestamp;


    @ManyToOne(fetch = EAGER,cascade = CascadeType.REMOVE)
    @JoinColumn(name = "postId", referencedColumnName = "id")
    private Post post;

    @ManyToOne(fetch = LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "commentId", referencedColumnName = "id")
    private Comment comment;

    @ManyToOne(fetch = LAZY,  cascade = CascadeType.REMOVE)
    @JoinColumn(name = "userId", referencedColumnName = "id")
    private User user;

}
