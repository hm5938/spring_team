package com.sparta.spring_team.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Likes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Member member;


    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Post post;


    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Comment comment;


    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private SubComment subcomment;


    @Enumerated(EnumType.ORDINAL)
    private LikeType type;



}
