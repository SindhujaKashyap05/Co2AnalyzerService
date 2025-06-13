package com.example.co2analyzer.Model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;

@DynamoDbBean
public class CO2Metrics {
    private String jobName;
    private Double dynamicPower;
    private Double staticEmission;
    private Double dynamicEmission;

    @DynamoDbPartitionKey
    @DynamoDbAttribute("JobName")
    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    @DynamoDbAttribute("dynamic_power_kw")
    public Double getDynamicPower() {
        return dynamicPower;
    }

    public void setDynamicPower(Double dynamicPower) {
        this.dynamicPower = dynamicPower;
    }

    @DynamoDbAttribute("static_emissions_kgCO2")
    public Double getStaticEmission() {
        return staticEmission;
    }

    public void setStaticEmission(Double staticEmission) {
        this.staticEmission = staticEmission;
    }

    @DynamoDbAttribute("dynamic_emissions_kgCO2")
    public Double getDynamicEmission() {
        return dynamicEmission;
    }

    public void setDynamicEmission(Double dynamicEmission) {
        this.dynamicEmission = dynamicEmission;
    }
}
