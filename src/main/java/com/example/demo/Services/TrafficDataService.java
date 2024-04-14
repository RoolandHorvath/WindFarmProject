package com.example.demo.Services;

import com.example.demo.Entities.TrafficData;
import com.example.demo.Repositories.TrafficDataRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TrafficDataService {

    private final TrafficDataRepository trafficDataRepository;

    public TrafficDataService(TrafficDataRepository repository) {
        this.trafficDataRepository = repository;
    }

    public List<TrafficData> findAll() {
        return trafficDataRepository.findAll();
    }
    public TrafficData getFirstTrafficData() {
        return trafficDataRepository.findFirstTrafficData();
    }
}
