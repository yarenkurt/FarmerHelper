package com.example.farmerhelper.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import androidx.annotation.Nullable;

import com.example.farmerhelper.R;
import com.example.farmerhelper.db.options.DbOptions;
import com.example.farmerhelper.db.options.PlantOptions;
import com.example.farmerhelper.models.PlantModel;
import com.example.farmerhelper.results.ErrorResult;
import com.example.farmerhelper.results.Result;
import com.example.farmerhelper.results.SuccessResult;

import java.util.Locale;

public class PlantDbManager extends DbManagerBase {

    //region Constructor
    private SQLiteDatabase db;
    Context context;

    public PlantDbManager(@Nullable Context context) {
        super(context);
        this.context = context;
        db = getWritableDatabase();
    }
    //endregion

    //region Db Read Operations
    private static String langCode() {
        return Locale.getDefault().getLanguage();
    }

    public Cursor getAll(int plantType) {
        GetAllAsync getAllAsync = new GetAllAsync();
        return getAllAsync.doInBackground(plantType);
    }

    public Cursor get(int id) {
        GetAsync getAsync = new GetAsync();
        //Locale.getDefault().getDisplayLanguage()
        return getAsync.doInBackground(id);
    }

    private class GetAsync extends AsyncTask<Integer, Void, Cursor> {
        @Override
        protected Cursor doInBackground(Integer... integers) {
            return get(integers[0]);
        }

        private Cursor get(int id) {
            return db.query(PlantOptions.TABLE_NAME,
                    null,
                    PlantOptions._ID + " = " + id,
                    null,
                    null,
                    null,
                    null);
        }
    }

    private class GetAllAsync extends AsyncTask<Integer, Void, Cursor> {
        @Override
        protected Cursor doInBackground(Integer... integers) {
            return getAll(integers[0]);
        }


        private Cursor getAll(int plantType) {
            return db.query(PlantOptions.TABLE_NAME,
                    null,
                    PlantOptions.COLUMN_PLANT_TYPE + " = '" + plantType + "'"
                            + " and " + PlantOptions.COLUMN_LANG_CODE + " ='" + langCode() + "'"
                    ,
                    null,
                    null,
                    null,
                    PlantOptions.COLUMN_TITLE + " ASC");
        }
    }
    //endregion

    //region Db Write Operations
    public Result insert(PlantModel plantModel) {
        WriteAsync writeAsync = new WriteAsync();
        plantModel.Id = 0;
        return writeAsync.insert(plantModel);
    }

    public Result update(PlantModel plantModel) {
        if (plantModel.Id == 0)
            return new ErrorResult(new Exception(context.getString(R.string.id_column_is_more_than_zero)));
        WriteAsync writeAsync = new WriteAsync();
        return writeAsync.update(plantModel);
    }

    private class WriteAsync extends AsyncTask<PlantModel, Void, Result> {

        @Override
        public Result doInBackground(PlantModel... plantsModels) {
            PlantModel plantModel = plantsModels[0];
            if (plantModel.Id == 0)
                return insert(plantModel);
            return update(plantModel);
        }

        private Result insert(PlantModel plantModel) {
            ContentValues cv = new ContentValues();
            cv.put(PlantOptions.COLUMN_TITLE, plantModel.Title);
            cv.put(PlantOptions.COLUMN_LANG_CODE, langCode());
            cv.put(PlantOptions.COLUMN_PLANT_TYPE, plantModel.PlantType);
            cv.put(PlantOptions.COLUMN_MORBIDITY, plantModel.Morbidity);
            cv.put(PlantOptions.COLUMN_LIFECYCLE, plantModel.Lifecycle);
            cv.put(PlantOptions.COLUMN_IMAGE, plantModel.Image);
            try {
                db.insert(PlantOptions.TABLE_NAME, null, cv);
                return new SuccessResult(context.getString(R.string.inserting_table_is_successfully));
            } catch (Exception e) {
                return new ErrorResult(e);
            }

        }

        private Result update(PlantModel plantModel) {
            ContentValues cv = new ContentValues();
            cv.put(PlantOptions.COLUMN_TITLE, plantModel.Title);
            cv.put(PlantOptions.COLUMN_PLANT_TYPE, plantModel.PlantType);
            cv.put(PlantOptions.COLUMN_MORBIDITY, plantModel.Morbidity);
            cv.put(PlantOptions.COLUMN_LIFECYCLE, plantModel.Lifecycle);
            cv.put(PlantOptions.COLUMN_IMAGE, plantModel.Image);
            try {
                db.update(PlantOptions.TABLE_NAME, cv,
                        PlantOptions._ID + "=?",
                        new String[]{String.valueOf(plantModel.Id)}
                );
                return new SuccessResult(context.getString(R.string.updating_table_is_successfully));
            } catch (Exception e) {
                return new ErrorResult(e);
            }
        }
    }
    //endregion

    //region Delete Operation
    public Result delete(int id) {
        if (id == 0)
            return new ErrorResult(new Exception(context.getString(R.string.id_column_is_more_than_zero)));
        DeleteAsync deleteAsync = new DeleteAsync();
        return deleteAsync.doInBackground(id);
    }

    private class DeleteAsync extends AsyncTask<Integer, Void, Result> {

        @Override
        protected Result doInBackground(Integer... integers) {
            int id = integers[0];
            try {
                db.delete(PlantOptions.TABLE_NAME, PlantOptions._ID + "=?", new String[]{String.valueOf(id)});
                return new SuccessResult(context.getString(R.string.deleting_item_is_successfully));
            } catch (Exception e) {
                return new ErrorResult(e);
            }
        }
    }
    //endregion

    //region Seed Data
    public void seedData() {
        SeedAsync seedAsync = new SeedAsync();
        seedAsync.doInBackground();
    }

    private class SeedAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            seed();
            return null;
        }

        private void seed() {
            Cursor cursor = db.query(PlantOptions.TABLE_NAME,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null);
            if (cursor.getCount() == 0) {
                generate("Ağaç", 0,"tr");
                generate("Çiçek", 1,"tr");
                generate("Sebze", 2,"tr");
                generate("Meyve", 3,"tr");

                generate("Tree", 0,"en");
                generate("Flower", 1,"en");
                generate("Vegetable", 2,"en");
                generate("Fruit", 3,"en");
            }
        }

        private void generate(String Title, int plantType, String langCode) {
            for (int i = 1; i <= 9; i++) {
                ContentValues cv = new ContentValues();
                cv.put(PlantOptions.COLUMN_TITLE, Title + " 0" + i);
                cv.put(PlantOptions.COLUMN_LANG_CODE, langCode);
                cv.put(PlantOptions.COLUMN_PLANT_TYPE, plantType);
                cv.put(PlantOptions.COLUMN_MORBIDITY, "Lorem ipsum dolor sit amet, consectetur adipiscing elit 0" + i);
                cv.put(PlantOptions.COLUMN_LIFECYCLE, "Nam luctus sapien a sollicitudin varius 0" + i);
                cv.put(PlantOptions.COLUMN_IMAGE, (byte[]) null);
                try {
                    db.insert(PlantOptions.TABLE_NAME, null, cv);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    //endregion
}
