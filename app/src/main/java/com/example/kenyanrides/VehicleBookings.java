package com.example.kenyanrides;

public class VehicleBookings {

    private String vehicleName;
    private String fromDate;
    private String toDate;
    private String travelDestination;
    private int priceDue;
    private int vehicleImage;


    public VehicleBookings(String vehicleName, String fromDate, String toDate, String travelDestination, int priceDue, int vehicleImage) {
        this.vehicleName = vehicleName;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.travelDestination = travelDestination;
        this.vehicleImage = vehicleImage;
        this.priceDue = priceDue;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getTravelDestination() {
        return travelDestination;
    }

    public void setTravelDestination(String travelDestination) {
        this.travelDestination = travelDestination;
    }

    public int getVehicleImage() {
        return vehicleImage;
    }

    public void setVehicleImage(int vehicleImage) {
        this.vehicleImage = vehicleImage;
    }

    public int getPriceDue() {
        return priceDue;
    }

    public void setPriceDue(int priceDue) {
        this.priceDue = priceDue;
    }
}
