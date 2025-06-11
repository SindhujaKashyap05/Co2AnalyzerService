package com.example.co2analyzer.exception;

public class S3UploadException extends Exception {
    public S3UploadException(String message) {
        super(message);
    }

    public S3UploadException(String message, Throwable cause) {
        super(message, cause);
    }
}