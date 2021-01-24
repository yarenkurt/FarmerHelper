package com.example.farmerhelper.db.options;

import android.provider.BaseColumns;

public final class PlantOptions implements BaseColumns {

    public static final String TABLE_NAME = "Plants";
    public static final String COLUMN_LANG_CODE = "LangCode";
    public static final String COLUMN_TITLE = "Title";
    public static final String COLUMN_PLANT_TYPE = "PlantType";
    public static final String COLUMN_MORBIDITY = "Morbidity";
    public static final String COLUMN_LIFECYCLE = "Lifecycle";
    public static final String COLUMN_IMAGE = "Image";

    public static final String DROP_SQL ="DROP TABLE IF EXISTS " + TABLE_NAME;
    public static final String CREATE_SQL =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                    " (" +
                    _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_LANG_CODE + " TEXT NOT NULL, " +
                    COLUMN_TITLE + " TEXT NOT NULL, " +
                    COLUMN_PLANT_TYPE + " TEXT NOT NULL, " +
                    COLUMN_MORBIDITY + " TEXT NOT NULL, " +
                    COLUMN_LIFECYCLE + " TEXT NOT NULL, " +
                    COLUMN_IMAGE + " BLOB " +
                    "); ";
}

