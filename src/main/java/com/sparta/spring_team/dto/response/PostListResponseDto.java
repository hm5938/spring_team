package com.sparta.spring_team.dto.response;


import com.sparta.spring_team.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostListResponseDto {

    private List<SimplePostResponseDto> post = new ArrayList<>();



   public PostListResponseDto getPostList(List<Post> postlist){
        this.post = new ArrayList<>();
        for (Post p : postlist) {
            this.post.add(new SimplePostResponseDto(p));
        }

        return this;
    }
}
