package com.sparta.spring_team.repository;



import com.sparta.spring_team.entity.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Likes, Long> {
}
