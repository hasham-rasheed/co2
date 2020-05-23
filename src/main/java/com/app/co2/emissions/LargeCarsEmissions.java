package com.app.co2.emissions;

import java.util.Arrays;

public enum LargeCarsEmissions {
    
    LARGE_DIESEL_CAR("large-diesel-car", 142),
    LARGE_PETROL_CAR("large-petrol-car", 142),
    LARGE_PLUGIN_HYBRID_CAR("large-plugin-hybrid-car", 142),
    LARGE_ELECTRIC_CAR("large-electric-car", 142);

    private String carType;
    private double emission;


    LargeCarsEmissions(final String carType, final double emission) {
        this.carType = carType;
        this.emission = emission;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(final String carType) {
        this.carType = carType;
    }

    public double getEmission() {
        return emission;
    }

    public void setEmission(final double emission) {
        this.emission = emission;
    }

    /**
     * Resolve to Enum by provided carType in String format
     * @param carType
     * @return SmallCarsEmissions Enum
     */
    public static LargeCarsEmissions resolve(final String carType) {
        return Arrays.stream(LargeCarsEmissions.values())
                .filter(e -> e.getCarType().equals(carType))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(String.format("Unsupported type %s.", carType)));
    }
}
