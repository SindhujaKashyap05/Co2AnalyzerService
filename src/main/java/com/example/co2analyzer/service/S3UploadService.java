package com.example.co2analyzer.service;

import com.example.co2analyzer.config.S3Properties;
import com.example.co2analyzer.dto.ModelUploaderRequest;
import com.example.co2analyzer.exception.S3UploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class S3UploadService {

    private final S3Client s3Client;
    private final S3Properties s3Properties;

    @Autowired
    public S3UploadService(S3Client s3Client, S3Properties s3Properties) {
        this.s3Client = s3Client;
        this.s3Properties = s3Properties;
    }

    public String uploadModel(MultipartFile file, ModelUploaderRequest request) throws S3UploadException {
        try {
            String s3Key = generateS3Key(request, file.getOriginalFilename());

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(s3Properties.getBucketName())
                    .key(s3Key)
                    .contentType(file.getContentType())
                    .metadata(java.util.Map.of(
                            "model-name", request.getModelName(),
                            "version", request.getVersion(),
                            "framework", request.getFramework() != null ? request.getFramework() : "unknown",
                            "model-type", request.getModelType() != null ? request.getModelType() : "unknown",
                            "upload-timestamp", LocalDateTime.now().toString()
                    ))
                    .build();

            s3Client.putObject(putObjectRequest,
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            return s3Key;

        } catch (S3Exception e) {
            throw new S3UploadException("Failed to upload to S3: " + e.getMessage(), e);
        } catch (IOException e) {
            throw new S3UploadException("Failed to read file: " + e.getMessage(), e);
        }
    }

    private String generateS3Key(ModelUploaderRequest request, String originalFilename) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String fileExtension = getFileExtension(originalFilename);

        return String.format("models/%s/%s/%s_%s%s",
                request.getModelName(),
                request.getVersion(),
                timestamp,
                UUID.randomUUID().toString().substring(0, 8),
                fileExtension);
    }

    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf("."));
    }
}