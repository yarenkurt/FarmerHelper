package com.example.farmerhelper.results;

public class ErrorResult extends Result {

    public ErrorResult(Exception e) {
        super(false, e.getMessage());
    }
}
