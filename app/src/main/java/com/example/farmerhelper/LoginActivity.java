package com.example.farmerhelper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.farmerhelper.db.AccountDbManager;
import com.example.farmerhelper.db.PlantDbManager;
import com.example.farmerhelper.models.AccountModel;
import com.example.farmerhelper.models.LoginModel;
import com.example.farmerhelper.models.Settings;
import com.example.farmerhelper.results.DataResult;
import com.example.farmerhelper.results.Result;

import java.util.Locale;

public class LoginActivity extends AppCompatActivity {
    Button btnLogin;
    ProgressBar loading;
    EditText email, password;
    AccountDbManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnLogin = findViewById(R.id.btnLogin);
        loading = findViewById(R.id.loading);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        email.setText("abdi.kurt@yeditepesoft.com");
        password.setText("124578");
        dbManager = new AccountDbManager(this);

    }

    public void login(View view) {
        String email = this.email.getText().toString().trim();
        String password = this.password.getText().toString().trim();

        if (email.length() == 0 || !email.contains("@") || !email.contains(".")) {
            Toast.makeText(this, getString(R.string.please_enter_valid_email), Toast.LENGTH_SHORT).show();
        } else if (password.length() == 0) {
            Toast.makeText(this, getString(R.string.please_enter_your_password), Toast.LENGTH_SHORT).show();
        } else {
            loading.setVisibility(View.VISIBLE);
            btnLogin.setVisibility(View.INVISIBLE);
            DataResult<AccountModel> result = dbManager.signIn(new LoginModel(email, password));
            if (!result.Success) {
                Toast.makeText(this, result.Message, Toast.LENGTH_SHORT).show();
                loading.setVisibility(View.INVISIBLE);
                btnLogin.setVisibility(View.VISIBLE);
            } else {



                Toast.makeText(this, result.Message, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                Settings.accountId = result.Data.id;
                Settings.fullName = result.Data.fullName;
                Settings.email = result.Data.email;
                Settings.gender = result.Data.gender;
                startActivity(intent);
                finish();
            }
        }
    }

    public void goRegister(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }
}