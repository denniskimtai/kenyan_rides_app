package com.example.kenyanrides;

public class VehicleBookings {

    private String vehicleName;
    private String fromDate;
    private String toDate;
    private String travelDestination;
    private String vehicleStatus;
    private int priceDue;
    private String vehicleImage;
    private int numberOfBookedVehicles;
    private int id;
    private String BrandName;


    public VehicleBookings(String vehicleName, String fromDate, String toDate, String travelDestination, int priceDue, String vehicleImage,
                           String vehicleStatus, int numberOfBookedVehicles, int id, String BrandName) {
        this.vehicleName = vehicleName;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.travelDestination = travelDestination;
        this.vehicleImage = vehicleImage;
        this.priceDue = priceDue;
        this.vehicleStatus = vehicleStatus;
        this.numberOfBookedVehicles = numberOfBookedVehicles;
        this.id = id;
        this.BrandName = BrandName;
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

    public String getVehicleImage() {
        return vehicleImage;
    }

    public void setVehicleImage(String vehicleImage) {
        this.vehicleImage = vehicleImage;
    }

    public int getPriceDue() {
        return priceDue;
    }

    public void setPriceDue(int priceDue) {
        this.priceDue = priceDue;
    }

    public String getVehicleStatus() {
        return vehicleStatus;
    }

    public void setVehicleStatus(String vehicleStatus) {
        this.vehicleStatus = vehicleStatus;
    }

    public int getNumberOfBookedVehicles() {
        return numberOfBookedVehicles;
    }

    public void setNumberOfBookedVehicles(int numberOfBookedVehicles) {
        this.numberOfBookedVehicles = numberOfBookedVehicles;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBrandName() {
        return BrandName;
    }

    public void setBrandName(String brandName) {
        BrandName = brandName;
    }
}
