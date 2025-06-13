package com.example.co2analyzer.service;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
//import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
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

    //private static final String TABLE_NAME = "AIModelCo2Metrics";

    @Autowired
    private final DynamoDbClient dynamoDbClient;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public DynamoDbService(DynamoDbClient dynamoDbClient) {
        this.dynamoDbClient = dynamoDbClient;
    }


    public List<Map<String, Object>> fetchAllItemsFromTable(String tableName) {
        ScanRequest scanRequest = ScanRequest.builder()
                .tableName(tableName)
                .build();

        ScanResponse scanResponse = dynamoDbClient.scan(scanRequest);
        List<Map<String, AttributeValue>> items = scanResponse.items();

        return items.stream()
                .map(this::convertItemToMap)
                .collect(Collectors.toList());
    }


    private Map<String, Object> convertItemToMap(Map<String, AttributeValue> item) {
        Map<String, Object> plainMap = new HashMap<>();

        for (Map.Entry<String, AttributeValue> entry : item.entrySet()) {
        String key = entry.getKey();
        AttributeValue val = entry.getValue();
        plainMap.put(key, convertAttributeValue(val));
        }
        return plainMap;
   }

    private Object convertAttributeValue(AttributeValue val) {
        if (val == null || (val.nul() != null && val.nul())) {
            return null;
        } else if (val.s() != null) {
            return val.s();
        } else if (val.n() != null) {
            return val.n();
        } else if (val.bool() != null) {
            return val.bool();
        } else if (val.hasL()) {
            return val.l().stream().map(this::convertAttributeValue).collect(Collectors.toList());
        } else if (val.hasM()) {
            return val.m().entrySet().stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            e -> convertAttributeValue(e.getValue())
                    ));
        } else {
            return val.toString();
        }
    }


}
