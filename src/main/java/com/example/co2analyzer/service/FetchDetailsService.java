package com.example.co2analyzer.service;

import com.example.co2analyzer.Model.CO2Metrics;
import com.example.co2analyzer.Model.SageMakerJobStatus;
import com.example.co2analyzer.Model.SageMakerResponse;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.util.ArrayList;
import java.util.List;

@Service
public class FetchDetailsService {

    private final DynamoDbEnhancedClient enhancedClient;

    public FetchDetailsService (DynamoDbEnhancedClient enhancedClient) {
        this.enhancedClient = enhancedClient;
    }

    public List<SageMakerResponse> getAllJobDetails() {
        DynamoDbTable< CO2Metrics> metricsTable = enhancedClient.table("AIModelCo2Metrics", TableSchema.fromBean(CO2Metrics.class));
        DynamoDbTable<SageMakerJobStatus> statusTable = enhancedClient.table("SageMakerJobStatus", TableSchema.fromBean(SageMakerJobStatus.class));

        List<SageMakerResponse> responses = new ArrayList<>();
        System.out.println("Inside jobDetails");
        for (CO2Metrics metric : metricsTable.scan().items()) {
            SageMakerJobStatus status = statusTable.getItem(r -> r.key(k -> k.partitionValue(metric.getJobName())));

            if (status != null) {
                SageMakerResponse res = new SageMakerResponse();
                res.setJobName(metric.getJobName());
                res.setDynamicPower(metric.getDynamicPower());
                res.setStaticEmission(metric.getStaticEmission());
                res.setDynamicEmission(metric.getDynamicEmission());
                res.setCreationTime(status.getCreationTime());
                res.setJobStatus(status.getJobStatus());
                res.setModelFile(status.getModelFile());
                responses.add(res);
            }
        }
        return responses;
    }
}
