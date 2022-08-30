package com.sparta.spring_team.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SubCommentRequestDto {

    @NotBlank
    private Long commentId;
    @NotBlank
    private String content;
}
