package com.app.co2;

import com.app.co2.model.city.Coordinates;
import com.app.co2.service.GeocodeDistanceService;
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

    private static final String EQUAL_SIGN = "=";
    private static final String SPLIT = "--";
    private static final String BLANK = "";
    //fallback api key can be configured in application.properties and will be used if ORS_TOKEN environment variable is not found.
    @Value("${fallback.api.key}")
    private String fallbackApiKey;
    //the API KEY corresponding to ORS_TOKEN environment variable
    private String API_KEY;
    private static final String ORS_TOKEN = "ORS_TOKEN";
    private static final String START_KEY = "start";
    private static final String END_KEY = "end";
    private static final String TRANSPORT_KEY = "transportation-method";
    @Autowired
    private GeocodeDistanceService api;

    public static void main(final String[] args) {
        SpringApplication.run(Co2Application.class, args);
    }

    @Override
    public void run(final String... args) {
        if(extractArgs(args) == null) {
            throw new IllegalArgumentException("MISSING ARGUMENTS...");
          
        }
        final String[] cleanArgs = extractArgs(args);
        for(final String arg : cleanArgs) {
            System.out.println("########## "+arg+" ##########");
        }
        API_KEY = System.getenv(ORS_TOKEN);
        if(StringUtils.isBlank(API_KEY)) {
            System.out.println("###### ORS_TOKEN NOT CONFIGURED, ATTEMPTING TO USE FALLBACK VERSION FROM APPLICATION.PROPERTIES ######");
            API_KEY = fallbackApiKey;
            if(StringUtils.isBlank(API_KEY)) {
                throw new IllegalArgumentException("API KEY NOT FOUND");
            } else {
                System.out.println("###### FALLBACK API KEY FOUND FROM APPLICATION.PROPERTIES ######");
            }
        }
        System.out.println("Finding coordinates for source and destination provided...");
        final Coordinates source = api.findCityCoordinates(API_KEY, cleanArgs[0]);
        if(source == null) {
            throw new IllegalArgumentException("COULD NOT FIND LOCATION: "+cleanArgs[0]);
        }
        System.out.println("Found coordinates: ["+source.getLat()+","+source.getLng()+"]");
        final Coordinates destination = api.findCityCoordinates(API_KEY, cleanArgs[1]);

        if(destination == null) {
            throw new IllegalArgumentException("COULD NOT FIND LOCATION: "+cleanArgs[1]);
        }
        System.out.println("Found coordinates: ["+destination.getLat()+","+destination.getLng()+"]");
        System.out.println("Finding distance between source and destination provided...");
        //assuming the result is in meters, kilometer conversion is needed
        final double metricsData = (api.findDistance(API_KEY, new Coordinates[]{source,destination}));
        if(metricsData == 0) {
            throw new IllegalArgumentException("UNABLE TO FIND DISTANCE BETWEEN: "+cleanArgs[0]+" and "+cleanArgs[1]);
        }
        System.out.println("Distance in meters found: "+metricsData);
        //do actual CO2 calculation here
        final double emission = api.findEmissionByCarType(cleanArgs[2].toLowerCase());
        //result should be in kilograms
        final double result = (metricsData/1000) * (emission/1000);
        System.out.println("Your trip caused "+new BigDecimal(result).setScale(2, RoundingMode.HALF_UP).doubleValue()+"kg of CO2-equivalent");

    }

    private String[] extractArgs(final String[] args) {
         if(args == null || args.length == 0) {
            throw new IllegalArgumentException("NO ARGUMENTS PROVIDED...");
        } else if(args.length < 3) {
            throw new IllegalArgumentException("INCOMPLETE ARGUMENTS PROVIDED...");
        }

        final String[] newArgs = new String[3];
        final String[] argsString = Arrays.toString(args)
                .replaceAll("\\[", BLANK)
                .replaceAll("\\]", BLANK)
                .replaceAll(",",BLANK)
                .split(SPLIT);
        if(!Arrays.toString(args).toLowerCase().contains(START_KEY)
                || !Arrays.toString(args).toLowerCase().contains(END_KEY)
                || !Arrays.toString(args).toLowerCase().contains(TRANSPORT_KEY)) {
            throw new IllegalArgumentException("MISSING OR INVALID ARGUMENT(S)...");
        }
        newArgs[0] = argsString[1].trim().replaceAll(START_KEY, BLANK).replaceAll(END_KEY,BLANK).trim().replaceAll(EQUAL_SIGN, BLANK).trim();
        newArgs[1] = argsString[2].trim().replaceAll(START_KEY, BLANK).replaceAll(END_KEY,BLANK).trim().replaceAll(EQUAL_SIGN, BLANK).trim();
        newArgs[2] = argsString[3].trim().replaceAll(TRANSPORT_KEY, BLANK).trim().replaceAll(EQUAL_SIGN, BLANK).trim();

        System.out.println("returning new args: "+ Arrays.toString(newArgs));
        return newArgs;
    }

}
