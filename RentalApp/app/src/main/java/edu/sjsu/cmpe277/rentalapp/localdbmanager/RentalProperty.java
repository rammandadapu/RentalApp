package edu.sjsu.cmpe277.rentalapp.localdbmanager;

/**
 * Created by divya.chittimalla on 4/26/16.
 */
public class RentalProperty {
    String propertyId;
    String name;

    public RentalProperty() {
    }

    public RentalProperty(String propertyId, String name) {
        this.propertyId = propertyId;
        this.name = name;
    }

    public String getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
