package com.example.demo.Services;

import com.example.demo.DTOs.TrafficDataDTO;
import com.example.demo.DTOs.TrafficDataFilteredDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.sql.Date;

@Service
public class TrafficDataService {

    @PersistenceContext
    private EntityManager entityManager;

    public List<TrafficDataDTO> getAllTrafficData() {
        List<Object[]> results = entityManager.createNativeQuery(
                        "SELECT DatetimeUtc, MMSI, Class, Position.ToString() AS pointString, SOG, COG, Heading " +
                                "FROM TrafficData " +
                                "WHERE DATEPART(MINUTE, DatetimeUtc) % 5 = 0 AND DATEPART(SECOND, DatetimeUtc) = 0 " +
                                "ORDER BY DatetimeUtc"
                ).setMaxResults(100000).getResultList();

        return results.stream()
                .map(this::convertToTrafficDataDTO)
                .collect(Collectors.toList());
    }



    private TrafficDataDTO convertToTrafficDataDTO(Object[] result) {
        try {
            if (result != null && result.length == 7) {
                String datetimeUtc = ((java.sql.Timestamp) result[0]).toLocalDateTime().toString();
                int mmsi = ((Number) result[1]).intValue();
                String vesselClass = (String) result[2];
                String pointString = (String) result[3];
                double sog = ((Number) result[4]).doubleValue();
                double cog = ((Number) result[5]).doubleValue();
                int heading = ((Number) result[6]).intValue();

                return parsePointString(datetimeUtc, mmsi, vesselClass, pointString, sog, cog, heading);
            } else {
                System.out.println("Invalid data: " + Arrays.toString(result));
            }
        } catch (Exception e) {
            System.out.println("Failed to parse: " + Arrays.toString(result));
            e.printStackTrace();
        }
        return null;
    }


    private TrafficDataDTO parsePointString(String datetimeUtc, int mmsi, String vesselClass, String pointString, double sog, double cog, int heading) {
        Pattern pattern = Pattern.compile("POINT \\((-?\\d+(?:\\.\\d+)?) (-?\\d+(?:\\.\\d+)?)\\)");
        Matcher matcher = pattern.matcher(pointString.trim());

        if (matcher.matches()) {
            double longitude = Double.parseDouble(matcher.group(1));
            double latitude = Double.parseDouble(matcher.group(2));

            return new TrafficDataDTO(datetimeUtc, mmsi, vesselClass, latitude, longitude, sog, cog, heading);
        } else {
            System.out.println("Failed to parse POINT string: " + pointString);
            return null;
        }
    }


    public List<TrafficDataFilteredDTO> getDailyAverageTrafficData() {
        List<Object[]> results = entityManager.createNativeQuery(
                "SELECT CONVERT(DATE, DatetimeUtc) AS Date, MMSI, AVG(SOG) AS AvgSOG, AVG(COG) AS AvgCOG " +
                        "FROM TrafficData " +
                        "GROUP BY CONVERT(DATE, DatetimeUtc), MMSI"
        ).getResultList();

        return results.stream()
                .map(this::convertToTrafficDataAggregateDTO)
                .collect(Collectors.toList());
    }

    private TrafficDataFilteredDTO convertToTrafficDataAggregateDTO(Object[] result) {
        if (result != null && result.length == 4) {
            java.sql.Date date = (java.sql.Date) result[0];
            int mmsi = ((Number) result[1]).intValue();
            double avgSOG = ((BigDecimal) result[2]).doubleValue();
            double avgCOG = ((BigDecimal) result[3]).doubleValue();

            return new TrafficDataFilteredDTO(date.toLocalDate(), mmsi, avgSOG, avgCOG);
        }
        return null;
    }

}