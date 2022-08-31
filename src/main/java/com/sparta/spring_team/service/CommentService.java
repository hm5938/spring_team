package com.sparta.spring_team.service;

import com.sparta.spring_team.dto.request.CommentRequestDto;
import com.sparta.spring_team.dto.response.*;
import com.sparta.spring_team.entity.Comment;
import com.sparta.spring_team.entity.Member;
import com.sparta.spring_team.entity.Post;
import com.sparta.spring_team.entity.SubComment;
import com.sparta.spring_team.repository.CommentRepository;
import com.sparta.spring_team.shared.PublicMethod;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final PublicMethod publicMethod;

    private final PostService postService;
    //private final SubCommentService subCommentService;

    @Transactional
    public ResponseDto<?> createComment(CommentRequestDto requestDto, HttpServletRequest request) {
        ResponseDto result = publicMethod.checkLogin(request);
        if(!result.isSuccess()) return result;

        Member member = (Member)result.getData();

        //포스트
        Post post = postService.isPresentPost(requestDto.getPostId());
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글입니다.");
        }

        Comment comment = Comment.builder()
                .member(member)
                .post(post)
                .content(requestDto.getContent())
                .build();

        commentRepository.save(comment);
        return ResponseDto.success(comment);
    }

    @Transactional(readOnly = true)
    public ResponseDto<?> getAllCommentsByPost(Long postId) {
        //포스트
        Post post = postService.isPresentPost(postId);
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글입니다.");
        }

        //댓글 목록
        List<CommentResponseDto> responseList = new ArrayList<>();
        List<Comment> comments = commentRepository.findAllByPost(post);

        for (Comment comment : comments) {
            //대댓글 목록
            List<SubCommentResponseDto> subResponseList = new ArrayList<>();
            List<SubComment> subComments = comment.getSubComments();

            for(SubComment subComment : subComments){
                subResponseList.add(SubCommentResponseDto.builder()
                                .id(subComment.getId())
                                .membername(subComment.getMember().getMembername())
                                .content(subComment.getContent())
                                .likeNum(Long.valueOf(subComment.getLikes().size()))
                                .createdAt(comment.getCreatedAt())
                                .modifiedAt(comment.getModifiedAt())
                                .build()
                );
            }

            responseList.add(
                    CommentResponseDto.builder()
                            .id(comment.getId())
                            .membername(comment.getMember().getMembername())
                            .content(comment.getContent())
                            .subCommentsList(subResponseList)
                            .likeNum(Long.valueOf(comment.getLikes().size()))
                            .createdAt(comment.getCreatedAt())
                            .modifiedAt(comment.getModifiedAt())
                            .build()
            );
        }
        return ResponseDto.success(responseList);
    }

    @Transactional(readOnly = true)
    public ResponseDto<?> getAllCommentsByMember(Member member) {
        List<CommentMypageResponseDto> responseList = new ArrayList<>();
        List<Comment> commentsList= commentRepository.findAllByMember(member);

        for(Comment comment : commentsList){
            responseList.add(CommentMypageResponseDto.builder()
                            .id(comment.getId())
                            .content(comment.getContent())
                            .likeNum(Long.valueOf(comment.getLikes().size()))
                            .createdAt(comment.getCreatedAt())
                            .modifiedAt(comment.getModifiedAt())
                            .build());
        }
        //멤버 댓글 목록
        return ResponseDto.success(responseList);
    }

    @Transactional(readOnly = true)
    public ResponseDto<?> getCommentById(Long commentId) {
        Optional<Comment> commentOptional = commentRepository.findById(commentId);
        if(commentOptional.isEmpty()){
            return ResponseDto.fail("INVAILD_COMMENT", "존재하지 않는 댓글 입니다.");
        }
        Comment comment = commentOptional.get();

        return ResponseDto.success(CommentLikeMypageResponseDto.builder()
                .id(comment.getId())
                .membername(comment.getMember().getMembername())
                .content(comment.getContent())
                .likeNum(Long.valueOf(comment.getLikes().size()))
                .createdAt(comment.getCreatedAt())
                .modifiedAt(comment.getModifiedAt())
                .build());
    }

    @Transactional
    public ResponseDto<?> updateComment(Long id, CommentRequestDto requestDto, HttpServletRequest request) {
        ResponseDto result = publicMethod.checkLogin(request);
        if(!result.isSuccess()) return result;

        Member member = (Member)result.getData();

        Comment comment = isPresentComment(id);
        if (null == comment) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 댓글 입니다.");
        }

        if (comment.validateMember(member)) {
            return ResponseDto.fail("BAD_REQUEST", "작성자만 수정할 수 있습니다.");
        }

        return ResponseDto.success(comment.update(requestDto));
    }

    @Transactional
    public ResponseDto<?> deleteComment(Long id, HttpServletRequest request){
        ResponseDto result = publicMethod.checkLogin(request);
        if(!result.isSuccess()) return result;

        Member member = (Member)result.getData();

        Comment comment = isPresentComment(id);
        if (null == comment) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 댓글 입니다.");
        }

        if (comment.validateMember(member)) {
            return ResponseDto.fail("BAD_REQUEST", "작성자만 삭제할 수 있습니다.");
        }

        commentRepository.delete(comment);
        return ResponseDto.success("delete comment ID."+id);
    }

    @Transactional(readOnly = true)
    public Comment isPresentComment(Long id){
        Optional<Comment> optionalComment = commentRepository.findById(id);
        return optionalComment.orElse(null);
    }
}
