package com.sparta.spring_team.service;

import com.sparta.spring_team.dto.LikeDto;
import com.sparta.spring_team.dto.request.LikeRequestDto;
import com.sparta.spring_team.dto.response.ResponseDto;
import com.sparta.spring_team.entity.*;
import com.sparta.spring_team.repository.CommentRepository;
import com.sparta.spring_team.repository.LikeRepository;
import com.sparta.spring_team.repository.PostRepository;
import com.sparta.spring_team.repository.SubCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class LikeService {
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final SubCommentRepository subCommentRepository;
    @Transactional
    public ResponseDto<?> createLike(LikeRequestDto requestDto, Member member){


        LikeType likeType = LikeType.valueOf(requestDto.getLikeType());
        Post post =null; Comment comment =null; SubComment subComment =null;
        LikeDto<Object> likeDto  = LikeDto.builder()
                .likeType(likeType)
                .build();
        switch (likeType){
            case Post:
                post = postRepository.findById( requestDto.getData()).orElseThrow(()-> new RuntimeException("해당 게시물 없음"));
                likeDto.setData(post);

                break;
            case Comment:
                comment = commentRepository.findById(requestDto.getData()).orElseThrow(()-> new RuntimeException("해당 댓글 없음"));
                likeDto.setData(comment);
                break;
            case SubComment:
                subComment = subCommentRepository.findById(requestDto.getData()).orElseThrow(()-> new RuntimeException("해당 대댓글 없ㄷ음")) ;
                likeDto.setData(subComment);
                break;
        }
        Likes like = likeRepository.findByPostOrCommentOrSubcommentAndTypeAndMember(post,comment,subComment,likeType,member);
        if(like!= null) return ResponseDto.fail("DUPLICATE_LIKES","이미 좋아요를 눌렀습니다");
        return ResponseDto.success(likeRepository.save(new Likes(likeDto, member)));
    }

    @Transactional
    public ResponseDto<?> deleteLike(LikeRequestDto requestDto, Member member) {
        LikeType likeType = LikeType.valueOf(requestDto.getLikeType());
        Post post = null;
        Comment comment = null;
        SubComment subComment = null;
        switch (likeType){
            case Post:
                post = postRepository.findById( requestDto.getData()).orElseThrow(()-> new RuntimeException("해당 게시물 없음"));
                break;
            case Comment:
                comment = commentRepository.findById(requestDto.getData()).orElseThrow(()-> new RuntimeException("해당 댓글 없음"));
                break;
            case SubComment:
                subComment = subCommentRepository.findById(requestDto.getData()).orElseThrow(()-> new RuntimeException("해당 대댓글 없ㄷ음")) ;
                break;
        }
        Likes like = likeRepository.findByPostOrCommentOrSubcommentAndTypeAndMember(post,comment,subComment,likeType,member);
        if(like == null){
            return ResponseDto.fail("INVAILD_LIKES","해당 좋아요가 존재하지 않습니다.");
        }
        likeRepository.delete(like);
        return ResponseDto.success("success delete like");
    }

}
