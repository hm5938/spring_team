package com.sparta.spring_team.service;


import com.sparta.spring_team.dto.request.PostRequestDto;
import com.sparta.spring_team.dto.response.PostListResponseDto;
import com.sparta.spring_team.dto.response.PostResponseDto;
import com.sparta.spring_team.dto.response.ResponseDto;
import com.sparta.spring_team.entity.Comment;
import com.sparta.spring_team.entity.Member;
import com.sparta.spring_team.entity.Post;
import com.sparta.spring_team.repository.PostRepository;
import com.sparta.spring_team.shared.PublicMethod;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;
    private final PublicMethod publicMethod;

    @Transactional
    public ResponseDto<?> createPost(PostRequestDto requestDto, HttpServletRequest request) {
        ResponseDto<?> result = publicMethod.checkLogin(request);
        if(!result.isSuccess()) return result;
        Member member = (Member) result.getData();
        Post post =postRepository.save(new Post(requestDto, member));
        return ResponseDto.success(post);
    }
    @Transactional
    public ResponseDto<?> updatePost(Long postid,PostRequestDto requestDto, HttpServletRequest request) {
        ResponseDto<?> result = publicMethod.checkLogin(request);
        if(!result.isSuccess()) return result;
        Member member = (Member) result.getData();
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
    public ResponseDto<?> deletePost(Long postid, HttpServletRequest request){
        ResponseDto<?> result = publicMethod.checkLogin(request);
        if(!result.isSuccess()) return result;
        Member member = (Member) result.getData();

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
    @Transactional
    public ResponseDto<?> readAllPosts() {
        //T
        List<Post> posts = postRepository.findAll();
        PostListResponseDto postListResponseDto = new PostListResponseDto();
        return ResponseDto.success(postListResponseDto.getPostList(posts));
    }

    @Transactional
    public ResponseDto<?> readPost(Long postid) {
        Optional<Post> postOptional = postRepository.findById(postid);
        if (postOptional.isEmpty()) {
            return ResponseDto.fail("INVAILD_POST", "존재하지 않는 게시글 입니다");
        }
        Post post = postOptional.get();
        PostResponseDto postResponseDto = PostResponseDto.builder()
                .id(post.getId())
                .memberName(post.getMember().getMembername())
                .title(post.getTitle())
                .content(post.getContent())
                .imageUrl(post.getImageUrl())
                .commentNum((long) post.getComments().size())
                .subCommentNum(getSubCommentNum(post.getComments()))
                .likeNum((long) post.getLikes().size())
                .createdAt(post.getCreatedAt())
                .modifiedAt(post.getModifiedAt())
                .build();

        return ResponseDto.success(postResponseDto);
    }

    public Long getSubCommentNum(List<Comment> commentList) {
        int result = 0;
        for (Comment comment : commentList) {
            result += comment.getSubComments().size();
        }
        return (long) result;
    }
    @Transactional(readOnly = true)
    public Post isPresentPost(Long id){
        Optional<Post> optionalPost = postRepository.findById(id);
        return optionalPost.orElse(null);
    }
}
