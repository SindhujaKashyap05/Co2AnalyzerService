package com.example.co2analyzer.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@RestController
public class UIController {

    @GetMapping("/api/analytics")
    public List<Map<String, Object>> getAnalytics() {
        return List.of(
                Map.of("id", 1, "name", "BERT-Base", "type", "NLP", "co2", 45.2, "status", "analyzed", "lastRun", "2024-12-01"),
                Map.of("id", 2, "name", "GPT-3.5", "type", "Language", "co2", 234.7, "status", "analyzing", "lastRun", "2024-12-02"),
                Map.of("id", 3, "name", "ResNet-50", "type", "Vision", "co2", 78.3, "status", "analyzed", "lastRun", "2024-11-29"),
                Map.of("id", 4, "name", "YOLO-v8", "type", "Detection", "co2", 156.9, "status", "optimized", "lastRun", "2024-12-01")
        );
    }

}
