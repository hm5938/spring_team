package com.sparta.spring_team.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import com.sparta.spring_team.dto.response.ResponseDto;
import com.sparta.spring_team.entity.Member;
import com.sparta.spring_team.shared.PublicMethod;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;

@RequiredArgsConstructor
@Service
public class AwsS3Service {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3Client amazonS3Client;
    private final PublicMethod publicMethod;


    @Transactional
    public ResponseDto<?> uploadFile(MultipartFile multipartFile, HttpServletRequest request){
        ResponseDto<?> result = publicMethod.checkLogin(request);
        if(!result.isSuccess()) return result;

        String fileName = multipartFile.getOriginalFilename();
        String imagePath = "";
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(multipartFile.getSize());
        objectMetadata.setContentType(multipartFile.getContentType());

        try(InputStream inputStream = multipartFile.getInputStream()){
            amazonS3Client.putObject(new PutObjectRequest(bucket,fileName,inputStream,objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
            imagePath = String.valueOf(amazonS3Client.getUrl(bucket,fileName));

        } catch (Exception e) {
//            throw new RuntimeException(e);
            return ResponseDto.fail("FAIL_TO_UPLOAD","이미지 업로드에 실패했습니다. "+ e.toString());
        }


        return ResponseDto.success(imagePath);
    }



}
