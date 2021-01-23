package com.example.farmerhelper.models;

public class PasswordChangeModel {
    public int accountId;
    public String oldPassword;
    public String password;

    public PasswordChangeModel(int accountId, String oldPassword, String password) {
        this.accountId = accountId;
        this.oldPassword = oldPassword;
        this.password = password;
    }
}
