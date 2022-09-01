package com.sparta.spring_team.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import com.sparta.spring_team.dto.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

import static com.sparta.spring_team.Exception.ErrorCode.FAIL_TO_UPLOAD;

@RequiredArgsConstructor
@Service
public class AwsS3Service {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3Client amazonS3Client;

    @Transactional
    public ResponseDto<?> uploadFile(MultipartFile multipartFile){
        //System.out.println(multipartFile);
        //if(multipartFile.isEmpty()) return ResponseDto.fail(NULL_EXCEPTION);

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
            return ResponseDto.fail(FAIL_TO_UPLOAD);
        }

        return ResponseDto.success(imagePath);
    }

}
