package com.sparta.spring_team.dto;


import com.sparta.spring_team.entity.LikeType;
import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Builder
public class LikeDto <T> {
    @NotNull
    private LikeType likeType;

    @NotNull
    private T data;
}
