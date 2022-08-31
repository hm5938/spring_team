package com.sparta.spring_team.repository;


import com.sparta.spring_team.entity.Member;
import com.sparta.spring_team.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findById(Long id);

    List<Post> findAllByMember(Member member);
}