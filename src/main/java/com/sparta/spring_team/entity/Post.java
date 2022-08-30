package com.sparta.spring_team.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sparta.spring_team.dto.request.PostRequestDto;
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
public class Post extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Member member;

    @Column(nullable = false)
    private String title;

    @Column()
    private String content;

    @Column()
    private String imageUrl;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "post", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<Comment> comments;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "post", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Likes> likes;




    public Post(PostRequestDto requestDto, Member member){
        this.content = requestDto.getContent();
        this.title = requestDto.getTitle();
        this.imageUrl = requestDto.getImageUrl();
        this.member = member;
    }

    public Post update(PostRequestDto requestDto) {
        this.content = requestDto.getContent();
        this.title = requestDto.getTitle();
        this.imageUrl = requestDto.getImageUrl();
        return this;
    }

}
