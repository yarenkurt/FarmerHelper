package com.example.farmerhelper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.farmerhelper.R;
import com.example.farmerhelper.db.AccountDbManager;
import com.example.farmerhelper.models.AccountModel;
import com.example.farmerhelper.models.LoginModel;
import com.example.farmerhelper.models.Settings;
import com.example.farmerhelper.models.SignUpModel;
import com.example.farmerhelper.results.DataResult;
import com.example.farmerhelper.results.Result;

public class RegisterActivity extends AppCompatActivity {
    Button btnRegister;
    ProgressBar loading;
    EditText fullName, email, password, confirmPassword;
    AccountDbManager dbManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        btnRegister = findViewById(R.id.btnRegister);
        loading = findViewById(R.id.loading);

        fullName = findViewById(R.id.txtFullName);
        email = findViewById(R.id.txtEmail);
        password = findViewById(R.id.txtPassword);
        confirmPassword = findViewById(R.id.txtConfirmPassword);

        dbManager = new AccountDbManager(this);
    }

    public void register(View view) {

        String fullName = this.fullName.getText().toString().trim();
        String email = this.email.getText().toString().trim();
        String password = this.password.getText().toString().trim();
        String confirmPassword = this.confirmPassword.getText().toString().trim();

        if (fullName.length() < 5) {
            Toast.makeText(this, getString(R.string.full_name_should_be_more_than_4_characters), Toast.LENGTH_SHORT).show();
        } else if (email.length() == 0 || !email.contains("@") || !email.contains(".")) {
            Toast.makeText(this, R.string.please_enter_valid_email, Toast.LENGTH_SHORT).show();
        } else if (password.length() == 0) {
            Toast.makeText(this, R.string.please_enter_your_password, Toast.LENGTH_SHORT).show();
        } else if (!password.equals(confirmPassword)) {
            Toast.makeText(this, R.string.passwords_are_not_same, Toast.LENGTH_SHORT).show();
        } else {
            loading.setVisibility(View.VISIBLE);
            btnRegister.setVisibility(View.INVISIBLE);

            DataResult<AccountModel> result = dbManager.signUp(new SignUpModel(fullName,email,"Male",password));
            if (!result.Success) {
                Toast.makeText(this, result.Message, Toast.LENGTH_SHORT).show();
                loading.setVisibility(View.INVISIBLE);
                btnRegister.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(this, result.Message, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                Settings.accountId = result.Data.id;
                Settings.fullName = result.Data.fullName;
                Settings.email = result.Data.email;
                Settings.gender = result.Data.gender;
                startActivity(intent);
                finish();
            }

        }

    }

    public void goLogin(View view) {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}