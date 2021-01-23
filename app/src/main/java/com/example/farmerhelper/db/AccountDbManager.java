package com.example.farmerhelper.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import androidx.annotation.Nullable;
import com.example.farmerhelper.R;
import com.example.farmerhelper.db.options.AccountOptions;
import com.example.farmerhelper.db.options.PlantOptions;
import com.example.farmerhelper.helpers.HashingHelpers;
import com.example.farmerhelper.models.AccountModel;
import com.example.farmerhelper.models.LoginModel;
import com.example.farmerhelper.models.PasswordChangeModel;
import com.example.farmerhelper.models.SignUpModel;
import com.example.farmerhelper.results.DataResult;
import com.example.farmerhelper.results.ErrorDataResult;
import com.example.farmerhelper.results.ErrorResult;
import com.example.farmerhelper.results.Result;
import com.example.farmerhelper.results.SuccessDataResult;
import com.example.farmerhelper.results.SuccessResult;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public class AccountDbManager extends DbManagerBase {

    //region Constructor
    private SQLiteDatabase db;
    Context context;
    public AccountDbManager(@Nullable Context context) {
        super(context);
        this.context=context;
        db = getWritableDatabase();
    }
    //endregion

    //region Db Read Operations
    public DataResult<AccountModel> signIn(LoginModel model) {
        SignInAsync signIn = new SignInAsync();
        return signIn.doInBackground(model);
    }

    private class SignInAsync extends AsyncTask<LoginModel, Void, DataResult<AccountModel>> {
        @Override
        protected DataResult<AccountModel> doInBackground(LoginModel... models) {
            return SignIn(models[0]);
        }

        private DataResult<AccountModel> SignIn(LoginModel model) {
            try {
                Cursor cursor = db.query(AccountOptions.TABLE_NAME,
                        null,
                        AccountOptions.COLUMN_EMAIL + " = ?",
                        new String[]{model.email},
                        null,
                        null,
                        null);

                if (cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {

                        String passwordHash = cursor.getString(cursor.getColumnIndex(AccountOptions.COLUMN_PASSWORD_HASH));
                        byte[] passwordSalt = cursor.getBlob(cursor.getColumnIndex(AccountOptions.COLUMN_PASSWORD_SALT));
                        String hashedPassword = HashingHelpers.getHash(model.password, passwordSalt);
                        if (!hashedPassword.equals(passwordHash))
                            return new ErrorDataResult<>(new Exception(context.getString(R.string.password_wrong)));

                        int id = cursor.getInt(cursor.getColumnIndex(AccountOptions._ID));
                        String fullName = cursor.getString(cursor.getColumnIndex(AccountOptions.COLUMN_FULL_NAME));
                        String email = cursor.getString(cursor.getColumnIndex(AccountOptions.COLUMN_EMAIL));
                        String gender = cursor.getString(cursor.getColumnIndex(AccountOptions.COLUMN_GENDER));
                        AccountModel accountModel = new AccountModel(id, fullName, email, gender);
                        return new SuccessDataResult<AccountModel>(accountModel, context.getString(R.string.sign_in_successfully));
                    }
                }
                return new ErrorDataResult<>(new Exception(context.getString(R.string.account_not_found)));
            } catch (Exception e) {
                return new ErrorDataResult<>(e);
            }
        }

        private boolean Exists(String email) {
            try {
                Cursor cursor = db.query(AccountOptions.TABLE_NAME,
                        null,
                        AccountOptions.COLUMN_EMAIL + " = '" + AccountOptions.COLUMN_EMAIL + "'",
                        null,
                        null,
                        null,
                        null);

                return cursor.getCount() > 0;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }
    //endregion

    //region Db Write Operations
    public DataResult<AccountModel> signUp(SignUpModel signUpModel) {
        SignInAsync signIn = new SignInAsync();
        if (signIn.Exists(signUpModel.email))
            return new ErrorDataResult<>(new Exception(context.getString(R.string.this_email_is_used)));

        SignUpAsync signUpAsync = new SignUpAsync();
        return signUpAsync.doInBackground(signUpModel);
    }

    public Result update(AccountModel accountModel) {
        SignUpAsync upAsync = new SignUpAsync();
        return upAsync.update(accountModel);
    }

    private class SignUpAsync extends AsyncTask<SignUpModel, Void, DataResult<AccountModel>> {

        @Override
        public DataResult<AccountModel> doInBackground(SignUpModel... signUpModels) {
            SignUpModel signUpModel = signUpModels[0];
            return signUp(signUpModel);
        }

        private DataResult<AccountModel> signUp(SignUpModel signUpModel) {
            byte[] passwordSalt = new byte[0];
            try {
                passwordSalt = HashingHelpers.getSalt();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchProviderException e) {
                e.printStackTrace();
            }
            String passwordHash = HashingHelpers.getHash(signUpModel.password, passwordSalt);
            ContentValues cv = new ContentValues();
            cv.put(AccountOptions.COLUMN_FULL_NAME, signUpModel.fullName);
            cv.put(AccountOptions.COLUMN_EMAIL, signUpModel.email);
            cv.put(AccountOptions.COLUMN_GENDER, signUpModel.gender);
            cv.put(AccountOptions.COLUMN_PASSWORD_HASH, passwordHash);
            cv.put(AccountOptions.COLUMN_PASSWORD_SALT, passwordSalt);
            try {
                long id = db.insert(AccountOptions.TABLE_NAME, null, cv);
                AccountModel accountModel = new AccountModel((int) id, signUpModel.fullName, signUpModel.email, signUpModel.gender);
                return new SuccessDataResult<AccountModel>(accountModel, context.getString(R.string.inserting_table_is_successfully));
            } catch (Exception e) {
                return new ErrorDataResult<>(e);
            }

        }

        private Result update(AccountModel accountModel) {
            ContentValues cv = new ContentValues();
            cv.put(AccountOptions.COLUMN_FULL_NAME, accountModel.fullName);
            cv.put(AccountOptions.COLUMN_EMAIL, accountModel.email);
            cv.put(AccountOptions.COLUMN_GENDER, accountModel.gender);
            try {
                db.update(AccountOptions.TABLE_NAME, cv,
                        AccountOptions._ID + "=?",
                        new String[]{String.valueOf(accountModel.id)}
                );
                return new SuccessResult(context.getString(R.string.updating_table_is_successfully));
            } catch (Exception e) {
                return new ErrorResult(e);
            }

        }

    }
    //endregion

    public Result passwordChange(PasswordChangeModel passwordChangeModel) {
        PasswordChangeAsync changeAsync = new PasswordChangeAsync();
        return changeAsync.doInBackground(passwordChangeModel);
    }

    private class PasswordChangeAsync extends AsyncTask<PasswordChangeModel, Void, Result> {

        @Override
        protected Result doInBackground(PasswordChangeModel... passwordChangeModels) {
            PasswordChangeModel model = passwordChangeModels[0];
            return passwordChange(model);
        }

        private Result passwordChange(PasswordChangeModel model) {
            Cursor cursor = db.query(AccountOptions.TABLE_NAME,
                    null,
                    PlantOptions._ID + " = " + model.accountId,
                    null,
                    null,
                    null,
                    null);

            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    byte[] salt = cursor.getBlob(cursor.getColumnIndex(AccountOptions.COLUMN_PASSWORD_SALT));
                    String password = cursor.getString(cursor.getColumnIndex(AccountOptions.COLUMN_PASSWORD_HASH));
                    String oldPassword = HashingHelpers.getHash(model.oldPassword, salt);
                    if (!password.equals(oldPassword))
                        return new ErrorResult(new Exception("Old password is wrong"));

                    String newPwd = HashingHelpers.getHash(model.password, salt);
                    ContentValues cv = new ContentValues();
                    cv.put(AccountOptions.COLUMN_PASSWORD_HASH,newPwd);
                    try {
                        db.update(AccountOptions.TABLE_NAME, cv,
                                AccountOptions._ID + "=?",
                                new String[]{String.valueOf(model.accountId)}
                        );
                        return new SuccessResult("Password  is changed Successfully");
                    } catch (Exception e) {
                        return new ErrorResult(e);
                    }
                }
            }
            return new ErrorResult(new Exception("account not found"));
        }
    }
}
