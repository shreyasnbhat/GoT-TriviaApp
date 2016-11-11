package com.example.shreyas.thrones;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button HouseButton;
    private Button CharacterButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Reference Views
        HouseButton = (Button)findViewById(R.id.house_button);
        CharacterButton = (Button)findViewById(R.id.character_button);

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
