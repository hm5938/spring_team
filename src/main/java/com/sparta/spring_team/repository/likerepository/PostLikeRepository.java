package com.sparta.spring_team.repository.likerepository;

import com.sparta.spring_team.entity.Member;
import com.sparta.spring_team.entity.Post;
import com.sparta.spring_team.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike,Long> {
    Optional<PostLike> findById(Long id);

    List<PostLike> findAllByPost(Post post);
    List<PostLike> findAllByMember(Member member);

    Optional<PostLike> findByPostAndMember(Post post, Member member);
}
