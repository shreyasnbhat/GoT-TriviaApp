package com.example.shreyas.thrones;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CharacterDisplay extends AppCompatActivity {

    private Toolbar toolbar;

    private ArrayList<String> list = new ArrayList<>();

    private ArrayAdapter<String> adapter;

    private ListView listview;

    private DatabaseReference mDatabase;


    @Override
    protected void onDestroy() {
        super.onDestroy();
        list.clear();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_display);


        //Firebase Stuff
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);

        //View Referencing
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        listview = (ListView)findViewById(R.id.list);

        //Toolbar Stuff
        toolbar.setTitle("CharacterDisplay");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        adapter = new ArrayAdapter<>(CharacterDisplay.this,R.layout.list_house_member_format,R.id.house_names,list);
        listview.setAdapter(adapter);

        String url = "http://www.anapioficeandfire.com/api/characters?page=";
        String temp1;

        //Used to populate data into Firebase
        //When API is updated will need to push app update. As Firebase is copy of API JSON


        /*for(int k=50;k<=200;k++) {

            temp1 = url;
            temp1 = temp1+""+k;

            JsonArrayRequest jsObjRequest = new JsonArrayRequest
                    (Request.Method.GET, temp1, null, new Response.Listener<JSONArray>() {

                        @Override
                        public void onResponse(JSONArray response) {

                            String name;
                            String id;
                            String gender;
                            String culture;
                            String born;
                            String died;
                            String playedBy;


                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject obj = response.getJSONObject(i);
                                     name = obj.getString("name");
                                     id = obj.getString("url").replaceAll("[^0-9]","");
                                     gender = obj.getString("gender");
                                     culture = obj.getString("culture");
                                     born = obj.getString("born");
                                     died = obj.getString("died");
                                     playedBy = obj.getString("playedBy");
                                     int size = playedBy.length();

                                    if(playedBy.length()>2 && !name.equals(""))
                                    {

                                        playedBy = playedBy.substring(2,size-2);
                                        CharacterFormat x = new CharacterFormat(name,gender,culture,born,died,playedBy);
                                        mDatabase.child("CharacterDisplay").child(id).setValue(x);
                                    }
                                    else if(size==2 && !name.equalsIgnoreCase("")) {

                                        playedBy = "";
                                        CharacterFormat x = new CharacterFormat(name,gender,culture,born,died,playedBy);
                                        mDatabase.child("CharacterDisplay").child(id).setValue(x);


                                    }




                                } catch (Exception e) {
                                    e.printStackTrace();
                                }




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
        */


       //Populate Data from Firebase

       mDatabase.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {

               for (DataSnapshot shot : dataSnapshot.child("Characters").getChildren()) {

                   try {
                       String x = shot.child("name").getValue(String.class);

                       if(x!=null)
                        {list.add(x);}

                       adapter.notifyDataSetChanged();
                   }
                   catch(Exception e)
                   {
                       e.printStackTrace();
                   }

               }
           }

           @Override
           public void onCancelled(DatabaseError databaseError) {

               Log.v("Error","Firebase Error");

           }
       });










    }


}
