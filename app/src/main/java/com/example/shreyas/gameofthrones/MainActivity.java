package com.example.shreyas.gameofthrones;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private ListView mainList;
    private ArrayList<String> houses = new ArrayList<>();
    private ArrayAdapter<String> listAdpater ;
    private Toolbar toolbar;
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Reference Views
        mainList = (ListView)findViewById(R.id.house_name_list_main);
        toolbar = (Toolbar)findViewById(R.id.toolbar_main);
        button = (Button)findViewById(R.id.button);

        listAdpater = new ArrayAdapter<String>(this,R.layout.list_item_main,R.id.house_names,houses);

        toolbar.setTitle(R.string.app_name);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        mainList.setAdapter(listAdpater);

        String url = "http://www.anapioficeandfire.com/api/houses?page=";
        String temp1;

        for(int k=0;k<=45;k++) {

            temp1 = url;
            temp1 = temp1+""+k;

            JsonArrayRequest jsObjRequest = new JsonArrayRequest
                    (Request.Method.GET, temp1, null, new Response.Listener<JSONArray>() {

                        @Override
                        public void onResponse(JSONArray response) {

                            String temp = "blank";

                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject obj = response.getJSONObject(i);
                                    temp = obj.getString("name");

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                houses.add(temp);

                                listAdpater.notifyDataSetChanged();


                            }


                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Log.v("Error", "Volley Error");
                        }
                    });

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(jsObjRequest);

        }

        Collections.sort(houses);
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


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,Characters.class);
                startActivity(intent);
            }
        });

    }



}
