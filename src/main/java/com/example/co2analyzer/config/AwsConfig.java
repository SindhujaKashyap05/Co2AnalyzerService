package com.example.co2analyzer.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.s3.S3Client;


@Configuration
public class AwsConfig {

    private static final Logger logger = LoggerFactory.getLogger(AwsConfig.class);

    private final S3Properties s3Properties;

    @Autowired
    public AwsConfig(S3Properties s3Properties) {
        this.s3Properties = s3Properties;
    }

    @Bean
    public S3Client s3Client() {
        try {
            logger.info("Initializing S3 client for region: {}", s3Properties.getRegion());

            return S3Client.builder()
                    .region(Region.of(s3Properties.getRegion()))
                    .credentialsProvider(StaticCredentialsProvider.create(
                            AwsBasicCredentials.create(
                                    s3Properties.getAccessKey(),
                                    s3Properties.getSecretKey())))
                    .build();
        } catch (Exception e) {
            logger.error("Failed to initialize S3 client", e);
            throw new RuntimeException("Failed to initialize S3 client", e);
        }
    }


   @Bean
    public DynamoDbClient dynamoDbClient() {
        return DynamoDbClient.builder()
            .region(Region.of(s3Properties.getRegion()))
            .credentialsProvider(
                    StaticCredentialsProvider.create(
                            AwsBasicCredentials.create(
                                    s3Properties.getAccessKey(),
                                    s3Properties.getSecretKey()
                            )
                    )
            )
            .build();
    }
}