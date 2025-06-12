package com.example.co2analyzer.service;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DynamoDbService {

//    @Autowired
//    private AmazonDynamoDB dynamoDBClient;

    private static final String TABLE_NAME = "AIModelCo2Metrics";
    private static final String JOB_NAME_KEY = "JobName ";

    @Autowired
    private final DynamoDbClient dynamoDbClient;

    public DynamoDbService(DynamoDbClient dynamoDbClient) {
        this.dynamoDbClient = dynamoDbClient;
    }

//    public void fetch(){
//        ScanRequest scanRequest = ScanRequest.builder()
//                .tableName(TABLE_NAME)
//                .limit(1) // Only need the first item
//                .build();
//
//        ScanResponse response = dynamoDbClient.scan(scanRequest);
//
//    }
    public List<String> fetchAllItemsFromTable(String tableName) {
        ScanRequest scanRequest = ScanRequest.builder()
                .tableName(tableName)
                .build();

        ScanResponse scanResponse = dynamoDbClient.scan(scanRequest);
        ObjectMapper objectMapper = new ObjectMapper();


        return scanResponse.items().stream()
                .map(this::convertItemToJsonMap)
                .collect(Collectors.toList());
    }

    public String convertItemToJsonMap(Object object) {
        ObjectMapper objectMapper = new ObjectMapper();
                try {
                    return objectMapper.writeValueAsString(objectMapper);
                } catch (JsonProcessingException e) {
                    return "";
                }
    }

}
