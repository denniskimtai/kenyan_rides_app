package com.example.kenyanrides;

public class user {

    private int id, national_id;
    private String first_name;
    private String second_name;
    private String email;
    private String mobile_number;
    private String reg_date;
    private String dob;
    private String address;
    private String city;

    private String country;

    public user(int id, int national_id, String first_name, String second_name, String email,
                String mobile_number, String reg_date, String dob, String address, String city, String country) {
        this.id = id;
        this.national_id = national_id;
        this.first_name = first_name;
        this.email = email;
        this.second_name = second_name;
        this.mobile_number = mobile_number;
        this.reg_date = reg_date;
        this.dob = dob;
        this.address = address;
        this.city = city;
        this.country = country;
    }

    public int getId() {
        return id;
    }

    public int getNational_id() {
        return national_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getSecond_name() {
        return second_name;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public String getReg_date() {
        return reg_date;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}

