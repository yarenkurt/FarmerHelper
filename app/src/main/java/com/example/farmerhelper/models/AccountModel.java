package com.example.farmerhelper.models;

public class AccountModel {
    public int id;
    public String fullName;
    public String email;
    public String gender;

    public AccountModel(int id, String fullName, String email, String gender) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.gender = gender;
    }
}
