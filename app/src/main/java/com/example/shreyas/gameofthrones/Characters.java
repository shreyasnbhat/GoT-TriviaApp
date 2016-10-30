package com.example.shreyas.gameofthrones;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

public class Characters extends AppCompatActivity {

    private Toolbar toolbar;

    private ArrayList<String> list = new ArrayList<>();

    private ArrayAdapter<String> adapter;

    private ListView listview;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        list.clear();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volley_test);

        toolbar = (Toolbar)findViewById(R.id.toolbar);

        toolbar.setTitle("Characters");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



        listview = (ListView)findViewById(R.id.list);


        adapter = new ArrayAdapter<>(Characters.this,R.layout.list_item_main,R.id.house_names,list);

        listview.setAdapter(adapter);

        String url = "http://www.anapioficeandfire.com/api/characters?page=";
        String temp1;


        for(int k=0;k<=100;k++) {

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

                                list.add(temp);
                                adapter.notifyDataSetChanged();


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

        Collections.sort(list);
        adapter.notifyDataSetChanged();








    }


}
