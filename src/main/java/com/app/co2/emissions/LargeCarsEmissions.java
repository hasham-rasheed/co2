package com.app.co2.emissions;

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
        for(LargeCarsEmissions emission : LargeCarsEmissions.values()) {
            if(emission.getCarType().equalsIgnoreCase(carType)) {
                return emission;
            }
        }
        throw new IllegalArgumentException("Unsupported Car Type Provided: "+carType);
    }
}
