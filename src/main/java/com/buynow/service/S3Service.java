//package com.buynow.service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
//import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
//import software.amazon.awssdk.regions.Region;
//import software.amazon.awssdk.services.s3.S3Client;
//import software.amazon.awssdk.services.s3.model.PutObjectRequest;
//
//import java.io.IOException;
//import java.util.UUID;
//
//@Service
//public class S3Service {
//
//    @Autowired
//    private final S3Client s3Client;
//
//    @Value("${aws.s3.bucketName}")
//    private String bucketName;
//
//    public S3Service(
//            @Value("${aws.accessKey}") String accessKey,
//            @Value("${aws.secretKey}") String secretKey,
//            @Value("${aws.region}") String region) {
//
//        this.s3Client = S3Client.builder()
//                .region(Region.of(region))
//                .credentialsProvider(
//                        StaticCredentialsProvider.create(
//                                AwsBasicCredentials.create(accessKey, secretKey)
//                        )
//                ).build();
//    }
//
////    public String uploadFile(MultipartFile file) throws IOException {
////        String key = UUID.randomUUID() + "_" + file.getOriginalFilename();
////
////        s3Client.putObject(
////                PutObjectRequest.builder()
////                        .bucket(bucketName)
////                        .key(key)
////                        .contentType(file.getContentType())
////                        .build(),
////                software.amazon.awssdk.core.sync.RequestBody.fromBytes(file.getBytes())
////        );
////
////        return "https://" + bucketName + ".s3.amazonaws.com/" + key; // You can construct full URL if needed
////    }
//
//    public String uploadFile(MultipartFile file) throws IOException {
//        String key = "products/" + UUID.randomUUID() + "_" + file.getOriginalFilename();
//
//        try {
//            s3Client.putObject(
//                    PutObjectRequest.builder()
//                            .bucket(bucketName)
//                            .key(key)
//                            .contentType(file.getContentType())
//                            .build(),
//                    software.amazon.awssdk.core.sync.RequestBody.fromBytes(file.getBytes())
//            );
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to upload file to S3", e);
//        }
//
//        return "https://" + bucketName + ".s3.amazonaws.com/" + key;
//    }
//}

package com.buynow.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
public class S3Service {

    private final S3Client s3Client;

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public String uploadFile(MultipartFile file) throws IOException {
        String key = "products/" + UUID.randomUUID() + "_" + file.getOriginalFilename();

        try {
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(key)
                            .contentType(file.getContentType())
                            .build(),
                    software.amazon.awssdk.core.sync.RequestBody.fromBytes(file.getBytes())
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file to S3", e);
        }

        return "https://" + bucketName + ".s3." + s3Client.serviceClientConfiguration().region().id()
                + ".amazonaws.com/" + key;
    }

    public byte[] downloadFile(String fileName){

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();

        ResponseBytes<GetObjectResponse> objectResponseResponseBytes = s3Client.getObjectAsBytes(getObjectRequest);
        return objectResponseResponseBytes.asByteArray();
    }

//    public void deleteFile(String fileName){
//
//        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
//                .bucket(bucketName)
//                .key(fileName)
//                .build();
//
//        s3Client.deleteObject(deleteObjectRequest);
//    }

    public void deleteFile(String key) {
        try {
            s3Client.deleteObject(builder -> builder.bucket(bucketName).key(key).build());
        } catch (Exception e) {
            throw new RuntimeException("❌ Failed to delete file from S3", e);
        }
    }

}
