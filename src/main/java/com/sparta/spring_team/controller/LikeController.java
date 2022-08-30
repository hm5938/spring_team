package com.sparta.spring_team.controller;


import com.sparta.spring_team.dto.request.LikeRequestDto;
import com.sparta.spring_team.service.LikeService;
import com.sparta.spring_team.dto.response.ResponseDto;
import com.sparta.spring_team.entity.Member;
import com.sparta.spring_team.entity.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class LikeController {

    private final LikeService likeService;

    @RequestMapping(value = "/auth/likes", method = RequestMethod.POST)
    public ResponseDto<?> createlike(@RequestBody @Valid LikeRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Member member = userDetails.getMember();
        return likeService.createLike(requestDto, member);
    }

    @RequestMapping(value = "/auth/likes/delete", method = RequestMethod.POST)
    public ResponseDto<?> deletelike (@RequestBody @Valid LikeRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        Member member = userDetails.getMember();
        return likeService.deleteLike(requestDto,member);
    }
}
