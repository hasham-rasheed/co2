package com.app.co2.model.metrics;

import lombok.Data;

@Data
public class Query {

    private String responseType;

    private String profile;

    private String[][] locations;

    private String[] metrics;

    private String[] metricsStrings;

}
