package com.example.farmerhelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.farmerhelper.db.PlantDbManager;
import com.example.farmerhelper.db.options.PlantOptions;
import com.example.farmerhelper.helpers.HtmlHelper;
import com.example.farmerhelper.models.PlantModel;
import com.example.farmerhelper.models.Settings;
import com.example.farmerhelper.results.Result;
import com.google.android.material.navigation.NavigationView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class DetailActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    int plantType;
    EditText title, morbidity, lifecycle;
    Bitmap selectedImage;
    ImageView plantImage;
    Integer Id;
    PlantDbManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dbManager = new PlantDbManager(this);
        // Menu menu = navigationView.getMenu();
        // menu.findItem(R.id.shareSocialMedia).setVisible(false);
        // menu.findItem(R.id.sendMail).setVisible(false);


        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        Intent intent = getIntent();
        title = findViewById(R.id.titleText);
        morbidity = findViewById(R.id.morbidityText);
        lifecycle = findViewById(R.id.lifecycleText);
        plantImage = findViewById(R.id.plantImage);
        plantType = intent.getIntExtra("plantType", 0);
        Id = intent.getIntExtra("id", 0);
        selectedImage = BitmapFactory.decodeResource(getApplicationContext().getResources(), setDefaultImage());
        plantImage.setImageBitmap(selectedImage);
        get();

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.shareSocialMedia:
                share();
                break;
            case R.id.sendMail:
                sendEmail();
                break;
            case R.id.profile:
                startActivity(new Intent(this, ProfileActivity.class));
                //Intent intent = new Intent(DetailActivity.this, ProfileActivity.class);
                //startActivity(intent);
                break;
            case R.id.passwordChange:
                startActivity(new Intent(this, PasswordChangeActivity.class));
                //Intent pwdIntent = new Intent(DetailActivity.this, PasswordChangeActivity.class);
                //startActivity(pwdIntent);
                break;
            case R.id.logout:
                logout();
                break;
        }
        onBackPressed();

        return true;
    }

    private void share() {
        String message =
                getString(R.string.title)+": "+title.getText().toString()+"\n"+
                getString(R.string.morbidity)+": "+morbidity.getText().toString()+"\n"+
                getString(R.string.lifecycle)+": "+lifecycle.getText().toString()+"\n";
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,message);
        sendIntent.setType("text/plain");

        Intent chooser=Intent.createChooser(sendIntent,getString(R.string.share));
        if (sendIntent.resolveActivity(getPackageManager())!=null){
            startActivity(chooser);
        }
    }

    public void sendEmail() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/html");
        String html =
                HtmlHelper.mailHtml
                        .replace("[TitleTitle]", getString(R.string.title))
                        .replace("[TitleValue]", title.getText().toString())
                        .replace("[MorbidityTitle]", getString(R.string.morbidity))
                        .replace("[MorbidityValue]", morbidity.getText().toString())
                        .replace("[LifecycleTitle]", getString(R.string.lifecycle))
                        .replace("[LifecycleValue]", lifecycle.getText().toString());
        intent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(html));
        startActivity(Intent.createChooser(intent, getString(R.string.send_email)));
    }

    private void logout() {
        AlertDialog.Builder alert = new AlertDialog.Builder(DetailActivity.this);
        alert.setTitle(R.string.logout_now_are_you_sure);
        alert.setPositiveButton(R.string.yes, (dialog, which) -> {
            Settings.accountId = 0;
            Settings.fullName = "";
            Settings.email = "";
            Settings.gender = "";
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finishAffinity();


        });
        alert.setNegativeButton(R.string.no, (dialog, which) -> Toast.makeText(DetailActivity.this, getString(R.string.cancelled), Toast.LENGTH_SHORT).show());
        alert.show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                goToGallery();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            Uri image = data.getData();
            try {
                if (Build.VERSION.SDK_INT >= 28) {
                    ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(), image);
                    selectedImage = ImageDecoder.decodeBitmap(source);
                } else
                    selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), image);

                plantImage.setImageBitmap(selectedImage);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void get() {
        if (Id == 0) return;

        Cursor cursor = dbManager.get(Id);
        if (cursor == null) return;
        while (cursor.moveToNext()) {
            title.setText(cursor.getString(cursor.getColumnIndex("Title")));
            morbidity.setText(cursor.getString(cursor.getColumnIndex(PlantOptions.COLUMN_MORBIDITY)));
            lifecycle.setText(cursor.getString(cursor.getColumnIndex(PlantOptions.COLUMN_LIFECYCLE)));
            byte[] bytes = cursor.getBlob(cursor.getColumnIndex(PlantOptions.COLUMN_IMAGE));
            if (bytes != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                plantImage.setImageBitmap(bitmap);
            }

        }
    }

    public void save(View view) {
        String _title = title.getText().toString();
        String _morbidity = morbidity.getText().toString();
        String _lifecycle = lifecycle.getText().toString();
        Bitmap smallImage = makeSmallerImage(selectedImage, 300);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        smallImage.compress(Bitmap.CompressFormat.PNG, 50, outputStream);
        byte[] byteArray = outputStream.toByteArray();

        Result result;
        if (Id == 0)
            result = dbManager.insert(new PlantModel(Id, _title, plantType, _morbidity, _lifecycle, byteArray));
        else
            result = dbManager.update(new PlantModel(Id, _title, plantType, _morbidity, _lifecycle, byteArray));

        if (!result.Success)
            Toast.makeText(this, result.Message, Toast.LENGTH_LONG).show();

        Intent intent = new Intent(DetailActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("plantType", plantType);
        startActivity(intent);

    }

    public void cancel(View view) {
        finish();
    }

    public void selectImage(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        } else {
            goToGallery();
        }
    }

    private int setDefaultImage() {
        int defaultImage;
        switch (plantType) {
            case 1:
                defaultImage = R.drawable.flower;
                break;
            case 2:
                defaultImage = R.drawable.vegetables;
                break;
            case 3:
                defaultImage = R.drawable.fruit;
                break;
            default:
                defaultImage = R.drawable.tree;
        }
        return defaultImage;


    }

    private void goToGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 2);
    }

    private Bitmap makeSmallerImage(Bitmap image, int maximumSize) {
        int width = image.getWidth();
        int height = image.getHeight();
        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 0) {
            width = maximumSize;
            height = (int) (width / bitmapRatio);

        } else {
            height = maximumSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }
}