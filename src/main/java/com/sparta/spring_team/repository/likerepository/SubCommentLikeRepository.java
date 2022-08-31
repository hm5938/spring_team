package com.sparta.spring_team.repository.likerepository;

import com.sparta.spring_team.entity.Member;
import com.sparta.spring_team.entity.SubComment;
import com.sparta.spring_team.entity.SubCommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubCommentLikeRepository extends JpaRepository<SubCommentLike,Long> {
    Optional<SubCommentLike> findById(Long id);

    List<SubCommentLike> findAllBySubcomment(SubComment subcomment);
    List<SubCommentLike> findAllByMember(Member member);

    Optional<SubCommentLike> findBySubcommentAndMember(SubComment subcomment, Member member);
}
