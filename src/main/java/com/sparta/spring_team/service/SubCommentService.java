package com.sparta.spring_team.service;

import com.sparta.spring_team.dto.request.SubCommentRequestDto;
import com.sparta.spring_team.dto.response.ResponseDto;
import com.sparta.spring_team.entity.Member;
import com.sparta.spring_team.entity.SubComment;
import com.sparta.spring_team.repository.SubCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SubCommentService {
    private final SubCommentRepository subCommentRepository;

    @Transactional
    public ResponseDto<?> createSubComment(SubCommentRequestDto requestDto, Member member) {
        SubComment subComment = subCommentRepository.save(new SubComment(requestDto, member));
        return ResponseDto.success(subComment);
    }

    @Transactional
    public ResponseDto<?> updateSubComment(Long subCommentId, SubCommentRequestDto requestDto, Member member) {
        Optional<SubComment> subCommentOptional = subCommentRepository.findById(subCommentId);
        if(subCommentOptional.isEmpty()){
            return ResponseDto.fail("INVAILD_SUBCOMMENT","존재하기 않는 대댓글 입니다");
        }else if(!subCommentOptional.get().getMember().equals(member)){
            return ResponseDto.fail("NOT_SUBCOMMENTING_USER","대댓글 작성자가 아닙니다.");
        }
        SubComment subComment = subCommentOptional.get();
        return ResponseDto.success(subComment.update(requestDto));
    }

    @Transactional
    public ResponseDto<?> deleteSubComment(Long subCommentId, Member member){
        Optional<SubComment> subCommentOptional = subCommentRepository.findById(subCommentId);
        if(subCommentOptional.isEmpty()){
            return ResponseDto.fail("INVAILD_SUBCOMMENT","존재하기 않는 대댓글 입니다");
        }else if(!subCommentOptional.get().getMember().equals(member)){
            return ResponseDto.fail("NOT_SUBCOMMENTING_USER","대댓글 작성자가 아닙니다.");
        }
        SubComment subComment = subCommentOptional.get();
        subCommentRepository.delete(subComment);
        return ResponseDto.success("delete subcomment id "+ subCommentId);
    }
}
