package com.example.co2analyzer.controller;

import com.example.co2analyzer.Model.SageMakerResponse;
import com.example.co2analyzer.service.FetchDetailsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@RestController
public class UIController {

    private final FetchDetailsService fetchDetailsService;

    public UIController(FetchDetailsService fetchDetailsService) {
        this.fetchDetailsService = fetchDetailsService;
    }

    //For displaying as is in UI
    @GetMapping("/api/analytics")
    public List<Map<String, Object>> getAnalytics() {
        return List.of(
                Map.of("id", 1, "name", "BERT-Base", "type", "NLP", "co2", 45.2, "status", "analyzed", "lastRun", "2024-12-01"),
                Map.of("id", 2, "name", "GPT-3.5", "type", "Language", "co2", 234.7, "status", "analyzing", "lastRun", "2024-12-02"),
                Map.of("id", 3, "name", "ResNet-50", "type", "Vision", "co2", 78.3, "status", "analyzed", "lastRun", "2024-11-29"),
                Map.of("id", 4, "name", "YOLO-v8", "type", "Detection", "co2", 156.9, "status", "optimized", "lastRun", "2024-12-01")
        );
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("api/v1/fetch")
    public ResponseEntity<List<SageMakerResponse>> getAllJobDetails() {
        return ResponseEntity.ok(fetchDetailsService.getAllJobDetails());
    }

}
