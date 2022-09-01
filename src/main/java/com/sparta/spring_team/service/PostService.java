package com.sparta.spring_team.service;


import com.sparta.spring_team.dto.request.PostRequestDto;
import com.sparta.spring_team.dto.response.*;
import com.sparta.spring_team.entity.Comment;
import com.sparta.spring_team.entity.Member;
import com.sparta.spring_team.entity.Post;
import com.sparta.spring_team.entity.SubComment;
import com.sparta.spring_team.repository.PostRepository;
import com.sparta.spring_team.shared.PublicMethod;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.sparta.spring_team.Exception.ErrorCode.*;


@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;
    private final PublicMethod publicMethod;
    private final AwsS3Service imageUploader;

    @Transactional
    public ResponseDto<?> createPost(PostRequestDto requestDto, HttpServletRequest request) {
        ResponseDto<?> result = publicMethod.checkLogin(request);
        if (!result.isSuccess()) return result;
        Member member = (Member) result.getData();

        Post post = postRepository.save(new Post(requestDto, member));
        return ResponseDto.success(post);
    }

    @Transactional
    public ResponseDto<?> createPost(PostRequestDto requestDto, MultipartFile multipartFile, HttpServletRequest request) {
        ResponseDto<?> result = publicMethod.checkLogin(request);
        if (!result.isSuccess()) return result;
        Member member = (Member) result.getData();

        //upload 실패하면 그대로 반환
        ResponseDto<?> imageResult = imageUploader.uploadFile(multipartFile);
        if(null == result.getData()) return imageResult;

        String imageUrl = (String)imageResult.getData();
        requestDto.setImageUrl(imageUrl);

        Post post = postRepository.save(new Post(requestDto, member));
        return ResponseDto.success(post);
    }

    @Transactional
    public ResponseDto<?> updatePost(Long postid, PostRequestDto requestDto, HttpServletRequest request) {
        ResponseDto<?> result = publicMethod.checkLogin(request);
        if (!result.isSuccess()) return result;
        Member member = (Member) result.getData();
        Optional<Post> postOptional = postRepository.findById(postid);
        if (postOptional.isEmpty()) {
            return ResponseDto.fail(INVALID_POST);
        } else if (!postOptional.get().getMember().equals(member)) {
            return ResponseDto.fail(NO_AUTHORIZATION);
        }
        Post post = postOptional.get();
        return ResponseDto.success(post.update(requestDto));
    }

    @Transactional
    public ResponseDto<?> deletePost(Long postid, HttpServletRequest request) {
        ResponseDto<?> result = publicMethod.checkLogin(request);
        if (!result.isSuccess()) return result;
        Member member = (Member) result.getData();

        Optional<Post> postOptional = postRepository.findById(postid);
        if (postOptional.isEmpty()) {
            return ResponseDto.fail(INVALID_POST);
        } else if (!postOptional.get().getMember().equals(member)) {
            return ResponseDto.fail(NO_AUTHORIZATION);
        }
        Post post = postOptional.get();
        postRepository.delete(post);
        return ResponseDto.success("delete post id " + postid);
    }

    @Transactional(readOnly = true)
    public ResponseDto<?> readAllPosts() {
        //T
        List<SimplePostResponseDto> responseList = new ArrayList<>();
        List<Post> posts = postRepository.findAll();

        for (Post post : posts) {
            responseList.add(SimplePostResponseDto.builder()
                    .id(post.getId())
                    .title(post.getTitle())
                    .memberName(post.getMember().getMembername())
                    .likeNum(Long.valueOf(post.getLikes().size()))
                    .commentNum((Long.valueOf(post.getComments().size())))
                    .createdAt(post.getCreatedAt())
                    .modifiedAt(post.getModifiedAt())
                    .build()
            );
        }

        return ResponseDto.success(responseList);
    }

    @Transactional(readOnly = true)
    public ResponseDto<?> readPost(Long postid) {
        Optional<Post> postOptional = postRepository.findById(postid);
        if (postOptional.isEmpty()) {
            return ResponseDto.fail(INVALID_POST);
        }
        Post post = postOptional.get();

        //댓글 목록
        List<CommentResponseDto> responseList = new ArrayList<>();
        List<Comment> comments = post.getComments();

        for (Comment comment : comments) {
            //대댓글 목록
            List<SubCommentResponseDto> subResponseList = new ArrayList<>();
            List<SubComment> subComments = comment.getSubComments();

            for (SubComment subComment : subComments) {
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

        PostResponseDto postResponseDto = PostResponseDto.builder()
                .id(post.getId())
                .memberName(post.getMember().getMembername())
                .title(post.getTitle())
                .content(post.getContent())
                .imageUrl(post.getImageUrl())
                .commentsList(responseList)
                .likeNum(Long.valueOf(post.getLikes().size()))
                .createdAt(post.getCreatedAt())
                .modifiedAt(post.getModifiedAt())
                .build();

        return ResponseDto.success(postResponseDto);
    }

    @Transactional(readOnly = true)
    public ResponseDto<?> readAllPostsByMember(Member member) {
        List<PostMypageResponseDto> responseList = new ArrayList<>();
        List<Post> posts = postRepository.findAllByMember(member);

        for (Post post : posts) {
            responseList.add(PostMypageResponseDto.builder()
                    .id(post.getId())
                    .membername(member.getMembername())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .imageUrl(post.getImageUrl())
                    .likeNum(Long.valueOf(post.getLikes().size()))
                    .createdAt(post.getCreatedAt())
                    .modifiedAt(post.getModifiedAt())
                    .build()
            );
        }

        return ResponseDto.success(responseList);
    }

    @Transactional(readOnly = true)
    public ResponseDto<?> getPostById(Long postid) {
        Optional<Post> postOptional = postRepository.findById(postid);
        if (postOptional.isEmpty()) {
            return ResponseDto.fail(INVALID_POST);
        }
        Post post = postOptional.get();

        return ResponseDto.success(PostMypageResponseDto.builder()
                .id(post.getId())
                .membername(post.getMember().getMembername())
                .title(post.getTitle())
                .content(post.getContent())
                .imageUrl(post.getImageUrl())
                .likeNum(Long.valueOf(post.getLikes().size()))
                .createdAt(post.getCreatedAt())
                .modifiedAt(post.getModifiedAt())
                .build());
    }

    @Transactional(readOnly = true)
    public Post isPresentPost(Long id) {
        Optional<Post> optionalPost = postRepository.findById(id);
        return optionalPost.orElse(null);
    }
}
