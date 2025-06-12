package com.example.co2analyzer.Model;

public class SageMakerResponse {
    private String jobName;
    private Double dynamicPower;
    private Double staticEmission;
    private Double dynamicEmission;
    private String creationTime;
    private String jobStatus;

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public Double getDynamicPower() {
        return dynamicPower;
    }

    public void setDynamicPower(Double dynamicPower) {
        this.dynamicPower = dynamicPower;
    }

    public Double getStaticEmission() {
        return staticEmission;
    }

    public void setStaticEmission(Double staticEmission) {
        this.staticEmission = staticEmission;
    }

    public Double getDynamicEmission() {
        return dynamicEmission;
    }

    public void setDynamicEmission(Double dynamicEmission) {
        this.dynamicEmission = dynamicEmission;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public String getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
    }
}
