package com.sparta.spring_team.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sparta.spring_team.dto.request.SubCommentRequestDto;
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
public class SubComment extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Member member;

    @JoinColumn(name = "comment_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Comment comment;

    @Column(nullable = false)
    private String content;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "subcomment", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Likes> likes;

    public SubComment update(SubCommentRequestDto requestDto){ this.content = requestDto.getContent(); return this; }
    public boolean validateMember(Member member){ return this.member.equals(member); }

}
