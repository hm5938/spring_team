package com.sparta.spring_team.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDto {
    private Long id;
    private String memberName;
    private String title;
    private String content;
    private String imageUrl;
    private Long commentNum;
    private Long subCommentNum;
    private Long likeNum;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
