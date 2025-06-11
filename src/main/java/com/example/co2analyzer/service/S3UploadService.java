package com.example.co2analyzer.service;

import com.example.co2analyzer.config.S3Properties;
import com.example.co2analyzer.dto.ModelUploaderRequest;
import com.example.co2analyzer.exception.S3UploadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class S3UploadService {

    private static final Logger logger = LoggerFactory.getLogger(S3UploadService.class);

    private final S3Properties s3Properties;
    private final S3Client s3Client;

    @Autowired
    public S3UploadService(S3Properties s3Properties) {
        this.s3Properties = s3Properties;
        this.s3Client = createS3Client();
    }

    private S3Client createS3Client() {
        try {
            AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(
                    s3Properties.getAccessKey(),
                    s3Properties.getSecretKey()
            );

            return S3Client.builder()
                    .region(Region.of(s3Properties.getRegion()))
                    .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                    .build();
        } catch (Exception e) {
            logger.error("Failed to create S3 client: {}", e.getMessage());
            throw new RuntimeException("Failed to initialize S3 client", e);
        }
    }

    public String uploadModel(MultipartFile file, ModelUploaderRequest request) throws S3UploadException {
        try {
            String s3Key = generateS3Key(file.getOriginalFilename(), request);

            logger.info("Uploading file to S3. Bucket: {}, Key: {}, Size: {} bytes",
                    s3Properties.getBucketName(), s3Key, file.getSize());

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(s3Properties.getBucketName())
                    .key(s3Key)
                    .contentType(file.getContentType())
                    .contentLength(file.getSize())
                    .metadata(java.util.Map.of(
                            "model-name", request.getModelName(),
                            "version", request.getVersion(),
                            "upload-timestamp", LocalDateTime.now().toString()
                    ))
                    .build();

            PutObjectResponse response = s3Client.putObject(
                    putObjectRequest,
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize())
            );

            logger.info("Successfully uploaded file to S3. ETag: {}", response.eTag());
            return s3Key;

        } catch (S3Exception e) {
            logger.error("AWS S3 error during upload: {}", e.getMessage());
            throw new S3UploadException("Failed to upload to S3: " + e.getMessage(), e);
        } catch (IOException e) {
            logger.error("IO error during upload: {}", e.getMessage());
            throw new S3UploadException("Failed to read file: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Unexpected error during upload: {}", e.getMessage());
            throw new S3UploadException("Unexpected error: " + e.getMessage(), e);
        }
    }

    private String generateS3Key(String originalFilename, ModelUploaderRequest request) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String sanitizedModelName = request.getModelName().replaceAll("[^a-zA-Z0-9-_]", "_");
        String sanitizedVersion = request.getVersion().replaceAll("[^a-zA-Z0-9.-_]", "_");

        return String.format("models/%s/%s/%s_%s",
                sanitizedModelName,
                sanitizedVersion,
                timestamp,
                originalFilename);
    }

    public boolean testConnection() {
        try {
            s3Client.headBucket(builder -> builder.bucket(s3Properties.getBucketName()));
            logger.info("S3 connection test successful");
            return true;
        } catch (Exception e) {
            logger.error("S3 connection test failed: {}", e.getMessage());
            return false;
        }
    }
}