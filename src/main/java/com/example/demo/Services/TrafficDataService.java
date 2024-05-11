package com.example.demo.Services;

import com.example.demo.DTOs.TrafficDataDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class TrafficDataService {

    @PersistenceContext
    private EntityManager entityManager;

    public List<TrafficDataDTO> getAllTrafficData() {
        List<Object[]> results = entityManager.createNativeQuery(
                "SELECT TOP 1000 DatetimeUtc, MMSI, Class, Position.ToString() AS pointString, SOG, COG, Heading FROM TrafficData"
        ).getResultList();
        return results.stream()
                .map(this::convertToTrafficDataDTO)
                .collect(Collectors.toList());
    }


    private TrafficDataDTO convertToTrafficDataDTO(Object[] result) {
        try {
            if (result != null && result.length == 7) {
                String datetimeUtc = ((java.sql.Timestamp) result[0]).toLocalDateTime().toString();
                int mmsi = ((Number) result[1]).intValue(); // Handle Short or Integer safely
                String vesselClass = (String) result[2];
                String pointString = (String) result[3];
                double sog = ((Number) result[4]).doubleValue(); // Handle potential float/double
                double cog = ((Number) result[5]).doubleValue(); // Handle potential float/double
                int heading = ((Number) result[6]).intValue(); // Handle Short or Integer safely

                return parsePointString(datetimeUtc, mmsi, vesselClass, pointString, sog, cog, heading);
            } else {
                System.out.println("Invalid data or incorrect array length: " + Arrays.toString(result));
            }
        } catch (Exception e) {
            System.out.println("Failed to parse data: " + Arrays.toString(result));
            e.printStackTrace();
        }
        return null;
    }


    private TrafficDataDTO parsePointString(String datetimeUtc, int mmsi, String vesselClass, String pointString, double sog, double cog, int heading) {
        // Updated regex to handle both integer and decimal coordinates more robustly
        Pattern pattern = Pattern.compile("POINT \\((-?\\d+(?:\\.\\d+)?) (-?\\d+(?:\\.\\d+)?)\\)");
        Matcher matcher = pattern.matcher(pointString.trim());

        if (matcher.matches()) {
            // Parse longitude and latitude from the captured groups
            double longitude = Double.parseDouble(matcher.group(1));
            double latitude = Double.parseDouble(matcher.group(2));

            // Construct and return a new TrafficDataDTO with the parsed values
            return new TrafficDataDTO(datetimeUtc, mmsi, vesselClass, latitude, longitude, sog, cog, heading);
        } else {
            // Log an error message if the POINT string does not match the expected format
            System.out.println("Failed to parse POINT string: " + pointString);
            return null;
        }
    }

}