package com.app.co2.emissions;

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
        for(MediumCarsEmissions emission : MediumCarsEmissions.values()) {
            if(emission.getCarType().equalsIgnoreCase(carType)) {
                return emission;
            }
        }
        throw new IllegalArgumentException("Unsupported Car Type Provided: "+carType);
    }
}
