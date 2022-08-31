package com.sparta.spring_team.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponseDto {      //게시글에 달린 댓글 목록 반환
    private Long id;
    private String membername;
    private String content;
    private List<SubCommentResponseDto> subCommentsList;
    private Long likeNum;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
