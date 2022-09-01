package com.sparta.spring_team.service;

import com.sparta.spring_team.dto.request.LikeRequestDto;
import com.sparta.spring_team.dto.response.CommentMypageResponseDto;
import com.sparta.spring_team.dto.response.LikeResponseDto;
import com.sparta.spring_team.dto.response.PostMypageResponseDto;
import com.sparta.spring_team.dto.response.ResponseDto;
import com.sparta.spring_team.entity.*;
import com.sparta.spring_team.repository.likerepository.CommentLikeRepository;
import com.sparta.spring_team.repository.likerepository.PostLikeRepository;
import com.sparta.spring_team.repository.likerepository.SubCommentLikeRepository;
import com.sparta.spring_team.shared.LikeType;
import com.sparta.spring_team.shared.PublicMethod;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.sparta.spring_team.Exception.ErrorCode.*;

@RequiredArgsConstructor
@Service
public class LikeService {
    private final PostLikeRepository postLikeRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final SubCommentLikeRepository subCommentLikeRepository;


    private final PublicMethod publicMethod;
    private final PostService postService;
    private final CommentService commentService;
    private final SubCommentService subCommentService;

    @Transactional
    public ResponseDto<?> createLike(LikeRequestDto requestDto, HttpServletRequest request){
        ResponseDto result = publicMethod.checkLogin(request);
        if(!result.isSuccess()) return result;
        Member member = (Member)result.getData();

        LikeType type = requestDto.getType();
        switch(type){
            case POST:
                Post post = postService.isPresentPost(requestDto.getId());
                if (null == post) {
                    return ResponseDto.fail(INVALID_POST);
                }
                Optional<PostLike> postLike = postLikeRepository.findByPostAndMember(post, member);

                if(postLike.isPresent()) return ResponseDto.fail(DUPLICATE_LIKES);

                //저장
                PostLike postLikeObject = PostLike.builder()
                        .member(member)
                        .post(post)
                        .build();
                postLikeRepository.save(postLikeObject);

                return ResponseDto.success(LikeResponseDto.builder()
                        .id(postLikeObject.getId())
                        .membername(postLikeObject.getMember().getMembername())
                        .type(LikeType.POST)
                        .typeId(postLikeObject.getPost().getId())
                        .build());

            case COMMENT:
                Comment comment = commentService.isPresentComment(requestDto.getId());
                if (null == comment) {
                    return ResponseDto.fail(INVALID_COMMENT);
                }
                Optional<CommentLike> commentLike = commentLikeRepository.findByCommentAndMember(comment, member);

                if(commentLike.isPresent()) return ResponseDto.fail(DUPLICATE_LIKES);

                //저장
                CommentLike commentLikeObject = CommentLike.builder()
                        .member(member)
                        .comment(comment)
                        .build();
                commentLikeRepository.save(commentLikeObject);

                return ResponseDto.success(LikeResponseDto.builder()
                        .id(commentLikeObject.getId())
                        .membername(commentLikeObject.getMember().getMembername())
                        .type(LikeType.COMMENT)
                        .typeId(commentLikeObject.getComment().getId())
                        .build());

            case SUBCOMMENT:
                SubComment subComment = subCommentService.isPresentSubComment(requestDto.getId());
                if (null == subComment) {
                    return ResponseDto.fail(INVALID_SUBCOMMENT);
                }
                Optional<SubCommentLike> subCommentLike = subCommentLikeRepository.findBySubcommentAndMember(subComment, member);

                if(subCommentLike.isPresent()) return ResponseDto.fail(DUPLICATE_LIKES);

                //저장
                SubCommentLike subCommentLikeObject = SubCommentLike.builder()
                        .member(member)
                        .subcomment(subComment)
                        .build();
                subCommentLikeRepository.save(subCommentLikeObject);

                return ResponseDto.success(LikeResponseDto.builder()
                        .id(subCommentLikeObject.getId())
                        .membername(subCommentLikeObject.getMember().getMembername())
                        .type(LikeType.SUBCOMMENT)
                        .typeId(subCommentLikeObject.getSubcomment().getId())
                        .build());

            default:
                return ResponseDto.fail(WRONG_DATA);
        }

    }
    @Transactional
    public ResponseDto<?> deleteLike(LikeRequestDto requestDto, HttpServletRequest request) {
        ResponseDto result = publicMethod.checkLogin(request);
        if(!result.isSuccess()) return result;
        Member member = (Member) result.getData();

        LikeType type = requestDto.getType();
        switch(type) {
            case POST:
                Post post = postService.isPresentPost(requestDto.getId());
                if (null == post) {
                    return ResponseDto.fail(INVALID_POST);
                }
                Optional<PostLike> postLike = postLikeRepository.findByPostAndMember(post, member);

                if (postLike.isPresent()) {
                    postLikeRepository.delete(postLike.get());
                    return ResponseDto.success("success delete like");
                }
                return ResponseDto.fail(INVALID_LIKES);

            case COMMENT:
                Comment comment = commentService.isPresentComment(requestDto.getId());
                if (null == comment) {
                    return ResponseDto.fail(INVALID_COMMENT);
                }
                Optional<CommentLike> commentLike = commentLikeRepository.findByCommentAndMember(comment, member);

                if (commentLike.isPresent()) {
                    commentLikeRepository.delete(commentLike.get());
                    return ResponseDto.success("success delete like");
                }
                return ResponseDto.fail(INVALID_LIKES);

            case SUBCOMMENT:
                SubComment subComment = subCommentService.isPresentSubComment(requestDto.getId());
                if (null == subComment) {
                    return ResponseDto.fail(INVALID_SUBCOMMENT);
                }
                Optional<SubCommentLike> subCommentLike = subCommentLikeRepository.findBySubcommentAndMember(subComment, member);

                if (subCommentLike.isPresent()) {
                    subCommentLikeRepository.delete(subCommentLike.get());
                    return ResponseDto.success("success delete like");
                }
                return ResponseDto.fail(INVALID_LIKES);

            default:
                return ResponseDto.fail(WRONG_DATA);
        }
    }

