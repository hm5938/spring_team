package com.sparta.spring_team.controller;


import com.sparta.spring_team.dto.response.ResponseDto;
import com.sparta.spring_team.service.AwsS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ImageController {

    private final AwsS3Service awsS3Service;

    @RequestMapping(value = "/auth/images", method = RequestMethod.POST)
    public ResponseDto<?> readImage(@RequestPart(required = false)MultipartFile multipartFile, HttpServletRequest request) {
        return awsS3Service.uploadFile(multipartFile, request);
    }

}