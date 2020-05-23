package com.app.co2.model.city;


import lombok.Data;

@Data
public class Geocoding
{
    private Engine engine;

    private Query query;

    private String[] warnings;

    private String attribution;

    private String version;

    private String timestamp;

}

