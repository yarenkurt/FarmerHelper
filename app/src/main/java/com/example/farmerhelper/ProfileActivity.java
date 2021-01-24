package com.example.farmerhelper;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.farmerhelper.db.AccountDbManager;
import com.example.farmerhelper.models.AccountModel;
import com.example.farmerhelper.models.Settings;
import com.example.farmerhelper.results.Result;
import com.google.android.material.snackbar.Snackbar;

public class
ProfileActivity extends AppCompatActivity {
    EditText txtFullName, txtEmail;
    Spinner txtGender;
    AccountDbManager dbManager;
    ArrayAdapter<String> genderAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        txtFullName = findViewById(R.id.txtFullName);
        txtEmail = findViewById(R.id.txtEmail);
        txtGender = findViewById(R.id.txtGender);

        txtFullName.setText(Settings.fullName);
        txtEmail.setText(Settings.email);
        dbManager = new AccountDbManager(this);
        genderAdapter=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.genders));
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        txtGender.setAdapter(genderAdapter);
        txtGender.setSelection(genderAdapter.getPosition(Settings.gender));
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public void save(View view) {
        String fullName = txtFullName.getText().toString();
        String email = txtEmail.getText().toString();
        String gender = txtGender.getSelectedItem().toString();
        Result result = dbManager.update(new AccountModel(Settings.accountId, fullName, email, gender));
        if (!result.Success) {
            Snackbar.make(view, result.Message, Snackbar.LENGTH_LONG).setAction("", null).show();
            Toast.makeText(this, result.Message, Toast.LENGTH_SHORT).show();
            return;
        }
        Settings.fullName = fullName;
        Settings.email = email;
        Settings.gender = gender;
        finish();
    }
}