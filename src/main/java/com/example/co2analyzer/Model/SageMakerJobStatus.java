package com.example.co2analyzer.Model;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;

@DynamoDbBean
public class SageMakerJobStatus {
    private String jobName;
    private String creationTime;
    private String failureReason;
    private String jobArn;
    private String jobStatus;
    private String lastModifiedTime;
    private String modelFile;

    @DynamoDbPartitionKey
    @DynamoDbAttribute("JobName")
    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    @DynamoDbAttribute("CreationTime")
    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    @DynamoDbAttribute("FailureReason")
    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    @DynamoDbAttribute("JobArn")
    public String getJobArn() {
        return jobArn;
    }

    public void setJobArn(String jobArn) {
        this.jobArn = jobArn;
    }

    @DynamoDbAttribute("JobStatus")
    public String getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
    }

    @DynamoDbAttribute("LastModifiedTime")
    public String getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(String lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    @DynamoDbAttribute("ModelFile")
    public String getModelFile() {
        return modelFile;
    }

    public void setModelFile(String modelFile) {
        this.modelFile= modelFile;
    }

}
