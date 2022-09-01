package com.sparta.spring_team.controller;

import com.sparta.spring_team.dto.request.CommentRequestDto;
import com.sparta.spring_team.dto.response.ResponseDto;
import com.sparta.spring_team.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class CommentController {
    private final CommentService commentService;

    @RequestMapping(value = "/auth/comment", method = RequestMethod.POST)
    public ResponseDto<?> createComment(@RequestBody @Valid CommentRequestDto requestDto, HttpServletRequest request) {
        return commentService.createComment(requestDto, request);
    }

    @RequestMapping(value = "/comment/{postid}", method = RequestMethod.GET)
    public ResponseDto<?> getAllComments(@PathVariable Long postid){
        return commentService.getAllCommentsByPost(postid);
    }

    @RequestMapping(value = "/auth/comment/{commentid}", method = RequestMethod.PUT)
    public ResponseDto<?> updateComment(@PathVariable Long commentid, @RequestBody @Valid CommentRequestDto requestDto,
                                        HttpServletRequest request){
        return commentService.updateComment(commentid, requestDto, request);
    }

    @RequestMapping(value = "/auth/comment/{commentid}", method = RequestMethod.DELETE)
    public ResponseDto<?> deleteComment(@PathVariable Long commentid, HttpServletRequest request){
        return commentService.deleteComment(commentid, request);
    }

}
