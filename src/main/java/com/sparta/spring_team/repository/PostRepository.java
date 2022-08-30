package com.sparta.spring_team.repository;


import com.sparta.spring_team.entity.Member;
import com.sparta.spring_team.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByMember(Member member);
}