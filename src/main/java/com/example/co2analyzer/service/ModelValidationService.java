package com.example.co2analyzer.service;


import com.example.co2analyzer.config.S3Properties;
import com.example.co2analyzer.exception.ModelValidationException;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class ModelValidationService {

    // Allowed file extensions for ML models
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList(
            ".pkl", ".joblib", ".h5", ".pb", ".pt", ".pth", ".onnx", ".pmml", ".json", ".ipynb"
    );
    // Allowed MIME types
    private static final List<String> ALLOWED_MIME_TYPES = Arrays.asList(
            "application/octet-stream",
            "application/x-pickle",
            "application/json",
            "text/plain"
    );
    private final S3Properties s3Properties;
    private final Tika tika;

    @Autowired
    public ModelValidationService(S3Properties s3Properties) {
        this.s3Properties = s3Properties;
        this.tika = new Tika();
    }

    public void validateModelFile(MultipartFile file) throws ModelValidationException {
        validateFileNotEmpty(file);
        validateFileSize(file);
        validateFileExtension(file);
        validateMimeType(file);
        validateFileContent(file);
    }

    private void validateFileNotEmpty(MultipartFile file) throws ModelValidationException {
        if (file.isEmpty()) {
            throw new ModelValidationException("File cannot be empty");
        }
    }

    private void validateFileSize(MultipartFile file) throws ModelValidationException {
        if (file.getSize() > s3Properties.getMaxFileSize()) {
            throw new ModelValidationException(
                    String.format("File size exceeds maximum allowed size of %d bytes",
                            s3Properties.getMaxFileSize()));
        }
    }

    private void validateFileExtension(MultipartFile file) throws ModelValidationException {
        String filename = file.getOriginalFilename();
        if (filename == null) {
            throw new ModelValidationException("Filename cannot be null");
        }

        boolean validExtension = ALLOWED_EXTENSIONS.stream()
                .anyMatch(ext -> filename.toLowerCase().endsWith(ext));

        if (!validExtension) {
            throw new ModelValidationException(
                    "Invalid file extension. Allowed extensions: " + String.join(", ", ALLOWED_EXTENSIONS));
        }
    }

    private void validateMimeType(MultipartFile file) throws ModelValidationException {
        try {
            String detectedMimeType = tika.detect(file.getInputStream(), file.getOriginalFilename());

            if (!ALLOWED_MIME_TYPES.contains(detectedMimeType)) {
                // For some ML model files, MIME type detection might not be accurate
                // So we'll log a warning but not fail the validation
                System.out.println("Warning: Detected MIME type " + detectedMimeType +
                        " is not in the standard allowed list, but proceeding with upload");
            }
        } catch (IOException e) {
            throw new ModelValidationException("Error reading file for MIME type validation: " + e.getMessage());
        }
    }

    private void validateFileContent(MultipartFile file) throws ModelValidationException {
        // Basic content validation - check if file has some content
        try {
            byte[] content = file.getBytes();
            if (content.length == 0) {
                throw new ModelValidationException("File content is empty");
            }

            // Additional validation could be added here based on file type
            // For example, for JSON files, validate JSON structure
            String filename = file.getOriginalFilename();
            if (filename != null && filename.toLowerCase().endsWith(".json")) {
                validateJsonContent(content);
            }

        } catch (IOException e) {
            throw new ModelValidationException("Error reading file content: " + e.getMessage());
        }
    }

    private void validateJsonContent(byte[] content) throws ModelValidationException {
        try {
            String jsonString = new String(content);
            // Basic JSON validation - check if it starts and ends correctly
            jsonString = jsonString.trim();
            if (!((jsonString.startsWith("{") && jsonString.endsWith("}")) ||
                    (jsonString.startsWith("[") && jsonString.endsWith("]")))) {
                throw new ModelValidationException("Invalid JSON format");
            }
        } catch (Exception e) {
            throw new ModelValidationException("Invalid JSON content: " + e.getMessage());
        }
    }
}