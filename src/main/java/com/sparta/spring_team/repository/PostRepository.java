package com.sparta.spring_team.repository;


import com.sparta.spring_team.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}