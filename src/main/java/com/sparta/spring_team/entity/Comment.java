package com.sparta.spring_team.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sparta.spring_team.dto.request.CommentRequestDto;
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

    //@JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Member member;

    //@JoinColumn(name = "post_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Post post;

    @Column(nullable = false)
    private String content;


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "comment", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<SubComment> subComments;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "comment", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<CommentLike> likes;

    public Comment update(CommentRequestDto requestDto){ this.content = requestDto.getContent(); return this; }

    public boolean validateMember(Member member){ return !this.member.equals(member); }
}
