package com.example.co2analyzer.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "ML Model upload response")
public class ModelUploadResponse {

    @Schema(description = "Upload status", example = "SUCCESS")
    private String status;

    @Schema(description = "Uploaded model ID", example = "model_123456")
    private String modelId;

    @Schema(description = "S3 object key", example = "models/fraud_detection_v1/1.0.0/model.pkl")
    private String s3Key;

    @Schema(description = "File size in bytes", example = "1048576")
    private long fileSize;

    @Schema(description = "Upload timestamp")
    private LocalDateTime uploadedAt;

    @Schema(description = "Response message", example = "Model uploaded successfully")
    private String message;

    // Constructors
    public ModelUploadResponse() {
    }

    public ModelUploadResponse(String status, String modelId, String s3Key, long fileSize, String message) {
        this.status = status;
        this.modelId = modelId;
        this.s3Key = s3Key;
        this.fileSize = fileSize;
        this.message = message;
        this.uploadedAt = LocalDateTime.now();
    }

    // Getters and setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getS3Key() {
        return s3Key;
    }

    public void setS3Key(String s3Key) {
        this.s3Key = s3Key;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}