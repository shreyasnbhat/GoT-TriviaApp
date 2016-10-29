package com.example.shreyas.gameofthrones;

import android.icu.text.UnicodeSetSpanner;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

public class HouseActivity extends AppCompatActivity {

    private Toolbar toolbar_house;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house);

        //Reference Views
        toolbar_house = (Toolbar)findViewById(R.id.toolbar);

        String toolbartext = getIntent().getStringExtra("House");
        toolbar_house.setTitle(toolbartext);
        setSupportActionBar(toolbar_house);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        toolbar_house.setTitleTextColor(getResources().getColor(R.color.white));

    }
}
