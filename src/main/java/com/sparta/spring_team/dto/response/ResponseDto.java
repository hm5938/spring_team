package com.sparta.spring_team.dto.response;

import com.sparta.spring_team.Exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseDto<T> {
    private boolean success;
    private T data;
    private ErrorCode error;

    public static <T> ResponseDto<T> success(T data) {
        return new ResponseDto<>(true, data, null);
    }

    public static <T> ResponseDto<T> fail(ErrorCode code) {
        return new ResponseDto<>(false, null, code);
    }



    @Getter
    @AllArgsConstructor
    static class Error {
        private String code;
        private String message;
    }

}
