package com.example.farmerhelper;

import android.content.Intent;
import android.os.Bundle;

import com.example.farmerhelper.models.Settings;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import com.example.farmerhelper.ui.main.SectionsPagerAdapter;

public class MainActivity extends AppCompatActivity {
    int plantType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        plantType=getIntent().getIntExtra("plantType",0);
        tabs.selectTab(tabs.getTabAt(plantType));
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,DetailActivity.class);
                int position= tabs.getSelectedTabPosition();
                intent.putExtra("plantType",position);
                intent.putExtra("id",0);
                startActivity(intent);

            }
        });
    }
}