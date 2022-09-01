package com.sparta.spring_team.controller;

import com.sparta.spring_team.dto.request.SubCommentRequestDto;
import com.sparta.spring_team.dto.response.ResponseDto;
import com.sparta.spring_team.service.SubCommentService;
import lombok.RequiredArgsConstructor;
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

    @RequestMapping(value = "/subcomment/{commentid}", method = RequestMethod.GET)
    public ResponseDto<?> getAllSubComments(@PathVariable Long commentid){ return subCommentService.getAllSubCommentsByComment(commentid); }

    @RequestMapping(value = "/auth/subcomment/{subcommentid}", method = RequestMethod.PUT)
    public ResponseDto<?> updateSubComment(@PathVariable Long subcommentid, @RequestBody @Valid SubCommentRequestDto requestDto,
                                        HttpServletRequest request){
        return subCommentService.updateSubComment(subcommentid, requestDto, request);
    }

    @RequestMapping(value = "/auth/subcomment/{subcommentid}", method = RequestMethod.DELETE)
    public ResponseDto<?> deleteSubComment(@PathVariable Long subcommentid, HttpServletRequest request){
        return subCommentService.deleteSubComment(subcommentid, request);
    }
}

