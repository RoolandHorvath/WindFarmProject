package com.example.demo.Controllers;

import com.example.demo.DTOs.TrafficDataDTO;
import com.example.demo.Services.TrafficDataService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/traffic")
public class TrafficDataController {
    private final TrafficDataService trafficDataService;

    public TrafficDataController(TrafficDataService trafficDataService) {
        this.trafficDataService = trafficDataService;
    }

    @GetMapping
    public ResponseEntity<List<TrafficDataDTO>> getAllTrafficData() {
        List<TrafficDataDTO> trafficData = trafficDataService.getAllTrafficData();
        if (trafficData.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(trafficData);
    }
}