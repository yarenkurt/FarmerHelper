package com.example.farmerhelper.models;

public class PlantModel {
    public int Id;
    public String Title;
    public int PlantType;
    public String Morbidity;
    public String Lifecycle;
    public byte[] Image;

    public PlantModel(int id, String title, int plantType, String morbidity, String lifecycle, byte[] image) {
        Id = id;
        Title = title;
        PlantType = plantType;
        Morbidity = morbidity;
        Lifecycle = lifecycle;
        Image = image;
    }
}
