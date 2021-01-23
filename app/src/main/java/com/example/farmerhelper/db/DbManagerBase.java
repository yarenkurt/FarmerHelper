package com.example.farmerhelper.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;

import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import com.example.farmerhelper.db.options.AccountOptions;
import com.example.farmerhelper.db.options.DbOptions;
import com.example.farmerhelper.db.options.PlantOptions;

public class DbManagerBase extends SQLiteOpenHelper {

    //region Constructor
    private SQLiteDatabase db;

    public DbManagerBase(@Nullable Context context) {
        super(context, DbOptions.DB_NAME, null, DbOptions.VERSION);
        db = context.openOrCreateDatabase(DbOptions.DB_NAME, Context.MODE_PRIVATE, null);
    }
    //endregion

    //region Create and Upgrade Operations
    @Override
    public void onCreate(SQLiteDatabase db) {

        try {
            db.execSQL(AccountOptions.CREATE_SQL);
            db.execSQL(PlantOptions.CREATE_SQL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL(AccountOptions.DROP_SQL);
            db.execSQL(PlantOptions.DROP_SQL);
            onCreate(db);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //endregion
}
