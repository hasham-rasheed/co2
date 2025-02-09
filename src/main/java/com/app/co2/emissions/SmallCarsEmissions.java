package com.app.co2.emissions;

import java.util.Arrays;

public enum SmallCarsEmissions {

    SMALL_DIESEL_CAR("small-diesel-car", 142),
    SMALL_PETROL_CAR("small-petrol-car", 142),
    SMALL_PLUGIN_HYBRID_CAR("small-plugin-hybrid-car", 142),
    SMALL_ELECTRIC_CAR("small-electric-car", 142);

    private String carType;
    private double emission;


    SmallCarsEmissions(final String carType, final double emission) {
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
    public static SmallCarsEmissions resolve(final String carType) {
        return Arrays.stream(SmallCarsEmissions.values())
                .filter(e -> e.getCarType().equals(carType))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(String.format("Unsupported type %s.", carType)));
    }
}
