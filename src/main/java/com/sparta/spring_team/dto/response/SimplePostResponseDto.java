package com.sparta.spring_team.dto.response;


import com.sparta.spring_team.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SimplePostResponseDto {

    private Long id;
    private String title;
    private String memberName;
    private Long likeNum;
    private Long commentNum;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public SimplePostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.memberName = post.getMember().getMembername();
        this.likeNum = Long.valueOf(post.getLikes().size());
        this.commentNum = Long.valueOf(post.getComments().size());
        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();
    }
}

