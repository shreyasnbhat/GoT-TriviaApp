package com.example.shreyas.gameofthrones;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView mainList;
    private ArrayList<String> houses = new ArrayList<>();
    private ArrayAdapter<String> listAdpater ;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Reference Views
        mainList = (ListView)findViewById(R.id.house_name_list_main);
        toolbar = (Toolbar)findViewById(R.id.toolbar_main);

        listAdpater = new ArrayAdapter<String>(this,R.layout.list_item_main,R.id.house_names,houses);

        toolbar.setTitle(R.string.app_name);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        mainList.setAdapter(listAdpater);

        //Need to Convert to Recycler View/Grid View
        houses.add("House Stark");
        houses.add("House Bolton");
        houses.add("House Lannister");
        houses.add("House Baratheon");
        houses.add("House Tully");
        houses.add("House Targaryen");
        houses.add("House Martell");
        houses.add("House Florent");
        houses.add("House Redwyne");

        listAdpater.notifyDataSetChanged();

        mainList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(MainActivity.this,HouseActivity.class);
                TextView v = (TextView)view.findViewById(R.id.house_names);
                String toSend = v.getText().toString();
                Log.v("House",v.getText().toString());
                intent.putExtra("House",toSend);
                startActivity(intent);

            }
        });

    }



}
