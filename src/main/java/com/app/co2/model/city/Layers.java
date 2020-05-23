package com.app.co2.model.city;

public enum Layers {

    VENUE("venue"),
    STREET("street"),
    CONTINENT("continent"),
    EMPIRE("empire"),
    DEPENDENCY("dependency"),
    MACROCOUNTY("macrocounty"),
    MACROHOOD("macrohood"),
    MICROHOOD("microhood"),
    DISPUTED("disputed"),
    POSTALCODE("postalcode"),
    OCEAN("ocean"),
    MARINEAREA("marinearea"),
    COUNTRY("country"),
    MACROREGION("macroregion"),
    REGION("region"),
    COUNTY("county"),
    LOCALADMIN("localadmin"),
    LOCALITY("locality"),
    BOROUGH("borough"),
    NEIGHBOURHOOD("neighbourhood");

    private String layerName;

    Layers(final String layerName) {
        this.layerName = layerName;
    }

    public String getLayerName() {
        return layerName;
    }

    public void setLayerName(String layerName) {
        this.layerName = layerName;
    }

    //Since we cannot override valueOf method of enums, a custom method is needed.
    public static boolean exists(final String layerName) {

        for (final Layers layer : Layers.values()) {
            if (layer.getLayerName().equalsIgnoreCase(layerName)) {
                return true;
            }
        }

        return false;
    }


}
