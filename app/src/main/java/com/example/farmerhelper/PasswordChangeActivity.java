package com.example.farmerhelper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.sax.TextElementListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.farmerhelper.db.AccountDbManager;
import com.example.farmerhelper.models.AccountModel;
import com.example.farmerhelper.models.PasswordChangeModel;
import com.example.farmerhelper.models.Settings;
import com.example.farmerhelper.models.SignUpModel;
import com.example.farmerhelper.results.DataResult;
import com.example.farmerhelper.results.Result;

public class PasswordChangeActivity extends AppCompatActivity {
    EditText oldPassword, password, confirmPassword;
    TextView txtFullName;
    Button btnChangePassword;
    AccountDbManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_change);
        oldPassword = findViewById(R.id.txtOldPassword);
        password = findViewById(R.id.txtPassword);
        txtFullName = findViewById(R.id.txtFullName);
        confirmPassword = findViewById(R.id.txtConfirmPassword);
        btnChangePassword = findViewById(R.id.btnChangePassword);
        txtFullName.setText(Settings.fullName);
        dbManager = new AccountDbManager(this);
    }

    public void changePassword(View view) {

        String oldPassword = this.oldPassword.getText().toString().trim();
        String password = this.password.getText().toString().trim();
        String confirmPassword = this.confirmPassword.getText().toString().trim();

        if (oldPassword.length() == 0) {
            Toast.makeText(this, getString(R.string.please_enter_your_old_password), Toast.LENGTH_SHORT).show();
        } else if (password.length() == 0) {
            Toast.makeText(this, getString(R.string.please_enter_your_password), Toast.LENGTH_SHORT).show();
        } else if (!password.equals(confirmPassword)) {
            Toast.makeText(this, getString(R.string.passwords_are_not_same), Toast.LENGTH_SHORT).show();
        } else {
            Result result = dbManager.passwordChange(new PasswordChangeModel(Settings.accountId, oldPassword, password));
            if (!result.Success) {
                Toast.makeText(this, result.Message, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, result.Message, Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}