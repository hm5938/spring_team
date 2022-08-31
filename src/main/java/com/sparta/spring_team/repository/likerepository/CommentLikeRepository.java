package com.sparta.spring_team.repository.likerepository;

import com.sparta.spring_team.entity.Comment;
import com.sparta.spring_team.entity.CommentLike;
import com.sparta.spring_team.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike,Long> {
    Optional<CommentLike> findById(Long id);

    List<CommentLike> findAllByComment(Comment comment);
    List<CommentLike> findAllByMember(Member member);

    Optional<CommentLike> findByCommentAndMember(Comment comment, Member member);
}
