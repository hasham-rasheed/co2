package com.app.co2.service;

import com.app.co2.emissions.*;
import com.app.co2.model.city.CityData;
import com.app.co2.model.city.Coordinates;
import com.app.co2.model.city.Features;
import com.app.co2.model.city.Layers;
import com.app.co2.model.metrics.Metrics;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author Hasham Rasheed
 * Client implementation that provides 2 API:
 *
 * 1- Get City Coordinates : {@link GeocodeDistanceService#findCityCoordinates}.
 * Refer to https://openrouteservice.org/dev/#/api-docs/geocode/search/get for more details.
 *
 * 2- Get distance/time between 2 cities : {@link GeocodeDistanceService#findDistance}
 * Refer to https://openrouteservice.org/dev/#/api-docs/v2/matrix/%7Bprofile%7D/post for more details.
 */
@Service
public class GeocodeDistanceService {

    private static final String API_KEY_PARAM = "?api_key=";
    private static final String TEXT_PARAM = "&text=";
    private static final String WHITE_SPACE_REPLACEMENT = "%20";
    private static final String WHITE_SPACE = " ";
    private static final String SEARCH_CITY_URL = "https://api.openrouteservice.org/geocode/search";
    private static final String TIME_DISTANCE_URL = "https://api.openrouteservice.org/v2/matrix/driving-car";
    private static final String APPLICATION_JSON = "application/json";
    private static final String ACCEPT = "Accept";
    private static final String GET = "GET";
    private static final String SERVICE_ERROR = "Error occurred while calling service: ";
    private static final String CITY_NAME_IS_REQUIRED = "City name is required";
    private static final String LAYERS_PARAM = "&layers=";
    private static final String DISTANCE = "distance";
    private static final String POST = "POST";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String APPLICATION_JSON_UTF_8 = "application/json; utf-8";
    private static final String AUTHORIZATION = "Authorization";


    /**
     * Calls search city api
     * @param apiKey
     * @param params
     * @return
     */
    public Coordinates findCityCoordinates(final String apiKey, final String... params) {
        try {
            if (params == null || params.length == 0) {
                throw new IllegalArgumentException(CITY_NAME_IS_REQUIRED);
            }
            //generate the actual URL for searching the city with city name parameter
            final StringBuilder sb = generateCityCoordinatesURL(apiKey, params);

            final URL url = new URL(sb.toString());
            final HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod(GET);
            conn.setRequestProperty(ACCEPT, APPLICATION_JSON);

            //if call to the API is unsuccessful, show error message.
            if (conn.getResponseCode() != 200) {
                throw new IllegalArgumentException(SERVICE_ERROR + conn.getResponseCode());
            }

            //else, read the api response and convert it into proper model.
            final CityData cityData = readCityCoordsResponse(conn);
            //disconnect once done.
            conn.disconnect();

            //return the data
            return extractCoordinates(cityData);

        } catch (final Exception e) {
            //show stack trace on console in case of any other IO Exception
            e.printStackTrace();
            //nothing to return in this case.
            return null;
        }

    }

    /**
     * Calls distance matrix api
     * @param apiKey
     * @param srcDest
     * @return
     */
    public double findDistance(final String apiKey, final Coordinates[] srcDest) {
        try {
            if (srcDest == null || srcDest.length == 0) {
                throw new IllegalArgumentException("SOURCE AND DESTINATION ARE REQUIRED.");
            }

            //generate the actual URL for getting distance between two cities
            final StringBuilder sb = buildTimeOrDistanceRequest(srcDest);
            final HttpURLConnection conn = prepareConnectionForDistanceAPI(apiKey);

            try (final DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
                wr.writeBytes(sb.toString());
                wr.flush();
            }

            //if call to the API is unsuccessful, show error message.
            if (conn.getResponseCode() != 200) {
                throw new IllegalArgumentException(SERVICE_ERROR + conn.getResponseCode());
            }

            return extractMetricsDataFromResponse(conn);
        } catch (final Exception e) {
            //show stack trace on console in case of any other IO Exception
            e.printStackTrace();
            //nothing to return in this case.
            return 0;
        }
    }

    /**
     * Adds necessary parameters to connection
     * @param apiKey
     * @return
     * @throws IOException
     */
    private HttpURLConnection prepareConnectionForDistanceAPI(final String apiKey) throws IOException {
        final URL url = new URL(TIME_DISTANCE_URL);
        final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(POST);
        conn.setRequestProperty(CONTENT_TYPE, APPLICATION_JSON_UTF_8);
        conn.setRequestProperty(AUTHORIZATION, apiKey);
        conn.setRequestProperty(ACCEPT, APPLICATION_JSON);
        conn.setDoOutput(true);
        return conn;
    }

    /**
     * Extracts metrics information (distance) from api response.
     * @param conn
     * @return
     * @throws IOException
     */
    private double extractMetricsDataFromResponse(final HttpURLConnection conn) throws IOException {
        try (final BufferedReader in = new BufferedReader(
                new InputStreamReader(conn.getInputStream()))) {

            String line;
            final StringBuilder response = new StringBuilder();

            while ((line = in.readLine()) != null) {
                response.append(line);
            }

            //map the result to a pojo
            conn.disconnect();

            //return the data
            final Gson gson = new Gson();
             final Metrics metricsData = gson.fromJson(response.toString(), Metrics.class);
             if(metricsData == null
                     || metricsData.getDistances() == null
                     || metricsData.getDistances().length==0
                     || metricsData.getDistances()[0] == null
                     || metricsData.getDistances()[0][1] == null) {

                 throw new IllegalArgumentException("NO METRICS RESULT FOUND");
             }
            return Double.valueOf(metricsData.getDistances()[0][1]);
        }
    }

    /**
     * This methods build appropriate request for calling matrix api.
     * @param srcDest
     * @return
     */
    private StringBuilder buildTimeOrDistanceRequest(final Coordinates[] srcDest) {
        //request sample. Longitude is at index 0 in array e.g. [lng, lat], [lng, lat]...
        //{"locations":[[73.043415, 33.721545], [74.343554, 31.549671]],"metrics":["duration"]}
        final StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"locations\":[");
        for (int i = 0; i < srcDest.length; i++) {
            sb.append("[");
            sb.append(srcDest[i].getLng());
            sb.append(",");
            sb.append(srcDest[i].getLat());
            sb.append("]");
            if ((i + 1) < srcDest.length) {
                sb.append(",");
            }
        }
        sb.append("],");
        sb.append("\"metrics\":[");
        sb.append("\"" + DISTANCE + "\"");
        sb.append("]}");
        return sb;
    }

    /**
     * This method reads the response and converts it into CityData POJO
     * @param conn of type HttpURLConnection
     * @return CityData
     * @throws IOException
     */
    private CityData readCityCoordsResponse(final HttpURLConnection conn) throws IOException {
        final BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
        final Gson gson = new Gson();
        final String jsonStringResponse = br.readLine();
        CityData cityData = null;
        if (StringUtils.isNotBlank(jsonStringResponse)) {
            cityData = gson.fromJson(jsonStringResponse, CityData.class);
        }
        return cityData;
    }

    /**
     * This method appends required parameters to search api URL
     * @param apiKey
     * @param params
     * @return
     */
    private StringBuilder generateCityCoordinatesURL(final String apiKey, final String[] params) {
        final StringBuilder sb = new StringBuilder();
        sb.append(SEARCH_CITY_URL);
        sb.append(API_KEY_PARAM);
        sb.append(apiKey);
        sb.append(TEXT_PARAM);
        sb.append(params[0].toLowerCase().replaceAll(WHITE_SPACE, WHITE_SPACE_REPLACEMENT));
        if (params.length > 1 && StringUtils.isNotBlank(params[1])) {
            if (!Layers.exists(params[1])) {
                throw new IllegalArgumentException("INVALID LAYER PROVIDED: " + params[1]);
            }
            sb.append(LAYERS_PARAM);
            sb.append(params[1].toLowerCase().replaceAll(WHITE_SPACE, WHITE_SPACE_REPLACEMENT));
        }
        return sb;
    }

    /**
     * This method extracts coordinates from given city data.
     * @param cityData
     * @return
     */
    private Coordinates extractCoordinates(final CityData cityData) {
        if (cityData.getFeatures() != null && cityData.getFeatures().length > 0 && cityData.getFeatures()[0] != null) {
            //since result is sorted by confidence, therefore always picking up the first element.
            final Features feature = cityData.getFeatures()[0];
            if (feature.getGeometry() != null
                    && feature.getGeometry().getCoordinates() != null
                    && feature.getGeometry().getCoordinates().length > 0) {
                final String[] stringCoords = feature.getGeometry().getCoordinates();
                final Coordinates coordinates = new Coordinates(stringCoords[1], stringCoords[0]);
                return coordinates;
            }
        }
        throw new IllegalArgumentException("UNABLE TO FIND COORDINATES");
    }

    /**
     * Checks car type and returns relevant emission details.
     * @param carType
     * @return
     */
    public double findEmissionByCarType(final String carType) {
        final String[] carTypeArray = carType.trim().split("-");
        //get emission by car type in kilograms
        System.out.println("Finding vehicle type: "+carTypeArray[0]);
        double emission = 0;
        switch(carTypeArray[0].trim()) {
            case "small":
                emission = (SmallCarsEmissions.resolve(carType).getEmission());
                System.out.println("Car category is small with emission: "+emission+"g");
                return emission;
            case "medium":
                emission = (MediumCarsEmissions.resolve(carType).getEmission());
                System.out.println("Car category is medium with emission: "+emission+"g");
                return emission;
            case "large":
                emission = (LargeCarsEmissions.resolve(carType).getEmission());
                System.out.println("Car category is large with emission: "+emission+"g");
                return emission;
            case "bus":
                emission = (BusEmissions.resolve(carType).getEmission());
                System.out.println("Bus with emission: "+emission+"g");
                return emission;
            case "train":
                emission = (TrainEmissions.resolve(carType).getEmission());
                System.out.println("Train with emission: "+emission+"g");
                return emission;
            default:
                System.out.println("Transportation method not found...");
        }
        return emission;
    }

}
