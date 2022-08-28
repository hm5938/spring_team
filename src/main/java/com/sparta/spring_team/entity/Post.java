package com.sparta.spring_team.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Post extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = true)
    private String image;

    //좋아요 추가

    //댓글 목록
//    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Comment> comments;

    //멤버
//    @JoinColumn(name = "member_id", nullable = false)
//    @ManyToOne(fetch = FetchType.LAZY)
//    private Member member;
//
//    public void update(PostRequestDto postRequestDto) {
//        this.title = postRequestDto.getTitle();
//        this.content = postRequestDto.getContent();
//    }
//
//    public boolean validateMember(Member member) {
//        return !this.member.equals(member);
//    }

}
