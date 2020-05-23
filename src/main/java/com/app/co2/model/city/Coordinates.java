package com.app.co2.model.city;

import lombok.Data;

@Data
public class Coordinates {
    private String lat;
    private String lng;

    public Coordinates(final String lat, final String lng) {
        this.lat = lat;
        this.lng = lng;
    }
}
