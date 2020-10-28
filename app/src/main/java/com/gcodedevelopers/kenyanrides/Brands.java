package com.gcodedevelopers.kenyanrides;

public class Brands {

    private int id;
    private String brandName;

    public Brands(int id, String brandName){
        this.id = id;
        this.brandName = brandName;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brand) {
        this.brandName = brand;
    }


}
