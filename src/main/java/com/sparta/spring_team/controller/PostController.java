package com.sparta.spring_team.controller;


import com.sparta.spring_team.service.PostService;
import com.sparta.spring_team.dto.request.PostRequestDto;
import com.sparta.spring_team.dto.response.ResponseDto;
import com.sparta.spring_team.entity.Member;
import com.sparta.spring_team.entity.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class PostController {

 private final PostService postService;

    @RequestMapping(value = "/auth/posts", method = RequestMethod.POST)
    public ResponseDto<?> createPost(@RequestBody @Valid PostRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Member member =  userDetails.getMember();
        ResponseDto<?> result = postService.createPost(requestDto, member);
        return result;
    }

    @RequestMapping(value = "/auth/posts/{postid}", method = RequestMethod.PUT )
    public ResponseDto<?> updatePost(@PathVariable Long postid, @RequestBody @Valid PostRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        Member member = userDetails.getMember();
        return postService.updatePost(postid,requestDto, member);
    }

    @RequestMapping(value = "/auth/posts/{postid}", method = RequestMethod.DELETE )
    public ResponseDto<?> deletePost(@PathVariable Long postid, @AuthenticationPrincipal UserDetailsImpl userDetails){
        Member member = userDetails.getMember();
        return postService.deletePost(postid, member);
    }


}
