package com.sparta.spring_team.controller;

import com.sparta.spring_team.dto.request.SubCommentRequestDto;
import com.sparta.spring_team.dto.response.ResponseDto;
import com.sparta.spring_team.entity.Member;
import com.sparta.spring_team.entity.UserDetailsImpl;
import com.sparta.spring_team.service.SubCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class SubCommentController {

    private final SubCommentService subCommentService;

    @RequestMapping(value = "/auth/subcomment", method = RequestMethod.POST)
    public ResponseDto<?> createSubComment(@RequestBody @Valid SubCommentRequestDto requestDto,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Member member = userDetails.getMember();
        ResponseDto<?> result = subCommentService.createSubComment(requestDto, member);
        return result;
    }

    @RequestMapping(value = "/auth/subcomment/{subcommentid}", method = RequestMethod.PUT )
    public ResponseDto<?> updateSubComment(@PathVariable Long subcommentid, @RequestBody @Valid SubCommentRequestDto requestDto,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails){
        Member member = userDetails.getMember();
        return subCommentService.updateSubComment(subcommentid, requestDto, member);
    }
}

