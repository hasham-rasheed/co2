package com.app.co2.emissions;

import java.util.Arrays;

public enum MediumCarsEmissions {

    MEDIUM_DIESEL_CAR("medium-diesel-car", 142),
    MEDIUM_PETROL_CAR("medium-petrol-car", 142),
    MEDIUM_PLUGIN_HYBRID_CAR("medium-plugin-hybrid-car", 142),
    MEDIUM_ELECTRIC_CAR("medium-electric-car", 142);

    private String carType;
    private double emission;


    MediumCarsEmissions(final String carType, final double emission) {
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
    public static MediumCarsEmissions resolve(final String carType) {
        return Arrays.stream(MediumCarsEmissions.values())
                .filter(e -> e.getCarType().equals(carType))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(String.format("Unsupported type %s.", carType)));
    }
}
