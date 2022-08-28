package com.sparta.spring_team.repository;

import com.sparta.spring_team.entity.Member;
import com.sparta.spring_team.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByMember(Member member);
}