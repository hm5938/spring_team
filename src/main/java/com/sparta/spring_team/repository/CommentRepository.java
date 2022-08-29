package com.sparta.spring_team.repository;

import com.sparta.spring_team.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment,Long> {
}
