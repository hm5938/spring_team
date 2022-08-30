package com.sparta.spring_team.repository;

import com.sparta.spring_team.entity.Comment;
import com.sparta.spring_team.entity.Member;
import com.sparta.spring_team.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    Optional<Comment> findById(Long id);

    List<Comment> findAllByPost(Post post);
    List<Comment> findAllByMember(Member member);
}
