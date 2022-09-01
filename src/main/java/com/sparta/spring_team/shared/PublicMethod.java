package com.sparta.spring_team.shared;

import com.sparta.spring_team.dto.response.ResponseDto;
import com.sparta.spring_team.entity.Member;
import com.sparta.spring_team.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

import static com.sparta.spring_team.Exception.ErrorCode.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class PublicMethod {
    private final TokenProvider tokenProvider;

    @Transactional
    public ResponseDto<?> checkLogin(HttpServletRequest request){
        if (null == request.getHeader("Refresh-Token")) {
            return ResponseDto.fail(INVALID_LOGIN);
        }

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail(INVALID_LOGIN);
        }

        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail(INVALID_TOKEN);
        }

        return ResponseDto.success(member);
    }

    @Transactional
    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }
}
