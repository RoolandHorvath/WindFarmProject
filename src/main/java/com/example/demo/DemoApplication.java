package com.example.demo;

import com.example.demo.DTOs.AssetLocationDTO;
import com.example.demo.Services.AssetService;
import com.example.demo.Services.ManifestsJsonService;
import com.example.demo.Services.TrafficDataService;
import com.example.demo.Services.TrafficDataVesselsAroundAssetsService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;

import java.util.List;

@EntityScan("com.example.demo.Entities")
@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

	private final AssetService assetService;

	public DemoApplication(AssetService assetService) {
		this.assetService = assetService;
	}

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Override
	public void run(String... args) {
		testSingleAssetLocation();
		testAllAssetLocations();
	}

	private void testSingleAssetLocation() {
		System.out.println("Fetching the first asset location...");
		AssetLocationDTO location = assetService.getFirstAssetLocation();
		if (location != null) {
			System.out.println("First Asset Location Retrieved: " + location);
			System.out.println("Name: " + location.getName());
			System.out.println("Latitude: " + location.getLatitude());
			System.out.println("Longitude: " + location.getLongitude());
		} else {
			System.out.println("First asset location could not be retrieved.");
		}
	}

	private void testAllAssetLocations() {
		System.out.println("Fetching all asset locations...");
		List<AssetLocationDTO> locations = assetService.getAllAssetLocations();
		if (locations != null && !locations.isEmpty()) {
			System.out.println("All Asset Locations Retrieved: " + locations.size() + " assets found.");
			for (AssetLocationDTO location : locations) {
				System.out.println("Name: " + location.getName() + ", Latitude: " + location.getLatitude() + ", Longitude: " + location.getLongitude());
			}
		} else {
			System.out.println("No asset locations were retrieved.");
		}
	}
}