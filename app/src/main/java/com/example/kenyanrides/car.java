package com.example.kenyanrides;

public class car {

    public int image;
    public String carName;
    public String carPrice;


    public car(){

    }

    public car(int image, String carName, String carPrice){

        this.image =image;
        this.carName = carName;
        this.carPrice =carPrice;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getCarPrice() {
        return carPrice;
    }

    public void setCarPrice(String carPrice) {
        this.carPrice = carPrice;
    }
}
