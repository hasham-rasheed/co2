package com.app.co2.model.city;


import lombok.Data;

@Data
public class CityData
{
    private Features[] features;

    private Geocoding geocoding;

    private String[] bbox;

    private String type;

    public Features[] getFeatures ()
    {
        return features;
    }
}