    @Transactional(readOnly = true)
    public ResponseDto<?> getAllPostLikesByMember(Member member){
        List<PostMypageResponseDto> responseList = new ArrayList<>();
        List<PostLike> postLikes = postLikeRepository.findAllByMember(member);

        for(PostLike postLike : postLikes){
            ResponseDto postResponse = postService.getPostById(postLike.getPost().getId());
            PostMypageResponseDto data = (PostMypageResponseDto)postResponse.getData();
            responseList.add(data);
        }

        return ResponseDto.success(responseList);
    }

    @Transactional(readOnly = true)
    public ResponseDto<?> getAllCommentLikesByMember(Member member){
        List<CommentMypageResponseDto> responseList = new ArrayList<>();
        List<CommentLike> commentLikes = commentLikeRepository.findAllByMember(member);

        for(CommentLike commentLike : commentLikes){
            ResponseDto commentResponse = commentService.getCommentById(commentLike.getComment().getId());
            CommentMypageResponseDto data = (CommentMypageResponseDto)commentResponse.getData();
            responseList.add(data);
        }

        return ResponseDto.success(responseList);
    }

    @Transactional(readOnly = true)
    public ResponseDto<?> getAllSubCommentLikesByMember(Member member){
        List<CommentMypageResponseDto> responseList = new ArrayList<>();
        List<SubCommentLike> subCommentLikes = subCommentLikeRepository.findAllByMember(member);

        for(SubCommentLike subCommentLike : subCommentLikes){
            ResponseDto subCommentResponse = subCommentService.getSubCommentById(subCommentLike.getSubcomment().getId());
            CommentMypageResponseDto data = (CommentMypageResponseDto)subCommentResponse.getData();
            responseList.add(data);
        }

        return ResponseDto.success(responseList);
    }


//    @Transactional(readOnly = true)
//    public PostLike isPresentPostLike(Long id){
//        Optional<PostLike> optionalLike = postLikeRepository.findById(id);
//        return optionalLike.orElse(null);
//    }
//    @Transactional(readOnly = true)
//    public CommentLike isPresentCommentLike(Long id){
//        Optional<CommentLike> optionalLike = commentLikeRepository.findById(id);
//        return optionalLike.orElse(null);
//    }
//    @Transactional(readOnly = true)
//    public SubCommentLike isPresentSubCommentLike(Long id){
//        Optional<SubCommentLike> optionalLike = subCommentLikeRepository.findById(id);
//        return optionalLike.orElse(null);
//    }
}
