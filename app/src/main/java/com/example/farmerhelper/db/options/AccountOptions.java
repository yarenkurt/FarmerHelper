package com.example.farmerhelper.db.options;

import android.provider.BaseColumns;

public final class AccountOptions implements BaseColumns {
    public static final String TABLE_NAME = "Accounts";
    public static final String COLUMN_FULL_NAME = "FullName";
    public static final String COLUMN_EMAIL = "Email";
    public static final String COLUMN_PASSWORD_HASH = "PasswordHash";
    public static final String COLUMN_PASSWORD_SALT = "PasswordSalt";
    public static final String COLUMN_GENDER = "Gender";

    public static final String DROP_SQL ="DROP TABLE IF EXISTS " + TABLE_NAME;

    public static final String CREATE_SQL =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                    " (" +
                    _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_FULL_NAME + " TEXT NOT NULL, " +
                    COLUMN_EMAIL + " TEXT NOT NULL, " +
                    COLUMN_GENDER + " TEXT NOT NULL, " +
                    COLUMN_PASSWORD_HASH + " TEXT NOT NULL, " +
                    COLUMN_PASSWORD_SALT + " BLOB" +
                    "); ";
}
