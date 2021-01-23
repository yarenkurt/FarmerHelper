package com.example.farmerhelper.results;

public class DataResult<T>  extends Result {
    public T Data;

    public DataResult(T data,boolean success, String message) {
        super(success, message);
        Data=data;
    }
}
