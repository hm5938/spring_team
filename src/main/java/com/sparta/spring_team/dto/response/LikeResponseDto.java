package com.sparta.spring_team.dto.response;

import com.sparta.spring_team.shared.LikeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LikeResponseDto {
    private Long id;
    private String membername;
    private LikeType type;
    private Long typeId;
}
