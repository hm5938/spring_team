package com.sparta.spring_team.controller;

import com.sparta.spring_team.dto.request.CommentRequestDto;
import com.sparta.spring_team.dto.request.SubCommentRequestDto;
import com.sparta.spring_team.dto.response.ResponseDto;
import com.sparta.spring_team.entity.Member;
import com.sparta.spring_team.entity.UserDetailsImpl;
import com.sparta.spring_team.service.SubCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class SubCommentController {

    private final SubCommentService subCommentService;

    @RequestMapping(value = "/auth/subcomment", method = RequestMethod.POST)
    public ResponseDto<?> createSubComment(@RequestBody @Valid SubCommentRequestDto requestDto, HttpServletRequest request) {
        return subCommentService.createSubComment(requestDto, request);
    }

    @RequestMapping(value = "/subcomment", method = RequestMethod.GET)
    public ResponseDto<?> getAllSubComments(@PathVariable Long id){ return subCommentService.getAllSubCommentsByComment(id); }

    @RequestMapping(value = "/auth/subcomment/{id}", method = RequestMethod.PUT)
    public ResponseDto<?> updateSubComment(@PathVariable Long id, @RequestBody @Valid SubCommentRequestDto requestDto,
                                        HttpServletRequest request){
        return subCommentService.updateSubComment(id, requestDto, request);
    }

    @RequestMapping(value = "/auth/subcomment/{id}", method = RequestMethod.DELETE)
    public ResponseDto<?> deleteSubComment(@PathVariable Long id, HttpServletRequest request){
        return subCommentService.deleteSubComment(id, request);
    }
}

