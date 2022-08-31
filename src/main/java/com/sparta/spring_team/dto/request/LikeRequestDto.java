package com.sparta.spring_team.dto.request;

import com.sparta.spring_team.entity.Member;
import com.sparta.spring_team.shared.LikeType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LikeRequestDto{

    @NotNull
    private Long id;
    @NotNull
    private LikeType type; //Post, Comment, SubComment
}
