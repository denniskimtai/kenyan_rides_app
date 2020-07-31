package com.example.kenyanrides;

public class user {

    private int id, national_id;
    private String first_name, second_name, email, mobile_number, reg_date;

    public user(int id, int national_id, String first_name, String second_name, String email, String mobile_number, String reg_date) {
        this.id = id;
        this.national_id = national_id;
        this.first_name = first_name;
        this.email = email;
        this.second_name = second_name;
        this.mobile_number = mobile_number;
        this.reg_date = reg_date;
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
}

