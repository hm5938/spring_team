package com.sparta.spring_team;


import com.sparta.spring_team.entity.Post;
import com.sparta.spring_team.repository.PostRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
@Slf4j
@RequiredArgsConstructor // final 멤버 변수를 자동으로 생성합니다.
@Component // 스프링이 필요 시 자동으로 생성하는 클래스 목록에 추가합니다.
public class Scheduler {

    private final PostRepository postRepository;

    // 초, 분, 시, 일, 월, 주 순서
    @Scheduled(cron = "0 0 1 * * *")
    public void updatePrice() {
        System.out.println("게시물 삭제 실행");
        // 저장된 모든 관심상품을 조회합니다.
        List<Post> posts = postRepository.findAll();
        for (Post post : posts) {
            if (post.getComments().size()==0) {
                postRepository.delete(post);
                log.info("게시물 <<"+post.getTitle()+">> 게시물이 삭제되었습니다");
            }
        }
    }
}