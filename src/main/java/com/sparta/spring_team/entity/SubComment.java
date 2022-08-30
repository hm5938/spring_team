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
public class SubComment extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Member member;

    @Column(nullable = false)
    private String subComment;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "subcomment", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Likes> likes;

    @Column()
    private Long likeNum;

    public Long addLikeNum(boolean isadd){
        if(isadd){
            this.likeNum ++;
        }else{
            if(likeNum>0) this.likeNum --;
        }
        return this.likeNum;
    }
}
