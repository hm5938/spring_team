package com.sparta.spring_team.controller;


import com.sparta.spring_team.dto.request.LikeRequestDto;
import com.sparta.spring_team.service.LikeService;
import com.sparta.spring_team.dto.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class LikeController {

    private final LikeService likeService;

    @RequestMapping(value = "/auth/likes", method = RequestMethod.POST)
    public ResponseDto<?> createlike(@RequestBody @Valid LikeRequestDto requestDto, HttpServletRequest request) {
        return likeService.createLike(requestDto, request);
    }

    @RequestMapping(value = "/auth/likes/delete", method = RequestMethod.POST)
    public ResponseDto<?> deletelike (@RequestBody @Valid LikeRequestDto requestDto, HttpServletRequest request){
        return likeService.deleteLike(requestDto,request);
    }
}
