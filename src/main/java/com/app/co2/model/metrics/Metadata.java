package com.app.co2.model.metrics;

import lombok.Data;

@Data
public class Metadata {
    private Engine engine;

    private String service;

    private Query query;

    private String attribution;

    private String timestamp;

}
