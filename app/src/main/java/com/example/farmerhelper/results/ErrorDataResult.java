package com.example.farmerhelper.results;

public class ErrorDataResult<T> extends DataResult<T> {

    public ErrorDataResult(Exception e) {
        super(null, false, e.getMessage());
    }
}
