package com.example.co2analyzer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "ML Model upload metadata")
public class ModelUploaderRequest {

    @NotBlank(message = "Model name is required")
    @Size(min = 3, max = 50, message = "Model name must be between 3 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "Model name can only contain alphanumeric characters, hyphens, and underscores")
    @Schema(description = "Name of the ML model", example = "fraud_detection_v1")
    private String modelName;

    @NotBlank(message = "Version is required")
    @Pattern(regexp = "^\\d+\\.\\d+\\.\\d+$", message = "Version must follow semantic versioning (e.g., 1.0.0)")
    @Schema(description = "Model version following semantic versioning", example = "1.0.0")
    private String version;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    @Schema(description = "Model description", example = "Machine learning model for fraud detection")
    private String description;

    @Schema(description = "Model framework", example = "tensorflow", allowableValues = {"tensorflow", "pytorch", "sklearn", "xgboost", "lightgbm"})
    private String framework;

    @Schema(description = "Model type", example = "classification", allowableValues = {"classification", "regression", "clustering", "recommendation"})
    private String modelType;

    // Constructors
    public ModelUploaderRequest() {
    }

    public ModelUploaderRequest(String modelName, String version) {
        this.modelName = modelName;
        this.version = version;
        this.description = description;
    }

    // Getters and setters
    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

}