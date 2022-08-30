package com.sparta.spring_team.repository;



import com.sparta.spring_team.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Likes, Long> {
    Likes findByPostOrCommentOrSubcommentAndTypeAndMember(Post post, Comment comment, SubComment subComment, LikeType likeType, Member member);
}
