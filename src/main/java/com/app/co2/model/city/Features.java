package com.app.co2.model.city;

import lombok.Data;

@Data
public class Features
{
    private String[] bbox;

    private Geometry geometry;

    private String type;

    private Properties properties;

}
