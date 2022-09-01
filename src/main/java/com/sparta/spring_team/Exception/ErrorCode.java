package com.sparta.spring_team.Exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    //회원가입, 로그인 관련 에러
    DUPLICATE_MEMBERNAME("DUPLICATE_MEMBERNAME", "중복된 이름이 있습니다."),
    PASSWORDS_NOT_MATCHED("PASSWORDS_NOT_MATCHED", "비밀번호와 비밀번호 확인이 일치하지 않습니다."),
    MEMBER_NOT_FOUND("MEMBER_NOT_FOUND", "사용자를 찾을 수 없습니다."),
    INVALID_LOGIN("INVALID_LOGIN", "로그인이 필요합니다."),
    INVALID_TOKEN("INVALID_TOKEN", "Token이 유효하지 않습니다."),

    //게시글, 댓글, 대댓글을 찾을 수 없을 때
    INVALID_POST("INVALID_POST", "존재하지 않는 게시글입니다."),
    INVALID_COMMENT("INVALID_COMMENT", "존재하지 않는 댓글입니다."),
    INVALID_SUBCOMMENT("INVALID_SUBCOMMENT", "존재하지 않는 대댓글입니다."),

    //게시글, 댓글, 대댓글 작성자가 아닐 때(수정, 삭제 권한 없음)
    NO_AUTHORIZATION("NO_AUTHOR", "권한이 없습니다."),

    //좋아요 관련 에러
    DUPLICATE_LIKES("DUPLICATE_LIKES", "이미 좋아요를 눌렀습니다."),
    INVALID_LIKES("INVALID_LIKES", "해당 좋아요를 찾을 수 없습니다."),
    WRONG_DATA("WRONG_DATA", "데이터가 잘못되었습니다."),

    //image 파일 관련 오류
    NULL_EXCEPTION("NULL_EXCEPTION", "파일이 존재하지 않습니다."),
    FAIL_TO_UPLOAD("FAIL_TO_UPLOAD", "이미지 업로드에 실패했습니다.");

    private final String code;
    private final String message;
}
