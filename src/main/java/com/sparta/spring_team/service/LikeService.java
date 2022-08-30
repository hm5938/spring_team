package com.sparta.spring_team.service;

import com.sparta.spring_team.dto.LikeDto;
import com.sparta.spring_team.dto.request.LikeRequestDto;
import com.sparta.spring_team.dto.response.ResponseDto;
import com.sparta.spring_team.entity.*;
import com.sparta.spring_team.repository.CommentRepository;
import com.sparta.spring_team.repository.LikeRepository;
import com.sparta.spring_team.repository.PostRepository;
import com.sparta.spring_team.repository.SubCommentRepository;
import com.sparta.spring_team.shared.PublicMethod;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class LikeService {
    private final LikeRepository likeRepository;
    private final PublicMethod publicMethod;
    private final PostService postService;
    private final CommentService commentService;
    private final SubCommentService subCommentService;
    @Transactional
    public ResponseDto<?> createLike(LikeRequestDto requestDto, HttpServletRequest request){
        ResponseDto result = publicMethod.checkLogin(request);
        if(!result.isSuccess()) return result;
        Member member = (Member) result.getData();

        LikeType likeType = LikeType.valueOf(requestDto.getLikeType());
        Post post =null; Comment comment =null; SubComment subComment =null;
        LikeDto<Object> likeDto  = LikeDto.builder()
                .likeType(likeType)
                .build();
        switch (likeType){
            case Post:
                post = postService.isPresentPost(requestDto.getData());
                likeDto.setData(post);
                break;
            case Comment:
                comment = commentService.isPresentComment(requestDto.getData());
                likeDto.setData(comment);
                break;
            case SubComment:
                subComment = subCommentService.isPresentSubComment(requestDto.getData());
                likeDto.setData(subComment);
                break;
        }
        Likes like = likeRepository.findByPostOrCommentOrSubcommentAndTypeAndMember(post,comment,subComment,likeType,member);
        if(like!= null) return ResponseDto.fail("DUPLICATE_LIKES","이미 좋아요를 눌렀습니다");
        return ResponseDto.success(likeRepository.save(new Likes(likeDto, member)));
    }

    @Transactional
    public ResponseDto<?> deleteLike(LikeRequestDto requestDto, HttpServletRequest request) {
        ResponseDto result = publicMethod.checkLogin(request);
        if(!result.isSuccess()) return result;
        Member member = (Member) result.getData();

        LikeType likeType = LikeType.valueOf(requestDto.getLikeType());
        Post post = null;
        Comment comment = null;
        SubComment subComment = null;
        switch (likeType){
            case Post:
                post = postService.isPresentPost(requestDto.getData());
                break;
            case Comment:
                comment = commentService.isPresentComment(requestDto.getData());
                break;
            case SubComment:
                subComment = subCommentService.isPresentSubComment(requestDto.getData());
                break;
        }
        Likes like = isPresentLike(post,comment,subComment,likeType,member);

        if(like == null){
            return ResponseDto.fail("INVAILD_LIKES","해당 좋아요가 존재하지 않습니다.");
        }

        likeRepository.delete(like);
        return ResponseDto.success("success delete like");
    }


    @Transactional(readOnly = true)
    public Likes isPresentLike(Long id){
        Optional<Likes> optionalLike = likeRepository.findById(id);
        return optionalLike.orElse(null);
    }

    @Transactional(readOnly = true)
    public Likes isPresentLike(Post post, Comment comment, SubComment subComment, LikeType likeType, Member member){
        return likeRepository.findByPostOrCommentOrSubcommentAndTypeAndMember(post,comment,subComment,likeType,member);
    }
}
