package com.example.co2analyzer.controller;

import com.example.co2analyzer.dto.ErrorResponse;
import com.example.co2analyzer.dto.ModelUploaderRequest;
import com.example.co2analyzer.dto.ModelUploadResponse;
import com.example.co2analyzer.exception.ModelValidationException;
import com.example.co2analyzer.exception.S3UploadException;
import com.example.co2analyzer.service.DynamoDbService;
import com.example.co2analyzer.service.ModelValidationService;
import com.example.co2analyzer.service.S3UploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/models")
@Tag(name = "ML Model Upload", description = "APIs for uploading machine learning models to S3")
public class ModelUploadController {

    private final ModelValidationService validationService;
    private final S3UploadService s3UploadService;

    private final DynamoDbService dynamoDbService;

    @Autowired
    public ModelUploadController(ModelValidationService validationService, S3UploadService s3UploadService, DynamoDbService dynamoDbService) {
        this.validationService = validationService;
        this.s3UploadService = s3UploadService;
        this.dynamoDbService = dynamoDbService;
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Upload ML model to S3",
            description = "Upload a machine learning model file to S3 with validation and metadata"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Model uploaded successfully",
                    content = @Content(schema = @Schema(implementation = ModelUploadResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request or file validation failed",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    public ResponseEntity<?> uploadModel(
            @Parameter(description = "ML model file", required = true)
            @RequestParam("file") MultipartFile file,

            @Parameter(description = "Model name", required = true)
            @RequestParam("modelName") String modelName,

            @Parameter(description = "Model version", required = true)
            @RequestParam("version") String version){

        try {
            // Create request object
            ModelUploaderRequest request = new ModelUploaderRequest(modelName, version);

            // Validate the model file
            validationService.validateModelFile(file);

            // Upload to S3
            String s3Key = s3UploadService.uploadModel(file, request);

            // Generate response
            String modelId = "model_" + UUID.randomUUID().toString().substring(0, 8);
            ModelUploadResponse response = new ModelUploadResponse(
                    "SUCCESS",
                    modelId,
                    s3Key,
                    file.getSize(),
                    "Model uploaded successfully"
            );

            return ResponseEntity.ok(response);

        } catch (ModelValidationException e) {
            ErrorResponse error = new ErrorResponse(e.getMessage(), "VALIDATION_ERROR");
            return ResponseEntity.badRequest().body(error);

        } catch (S3UploadException e) {
            ErrorResponse error = new ErrorResponse("Upload failed: " + e.getMessage(), "UPLOAD_ERROR");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);

        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Internal server error: " + e.getMessage(), "SERVER_ERROR");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/health")
    @Operation(summary = "Health check", description = "Check if the model upload service is healthy")
    @ApiResponse(responseCode = "200", description = "Service is healthy")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Model upload service is healthy");
    }

    //For fetching the details as json from the table
    @GetMapping("/test")
    public List<Map<String, Object>> test(){
        return dynamoDbService.fetchAllItemsFromTable("SageMakerJobStatus");
    }
}