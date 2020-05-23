package com.app.co2;

import com.app.co2.model.city.Coordinates;
import com.app.co2.service.GeocodeDistanceService;
import com.app.co2.util.Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;

/**
 * @author Hasham Rasheed
 * Calculates total co2 emissions emitted by different sized vehicles traveling between 2 cities.
 */

@SpringBootApplication
public class Co2Application implements CommandLineRunner {

    @Autowired
    private GeocodeDistanceService api;

    @Autowired
    private Util util;

    public static void main(final String[] args) {
        SpringApplication.run(Co2Application.class, args);
    }

    @Override
    public void run(final String... args) {
        if(Util.extractArgs(args) == null) {
            throw new IllegalArgumentException("MISSING ARGUMENTS...");
        }
        final String[] cleanArgs = Util.extractArgs(args);
        for(final String arg : cleanArgs) {
            System.out.println("########## "+arg+" ##########");
        }
        final String apiKey = util.resolveAPIKey();

        System.out.println("Finding coordinates for source and destination provided...");

        final Coordinates source = findCity(apiKey, cleanArgs, 0);

        final Coordinates destination = findCity(apiKey, cleanArgs, 1);

        final double metricsData = calculateDistance(apiKey, cleanArgs, source, destination);

        System.out.println("Distance in meters found: "+metricsData);

        //do actual CO2 calculation here
        final double emission = api.findEmissionByCarType(cleanArgs[2].toLowerCase());

        //result should be in kilograms
        final double result = (metricsData/1000) * (emission/1000);

        System.out.println("Your trip caused "+new BigDecimal(result).setScale(2, RoundingMode.HALF_UP).doubleValue()+"kg of CO2-equivalent");

    }

    /**
     * Calculates distance between cities provided by calling service
     * @param cleanArgs
     * @param source
     * @param destination
     * @return
     */
    private double calculateDistance(final String apiKey,
                                     final String[] cleanArgs,
                                     final Coordinates source,
                                     final Coordinates destination) {
        System.out.println("Finding distance between source and destination provided...");
        //assuming the result is in meters, kilometer conversion is needed
        final double metricsData = (api.findDistance(apiKey, new Coordinates[]{source,destination}));
        if(metricsData == 0) {
            throw new IllegalArgumentException("UNABLE TO FIND DISTANCE BETWEEN: "+cleanArgs[0]+" and "+cleanArgs[1]);
        }
        return metricsData;
    }

    /**
     * Finds provided city by calling services
     * @param cleanArgs
     * @param index
     * @return
     */
    private Coordinates findCity(final String apiKey,
                                 final String[] cleanArgs,
                                 final int index) {
        final Coordinates city = api.findCityCoordinates(apiKey, cleanArgs[index]);
        if (city == null) {
            throw new IllegalArgumentException("COULD NOT FIND LOCATION: " + cleanArgs[index]);
        }
        System.out.println("Found coordinates: [" + city.getLat() + "," + city.getLng() + "]");
        return city;
    }


}
