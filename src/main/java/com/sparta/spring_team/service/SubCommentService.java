package com.sparta.spring_team.service;

import com.sparta.spring_team.dto.request.SubCommentRequestDto;
import com.sparta.spring_team.dto.response.CommentMypageResponseDto;
import com.sparta.spring_team.dto.response.ResponseDto;
import com.sparta.spring_team.dto.response.SubCommentResponseDto;
import com.sparta.spring_team.entity.Comment;
import com.sparta.spring_team.entity.Member;
import com.sparta.spring_team.entity.SubComment;
import com.sparta.spring_team.repository.SubCommentRepository;
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
public class SubCommentService {
    private final SubCommentRepository subCommentRepository;
    private final PublicMethod publicMethod;
    private final CommentService commentService;

    @Transactional
    public ResponseDto<?> createSubComment(SubCommentRequestDto requestDto, HttpServletRequest request) {
        ResponseDto result = publicMethod.checkLogin(request);
        if(!result.isSuccess()) return result;

        Member member = (Member)result.getData();

        Comment comment = commentService.isPresentComment(requestDto.getCommentId());
        if (null == comment) {
            return ResponseDto.fail(INVALID_COMMENT);
        }

        SubComment subComment = SubComment.builder()
                .member(member)
                .comment(comment)
                .content(requestDto.getContent())
                .build();
        subCommentRepository.save(subComment);
        return ResponseDto.success(subComment);
    }

    @Transactional(readOnly = true)
    public ResponseDto<?> getAllSubCommentsByComment(Long commentId){
        Comment comment = commentService.isPresentComment(commentId);
        if (null == comment) {
            return ResponseDto.fail(INVALID_COMMENT);
        }

        //댓글 목록
        List<SubCommentResponseDto> responseList = new ArrayList<>();
        List<SubComment> subCommentsList= subCommentRepository.findAllByComment(comment);

        for (SubComment subComment : subCommentsList) {
            responseList.add(
                    SubCommentResponseDto.builder()
                            .id(subComment.getId())
                            .membername(subComment.getMember().getMembername())
                            .content(subComment.getContent())
                            .likeNum(Long.valueOf(subComment.getLikes().size()))
                            .createdAt(subComment.getCreatedAt())
                            .modifiedAt(subComment.getModifiedAt())
                            .build()
            );
        }
        return ResponseDto.success(responseList);
    }

    @Transactional(readOnly = true)
    public ResponseDto<?> getAllSubCommentsByMember(Member member){
        //댓글 목록
        List<SubCommentResponseDto> responseList = new ArrayList<>();
        List<SubComment> subCommentsList= subCommentRepository.findAllByMember(member);

        for (SubComment subComment : subCommentsList) {
            responseList.add(
                    SubCommentResponseDto.builder()
                            .id(subComment.getId())
                            .membername(subComment.getMember().getMembername())
                            .content(subComment.getContent())
                            .likeNum(Long.valueOf(subComment.getLikes().size()))
                            .createdAt(subComment.getCreatedAt())
                            .modifiedAt(subComment.getModifiedAt())
                            .build()
            );
        }
        return ResponseDto.success(responseList);
    }

    @Transactional(readOnly = true)
    public ResponseDto<?> getSubCommentById(Long subCommentId) {
        Optional<SubComment> subCommentOptional = subCommentRepository.findById(subCommentId);
        if(subCommentOptional.isEmpty()){
            return ResponseDto.fail(INVALID_SUBCOMMENT);
        }
        SubComment subComment = subCommentOptional.get();

        return ResponseDto.success(CommentMypageResponseDto.builder()
                .id(subComment.getId())
                .membername(subComment.getMember().getMembername())
                .content(subComment.getContent())
                .likeNum(Long.valueOf(subComment.getLikes().size()))
                .createdAt(subComment.getCreatedAt())
                .modifiedAt(subComment.getModifiedAt())
                .build());
    }

    @Transactional
    public ResponseDto<?> updateSubComment(Long id, SubCommentRequestDto requestDto, HttpServletRequest request) {
        ResponseDto result = publicMethod.checkLogin(request);
        if(!result.isSuccess()) return result;

        Member member = (Member)result.getData();

        SubComment subComment = isPresentSubComment(id);
        if (null == subComment) {
            return ResponseDto.fail(INVALID_SUBCOMMENT);
        }

        if (subComment.validateMember(member)) {
            return ResponseDto.fail(NO_AUTHORIZATION);
        }

        return ResponseDto.success(subComment.update(requestDto));
    }

    @Transactional
    public ResponseDto<?> deleteSubComment(Long id, HttpServletRequest request){
        ResponseDto result = publicMethod.checkLogin(request);
        if(!result.isSuccess()) return result;

        Member member = (Member)result.getData();

        SubComment subComment = isPresentSubComment(id);
        if (null == subComment) {
            return ResponseDto.fail(INVALID_SUBCOMMENT);
        }

        if (subComment.validateMember(member)) {
            return ResponseDto.fail(NO_AUTHORIZATION);
        }

        subCommentRepository.delete(subComment);
        return ResponseDto.success("delete subcomment ID."+id);
    }

    @Transactional(readOnly = true)
    public SubComment isPresentSubComment(Long id){
        Optional<SubComment> optionalSubComment = subCommentRepository.findById(id);
        return optionalSubComment.orElse(null);
    }
}
