package com.app.co2.emissions;

import java.util.Arrays;

/**
 * @author Hasham Rasheed
 */
public enum TrainEmissions {

    TRAIN("train", 6);

    private String carType;
    private double emission;

    TrainEmissions(final String carType, final double emission) {
        this.carType = carType;
        this.emission = emission;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public double getEmission() {
        return emission;
    }

    public void setEmission(double emission) {
        this.emission = emission;
    }

    /**
     * Resolve to Enum by provided carType in String format
     * @param carType
     * @return TrainEmissions Enum
     */
    public static TrainEmissions resolve(final String carType) {
        return Arrays.stream(TrainEmissions.values())
                .filter(e -> e.getCarType().equals(carType))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(String.format("Unsupported type %s.", carType)));
    }
}
