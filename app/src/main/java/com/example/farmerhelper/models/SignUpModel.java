package com.example.farmerhelper.models;

public class SignUpModel {
    public String fullName;
    public String email;
    public String gender;
    public String password;

    public SignUpModel(String fullName, String email, String gender, String password) {
        this.fullName = fullName;
        this.email = email;
        this.gender = gender;
        this.password = password;
    }
}
