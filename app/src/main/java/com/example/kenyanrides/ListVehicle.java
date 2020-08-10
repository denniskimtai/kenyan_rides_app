package com.example.kenyanrides;

public class ListVehicle {

    private String listedVehicleName;
    private int listedVehiclePrice;
    private int listedVehicleImage;



    public ListVehicle(String listedVehicleName, int listedVehiclePrice, int listedVehicleImage) {
        this.listedVehicleName = listedVehicleName;
        this.listedVehiclePrice = listedVehiclePrice;
        this.listedVehicleImage = listedVehicleImage;
    }


    public String getListedVehicleName() {
        return listedVehicleName;
    }

    public void setListedVehicleName(String listedVehicleName) {
        this.listedVehicleName = listedVehicleName;
    }

    public int getListedVehiclePrice() {
        return listedVehiclePrice;
    }

    public void setListedVehiclePrice(int listedVehiclePrice) {
        this.listedVehiclePrice = listedVehiclePrice;
    }

    public int getListedVehicleImage() {
        return listedVehicleImage;
    }

    public void setListedVehicleImage(int listedVehicleImage) {
        this.listedVehicleImage = listedVehicleImage;
    }




}
