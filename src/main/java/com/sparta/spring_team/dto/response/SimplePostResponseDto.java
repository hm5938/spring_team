package com.sparta.spring_team.dto.response;


import com.sparta.spring_team.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
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
}

