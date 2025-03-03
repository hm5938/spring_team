package com.sparta.spring_team.service;

import com.sparta.spring_team.dto.response.MemberResponseDto;
import com.sparta.spring_team.dto.response.ResponseDto;
import com.sparta.spring_team.dto.request.LoginRequestDto;
import com.sparta.spring_team.dto.request.MemberRequestDto;
import com.sparta.spring_team.dto.request.TokenDto;
import com.sparta.spring_team.entity.Member;
import com.sparta.spring_team.jwt.TokenProvider;
import com.sparta.spring_team.repository.MemberRepository;
import com.sparta.spring_team.shared.PublicMethod;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.sparta.spring_team.Exception.ErrorCode.*;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    private final PublicMethod publicMethod;
    private final PostService postService;
    private final CommentService commentService;
    private final SubCommentService subCommentService;
    private final LikeService likeService;

    @Transactional
    public ResponseDto<?> createMember(MemberRequestDto requestDto) {
        if (null != isPresentMember(requestDto.getMembername())) {
            return ResponseDto.fail(DUPLICATE_MEMBERNAME);
        }

        if (!requestDto.getPassword().equals(requestDto.getPasswordConfirm())) {
            return ResponseDto.fail(PASSWORDS_NOT_MATCHED);
        }

        Member member = Member.builder()
                .membername(requestDto.getMembername())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .build();
        memberRepository.save(member);
        return ResponseDto.success(
                MemberResponseDto.builder()
                        .id(member.getId())
                        .membername(member.getMembername())
                        .createdAt(member.getCreatedAt())
                        .modifiedAt(member.getModifiedAt())
                        .build()
        );
    }

    @Transactional
    public ResponseDto<?> login(LoginRequestDto requestDto, HttpServletResponse response) {
        Member member = isPresentMember(requestDto.getMembername());
        if (null == member) {
            return ResponseDto.fail(MEMBER_NOT_FOUND);
        }

        if (!member.validatePassword(passwordEncoder, requestDto.getPassword())) {
            return ResponseDto.fail(MEMBER_NOT_FOUND);
        }

//    UsernamePasswordAuthenticationToken authenticationToken =
//        new UsernamePasswordAuthenticationToken(requestDto.getNickname(), requestDto.getPassword());
//    Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        TokenDto tokenDto = tokenProvider.generateTokenDto(member);
        tokenToHeaders(tokenDto, response);

        return ResponseDto.success(
                MemberResponseDto.builder()
                        .id(member.getId())
                        .membername(member.getMembername())
                        .createdAt(member.getCreatedAt())
                        .modifiedAt(member.getModifiedAt())
                        .build()
        );
    }

//  @Transactional
//  public ResponseDto<?> reissue(HttpServletRequest request, HttpServletResponse response) {
//    if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
//      return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
//    }
//    Member member = tokenProvider.getMemberFromAuthentication();
//    if (null == member) {
//      return ResponseDto.fail("MEMBER_NOT_FOUND",
//          "사용자를 찾을 수 없습니다.");
//    }
//
//    Authentication authentication = tokenProvider.getAuthentication(request.getHeader("Access-Token"));
//    RefreshToken refreshToken = tokenProvider.isPresentRefreshToken(member);
//
//    if (!refreshToken.getValue().equals(request.getHeader("Refresh-Token"))) {
//      return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
//    }
//
//    TokenDto tokenDto = tokenProvider.generateTokenDto(member);
//    refreshToken.updateValue(tokenDto.getRefreshToken());
//    tokenToHeaders(tokenDto, response);
//    return ResponseDto.success("success");
//  }

    @Transactional
    public ResponseDto<?> logout(HttpServletRequest request) {
        ResponseDto<?> result = publicMethod.checkLogin(request);
        if(!result.isSuccess()) return result;
        Member member = (Member) result.getData();

        return tokenProvider.deleteRefreshToken(member);
    }

    @Transactional(readOnly = true)
    public ResponseDto<?> mypage(HttpServletRequest request){
        ResponseDto<?> result = publicMethod.checkLogin(request);
        if(!result.isSuccess()) return result;
        Member member = (Member) result.getData();

        List<ResponseDto> responseList = new ArrayList<>();

        //글, 댓글, 대댓글 모두 조회
        responseList.add(postService.readAllPostsByMember(member));
        responseList.add(commentService.getAllCommentsByMember(member));
        responseList.add(subCommentService.getAllSubCommentsByMember(member));

        //좋아요한 글, 댓글, 대댓글 모두 조회
        responseList.add(likeService.getAllPostLikesByMember(member));
        responseList.add(likeService.getAllCommentLikesByMember(member));
        responseList.add(likeService.getAllSubCommentLikesByMember(member));

        return ResponseDto.success(responseList);
    }


    @Transactional(readOnly = true)
    public Member isPresentMember(String membername) {
        Optional<Member> optionalMember = memberRepository.findByMembername(membername);
        return optionalMember.orElse(null);
    }

    public void tokenToHeaders(TokenDto tokenDto, HttpServletResponse response) {
        response.addHeader("Authorization", "Bearer " + tokenDto.getAccessToken());
        response.addHeader("Refresh-Token", tokenDto.getRefreshToken());
        response.addHeader("Access-Token-Expire-Time", tokenDto.getAccessTokenExpiresIn().toString());
    }

}