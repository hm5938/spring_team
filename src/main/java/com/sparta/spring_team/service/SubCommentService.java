package com.sparta.spring_team.service;

import com.sparta.spring_team.dto.request.SubCommentRequestDto;
import com.sparta.spring_team.dto.response.ResponseDto;
import com.sparta.spring_team.entity.Comment;
import com.sparta.spring_team.entity.Member;
import com.sparta.spring_team.entity.SubComment;
import com.sparta.spring_team.repository.SubCommentRepository;
import com.sparta.spring_team.shared.PublicMethod;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

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
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 댓글입니다.");
        }

        SubComment subComment = SubComment.builder()
                .member(member)
                .comment(comment)
                .content(requestDto.getContent())
                .likeNum(Long.valueOf(0))
                .build();
        subCommentRepository.save(subComment);
        return ResponseDto.success(subComment);
    }

    @Transactional(readOnly = true)
    public ResponseDto<?> getAllSubCommentsByComment(Long commentId){
        Comment comment = commentService.isPresentComment(commentId);
        if (null == comment) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 댓글입니다.");
        }

        List<SubComment> subCommentList = subCommentRepository.findAllByComment(comment);
        return ResponseDto.success(subCommentList);
    }

    @Transactional(readOnly = true)
    public ResponseDto<?> getAllSubCommentsByMember(Member member){
        return ResponseDto.success(subCommentRepository.findAllByMember(member));
    }

    @Transactional
    public ResponseDto<?> updateSubComment(Long id, SubCommentRequestDto requestDto, HttpServletRequest request) {
        ResponseDto result = publicMethod.checkLogin(request);
        if(!result.isSuccess()) return result;

        Member member = (Member)result.getData();

        SubComment subComment = isPresentSubComment(id);
        if (null == subComment) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 대댓글입니다.");
        }

        if (subComment.validateMember(member)) {
            return ResponseDto.fail("BAD_REQUEST", "작성자만 수정할 수 있습니다.");
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
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 대댓글입니다.");
        }

        if (subComment.validateMember(member)) {
            return ResponseDto.fail("BAD_REQUEST", "작성자만 수정할 수 있습니다.");
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
