package com.sparta.spring_team.repository;


import com.sparta.spring_team.entity.Comment;
import com.sparta.spring_team.entity.SubComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubCommentRepository extends JpaRepository<SubComment, Long> {
    Optional<SubComment> findById(Long id);

    List<SubComment> findAllByComment(Comment comment);
}
