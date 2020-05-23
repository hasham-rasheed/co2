package com.app.co2.model.metrics;

import lombok.Data;

@Data
public class Metrics
{
    private String[][] distances;

    private Metadata metadata;

    private Sources[] sources;

    private Destinations[] destinations;

}
