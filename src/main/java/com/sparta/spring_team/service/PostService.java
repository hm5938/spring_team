package com.sparta.spring_team.service;


import com.sparta.spring_team.dto.request.PostRequestDto;
import com.sparta.spring_team.dto.response.ResponseDto;
import com.sparta.spring_team.entity.Member;
import com.sparta.spring_team.entity.Post;
import com.sparta.spring_team.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;

    @Transactional
    public ResponseDto<?> createPost(PostRequestDto requestDto, Member member) {
        Post post =postRepository.save(new Post(requestDto, member));
        return ResponseDto.success(post);
    }
    @Transactional
    public ResponseDto<?> updatePost(Long postid,PostRequestDto requestDto, Member member) {
        Optional<Post> postOptional = postRepository.findById(postid);
        if(postOptional.isEmpty()){
            return ResponseDto.fail("INVAILD_POST","존재하기 않는 게시글 입니다");
        }else if(!postOptional.get().getMember().equals(member)){
            return ResponseDto.fail("NOT_POSTING_USER","게시글 작성자가 아닙니다.");
        }
        Post post = postOptional.get();
        return ResponseDto.success(post.update(requestDto));
    }

    @Transactional
    public ResponseDto<?> deletePost(Long postid, Member member){
        Optional<Post> postOptional = postRepository.findById(postid);
        if(postOptional.isEmpty()){
            return ResponseDto.fail("INVAILD_POST","존재하기 않는 게시글 입니다");
        }else if(!postOptional.get().getMember().equals(member)){
            return ResponseDto.fail("NOT_POSTING_USER","게시글 작성자가 아닙니다.");
        }
        Post post = postOptional.get();
        postRepository.delete(post);
        return ResponseDto.success("delete post id "+postid);
    }
}
