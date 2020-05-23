package com.app.co2.emissions;

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
        for(SmallCarsEmissions emission : SmallCarsEmissions.values()) {
            if(emission.getCarType().equalsIgnoreCase(carType)) {
                return emission;
            }
        }
        throw new IllegalArgumentException("Unsupported Car Type Provided: "+carType);
    }
}
