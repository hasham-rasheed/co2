package com.app.co2.service;

import com.app.co2.model.city.Coordinates;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(args = {"--start=Islamabad", "--end=Lahore", "--transportation-method=bus"})
class GeocodeDistanceServiceTest {

    @Value("${fallback.api.key}")
    private String fallBackApiKey;
    private static final String ORS_TOKEN = "ORS_TOKEN";
    private String API_KEY="";
    @Value("${start}")
    private String startCity;
    @Value("${end}")
    private String endCity;
    @Value("${transportation-method}")
    private String transportationMethod;
    private GeocodeDistanceService geocodeDistanceServiceUnderTest;

    @BeforeEach
    void setUp() {
        API_KEY = System.getenv(ORS_TOKEN);
        if(StringUtils.isBlank(API_KEY)) {
            System.out.println("###### ORS_TOKEN NOT CONFIGURED, ATTEMPTING TO USE FALLBACK VERSION FROM APPLICATION.PROPERTIES ######");
            API_KEY = fallBackApiKey;
            if(StringUtils.isBlank(API_KEY)) {
                throw new IllegalArgumentException("API KEY NOT FOUND");
            } else {
                System.out.println("###### FALLBACK API KEY FOUND FROM APPLICATION.PROPERTIES ######");
            }
        }
        geocodeDistanceServiceUnderTest = new GeocodeDistanceService();
    }

    @Test
    void testFindCityCoordinates() {
        // Setup
        final Coordinates expectedResult = new Coordinates("33.72148", "73.04329");

        // Run the test
        final Coordinates result = geocodeDistanceServiceUnderTest.findCityCoordinates(API_KEY, startCity);

        // Verify the results
        assertEquals(expectedResult, result);
    }

    @Test
    void testFindDistance() {
        // Setup
        final Coordinates source = geocodeDistanceServiceUnderTest.findCityCoordinates(API_KEY, startCity);
        final Coordinates destination = geocodeDistanceServiceUnderTest.findCityCoordinates(API_KEY, endCity);
        final Coordinates[] srcDest = new Coordinates[]{source, destination};

        // Run the test
        final double result = geocodeDistanceServiceUnderTest.findDistance(API_KEY, srcDest);

        // Verify the results
        assertEquals(294815.75, result, 0.0001);
    }

    @Test
    void testFindEmissionByCarType() {
        // Setup

        // Run the test
        final double result = geocodeDistanceServiceUnderTest.findEmissionByCarType(transportationMethod);

        // Verify the results
        assertEquals(27, result, 0.0001);
    }
}
