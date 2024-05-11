package com.example.demo;

import com.example.demo.DTOs.AssetLocationDTO;
import com.example.demo.DTOs.TrafficDataDTO;
import com.example.demo.Services.AssetService;
import com.example.demo.Services.TrafficDataService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import java.util.List;

@EntityScan("com.example.demo.Entities")
@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

	private final AssetService assetService;
	private final TrafficDataService trafficDataService;

	public DemoApplication(AssetService assetService, TrafficDataService trafficDataService) {
		this.assetService = assetService;
		this.trafficDataService = trafficDataService;
	}

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Override
	public void run(String... args) {
		//testSingleAssetLocation();
		//testAllAssetLocations();
		testAllTrafficData();
	}

//	private void testSingleAssetLocation() {
//		System.out.println("Fetching the first asset location...");
//		AssetLocationDTO location = assetService.getFirstAssetLocation();
//		if (location != null) {
//			System.out.println("First Asset Location Retrieved: " + location);
//			System.out.println("Name: " + location.getName());
//			System.out.println("Latitude: " + location.getLatitude());
//			System.out.println("Longitude: " + location.getLongitude());
//		} else {
//			System.out.println("First asset location could not be retrieved.");
//		}
//	}

//	private void testAllAssetLocations() {
//		System.out.println("Fetching all asset locations...");
//		List<AssetLocationDTO> locations = assetService.getAllAssetLocations();
//		if (locations != null && !locations.isEmpty()) {
//			System.out.println("All Asset Locations Retrieved: " + locations.size() + " assets found.");
//			for (AssetLocationDTO location : locations) {
//				System.out.println("Name: " + location.getName() + ", Latitude: " + location.getLatitude() + ", Longitude: " + location.getLongitude());
//			}
//		} else {
//			System.out.println("No asset locations were retrieved.");
//		}
//	}

	private void testAllTrafficData() {
		System.out.println("Fetching all traffic data...");
		List<TrafficDataDTO> data = trafficDataService.getAllTrafficData();
		if (data != null && !data.isEmpty()) {
			System.out.println("All Traffic Data Retrieved: " + data.size() + " entries found.");
			for (TrafficDataDTO entry : data) {
				if (entry != null) {
					System.out.println("DatetimeUtc: " + entry.getDatetimeUtc() + ", MMSI: " + entry.getMmsi() +
							", Class: " + entry.getVesselClass() +
							", Latitude: " + entry.getLatitude() + ", Longitude: " + entry.getLongitude() +
							", SOG: " + entry.getSog() + ", COG: " + entry.getCog() + ", Heading: " + entry.getHeading());
				} else {
					System.out.println("Encountered a null entry in traffic data.");
				}
			}
		} else {
			System.out.println("No traffic data was retrieved.");
		}
	}
}