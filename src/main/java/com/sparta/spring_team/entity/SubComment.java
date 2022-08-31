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

    @Column(nullable = false)
    private String content;

    //JoinColumn(name = "comment_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Comment comment;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "subcomment", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<SubCommentLike> likes;

    public SubComment update(SubCommentRequestDto requestDto){ this.content = requestDto.getContent(); return this; }
    public boolean validateMember(Member member){ return this.member.equals(member); }

    public int getLikesNum(){
        if(likes == null) return 0;

        int count = 0;
        for(SubCommentLike like : likes){
            if(like.getSubcomment().equals(this)) ++count;
        }

        return count;
    }
}
