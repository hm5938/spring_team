package com.sparta.spring_team.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Comment extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Member member;

    @Column(nullable = false)
    private String comment;


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "comment", cascade = CascadeType.ALL)
    private List<SubComment> subComments;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "comment", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Likes> likes;

    @Column()
    private Long likeNum;

}
