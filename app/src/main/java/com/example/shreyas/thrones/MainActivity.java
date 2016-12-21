package com.example.shreyas.thrones;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;

public class MainActivity extends AppCompatActivity {

    private TextView HouseButton;
    private TextView CharacterButton;
    private Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fresco.initialize(this);
        //Reference Views
        HouseButton = (TextView) findViewById(R.id.house_button);
        CharacterButton = (TextView) findViewById(R.id.character_button);
        toolbar = (Toolbar)findViewById(R.id.toolbar);

        toolbar.setTitle("Game Of Thrones Wiki");

        //Toolbar Stuff
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));


        //Setting Listeners
        HouseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this,HouseDisplay.class);
                startActivity(intent);

            }
        });

        CharacterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this,CharacterDisplay.class);
                startActivity(intent);

            }
        });

    }


}
