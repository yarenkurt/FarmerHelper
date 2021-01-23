package com.example.farmerhelper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.farmerhelper.db.AccountDbManager;
import com.example.farmerhelper.models.AccountModel;
import com.example.farmerhelper.models.Settings;
import com.example.farmerhelper.results.Result;
import com.google.android.material.snackbar.Snackbar;

public class ProfileActivity extends AppCompatActivity {
    EditText txtFullName,txtEmail,txtGender;
AccountDbManager dbManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        txtFullName=findViewById(R.id.txtFullName);
        txtEmail=findViewById(R.id.txtEmail);
        txtGender=findViewById(R.id.txtGender);

        txtFullName.setText(Settings.fullName);
        txtEmail.setText(Settings.email);
        txtGender.setText(Settings.gender);
        dbManager=new AccountDbManager(this);
    }

    public void save(View view) {
        String fullName=txtFullName.getText().toString();
        String email=txtEmail.getText().toString();
        String gender=txtGender.getText().toString();
        Result result=dbManager.update(new AccountModel(Settings.accountId,fullName,email,gender));
        if (!result.Success){
            Snackbar.make(view, result.Message, Snackbar.LENGTH_LONG).setAction("", null).show();
            Toast.makeText(this, result.Message, Toast.LENGTH_SHORT).show();
            return;
        }
        Settings.fullName=fullName;
        Settings.email=email;
        Settings.gender=gender;
        finish();
    }
}