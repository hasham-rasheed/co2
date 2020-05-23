package com.app.co2.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * @author Hasham Rasheed
 * 23-05-2020
 * Provides utility services to be used accross the Application.
 */
@Service
public class Util {

    private static final String EQUAL_SIGN = "=";
    private static final String SPLIT = "--";
    private static final String BLANK = "";
    private static final String START_KEY = "start";
    private static final String END_KEY = "end";
    private static final String TRANSPORT_KEY = "transportation-method";
    //fallback api key can be configured in application.properties and will be used if ORS_TOKEN environment variable is not found.
    @Value("${fallback.api.key}")
    private String fallbackApiKey;
    //the API KEY corresponding to ORS_TOKEN environment variable
    private String API_KEY;
    private static final String ORS_TOKEN = "ORS_TOKEN";


    /**
     * Checks whether API key is configured
     */
    public String resolveAPIKey() {
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
        return API_KEY;
    }

    /**
     * Extracts arguments correctly by handling white space and = in key/values
     * @param args
     * @return
     */
    public static String[] extractArgs(final String[] args) {
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
