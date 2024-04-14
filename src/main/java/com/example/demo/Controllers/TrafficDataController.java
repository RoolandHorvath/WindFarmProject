package com.example.demo.Controllers;

import com.example.demo.Entities.TrafficData;
import com.example.demo.Services.TrafficDataService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/trafficdata")
public class TrafficDataController {

    private final TrafficDataService trafficDataService;

    public TrafficDataController(TrafficDataService trafficDataService) {
        this.trafficDataService = trafficDataService;
    }

    @GetMapping
    public ResponseEntity<List<TrafficData>> getAllTrafficData() {
        List<TrafficData> trafficData = trafficDataService.findAll();
        return ResponseEntity.ok(trafficData);
    }
}
