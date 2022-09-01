package com.sparta.spring_team.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.spring_team.dto.response.ResponseDto;
import java.io.IOException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.sparta.spring_team.Exception.ErrorCode.INVALID_LOGIN;

//JWT토큰 인증시 예외발생할 때의 처리
@Component
public class AuthenticationEntryPointException implements
        AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(
                new ObjectMapper().writeValueAsString(
                        ResponseDto.fail(INVALID_LOGIN)
                )
        );
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
