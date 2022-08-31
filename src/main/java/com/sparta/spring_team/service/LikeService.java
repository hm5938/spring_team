package com.sparta.spring_team.service;

import com.sparta.spring_team.dto.request.LikeRequestDto;
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
import java.util.Optional;

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
                    return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 입니다.");
                }
                Optional<PostLike> postLike = postLikeRepository.findbyPostAndMember(post, member);

                if(postLike.isPresent()) return ResponseDto.fail("DUPLICATE_LIKES", "이미 좋아요를 눌렀습니다");
                return ResponseDto.success(postLikeRepository.save(PostLike.builder()
                        .member(member)
                        .post(post)
                        .build()));

            case COMMENT:
                Comment comment = commentService.isPresentComment(requestDto.getId());
                if (null == comment) {
                    return ResponseDto.fail("NOT_FOUND", "존재하지 않는 댓글 입니다.");
                }
                Optional<CommentLike> commentLike = commentLikeRepository.findByCommentAndMember(comment, member);

                if(commentLike.isPresent()) return ResponseDto.fail("DUPLICATE_LIKES", "이미 좋아요를 눌렀습니다");
                return ResponseDto.success(commentLikeRepository.save(CommentLike.builder()
                                .member(member)
                                .comment(comment)
                                .build()));

            case SUBCOMMENT:
                SubComment subComment = subCommentService.isPresentSubComment(requestDto.getId());
                if (null == subComment) {
                    return ResponseDto.fail("NOT_FOUND", "존재하지 않는 대댓글 입니다.");
                }
                Optional<SubCommentLike> subCommentLike = subCommentLikeRepository.findBySubCommentAndMemeber(subComment, member);

                if(subCommentLike.isPresent()) return ResponseDto.fail("DUPLICATE_LIKES", "이미 좋아요를 눌렀습니다");
                return ResponseDto.success(subCommentLikeRepository.save(SubCommentLike.builder()
                        .member(member)
                        .subcomment(subComment)
                        .build()));
            default:
                return ResponseDto.fail("WRONG_DATA", "데이터가 잘못되었습니다.");
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
                    return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 입니다.");
                }
                Optional<PostLike> postLike = postLikeRepository.findbyPostAndMember(post, member);

                if (postLike.isPresent()) {
                    postLikeRepository.delete(postLike.get());
                    return ResponseDto.success("success delete like");
                }
                return ResponseDto.fail("INVAILD_LIKES", "좋아요를 누르지 않았습니다.");

            case COMMENT:
                Comment comment = commentService.isPresentComment(requestDto.getId());
                if (null == comment) {
                    return ResponseDto.fail("NOT_FOUND", "존재하지 않는 댓글 입니다.");
                }
                Optional<CommentLike> commentLike = commentLikeRepository.findByCommentAndMember(comment, member);

                if (commentLike.isPresent()) {
                    commentLikeRepository.delete(commentLike.get());
                    return ResponseDto.success("success delete like");
                }
                return ResponseDto.fail("INVAILD_LIKES", "좋아요를 누르지 않았습니다.");

            case SUBCOMMENT:
                SubComment subComment = subCommentService.isPresentSubComment(requestDto.getId());
                if (null == subComment) {
                    return ResponseDto.fail("NOT_FOUND", "존재하지 않는 대댓글 입니다.");
                }
                Optional<SubCommentLike> subCommentLike = subCommentLikeRepository.findBySubCommentAndMemeber(subComment, member);

                if (subCommentLike.isPresent()) {
                    subCommentLikeRepository.delete(subCommentLike.get());
                    return ResponseDto.success("success delete like");
                }
                return ResponseDto.fail("INVAILD_LIKES", "좋아요를 누르지 않았습니다.");

            default:
                return ResponseDto.fail("WRONG_DATA", "데이터가 잘못되었습니다.");
        }
    }


    @Transactional(readOnly = true)
    public PostLike isPresentPostLike(Long id){
        Optional<PostLike> optionalLike = postLikeRepository.findById(id);
        return optionalLike.orElse(null);
    }
    @Transactional(readOnly = true)
    public CommentLike isPresentCommentLike(Long id){
        Optional<CommentLike> optionalLike = commentLikeRepository.findById(id);
        return optionalLike.orElse(null);
    }
    @Transactional(readOnly = true)
    public SubCommentLike isPresentSubCommentLike(Long id){
        Optional<SubCommentLike> optionalLike = subCommentLikeRepository.findById(id);
        return optionalLike.orElse(null);
    }

//    @Transactional(readOnly = true)
//    public Likes isPresentLike(Post post, Comment comment, SubComment subComment, LikeType likeType, Member member){
//        return likeRepository.findByPostOrCommentOrSubcommentAndTypeAndMember(post,comment,subComment,likeType,member);
//    }
}
