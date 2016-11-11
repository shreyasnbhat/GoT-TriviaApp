package com.example.shreyas.thrones;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HouseDisplay extends AppCompatActivity {


    private ArrayList<HouseDetails> houses = new ArrayList<>();

    private Toolbar toolbar;

    private RecyclerView rv;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_display);

        //Firebase Stuff
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Houses");
        mDatabase.keepSynced(true);

        //Reference Views
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        rv = (RecyclerView)findViewById(R.id.houses_rv);

        //Toolbar Stuff
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitle(R.string.app_name);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        final HousesRV adapter = new HousesRV(houses,this);

        //Recycler View Stuff
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setHasFixedSize(true);

        //URLS for JSON Parsing
        String url = "http://www.anapioficeandfire.com/api/houses?page=";
        String temp1;



        /*for(int k=1;k<=45;k++) {

            temp1 = url;
            temp1 = temp1+""+k;

            JsonArrayRequest jsObjRequest = new JsonArrayRequest
                    (Request.Method.GET, temp1, null, new Response.Listener<JSONArray>() {

                        @Override
                        public void onResponse(JSONArray response) {

                             String id;
                             String name;
                             String region;
                             String coatOfArms;
                             String words;
                             ArrayList<String> titles = new ArrayList<>();
                             String currentLord;
                             ArrayList<String> members = new ArrayList<>();

                            for (int i = 0; i < response.length(); i++) {
                                try {

                                    //Derefernce JSON
                                    JSONObject obj = response.getJSONObject(i);

                                    id = IntegerExtractor(obj.getString("url"));
                                    name = obj.getString("name");
                                    region = obj.getString("region");
                                    coatOfArms = obj.getString("coatOfArms");
                                    words = obj.getString("words");
                                    currentLord = obj.getString("currentLord");
                                    JSONArray titleArray = obj.getJSONArray("titles");
                                    for(int f = 0; f < titleArray.length();f++)
                                    {
                                        try {

                                            titles.add(titleArray.get(f).toString());
                                        }
                                        catch (Exception e)
                                        {
                                            e.printStackTrace();
                                        }
                                    }

                                    JSONArray memberArray = obj.getJSONArray("swornMembers");
                                    for(int f = 0; f < memberArray.length();f++)
                                    {


                                        members.add(IntegerExtractor(memberArray.get(f).toString()));
                                    }





                                    mDatabase.child(id).child("name").setValue(name);
                                    mDatabase.child(id).child("region").setValue(region);
                                    mDatabase.child(id).child("coatOfArms").setValue(coatOfArms);
                                    mDatabase.child(id).child("words").setValue(words);
                                    mDatabase.child(id).child("currentLord").setValue(currentLord);
                                    mDatabase.child(id).child("titles").setValue(titles);
                                    mDatabase.child(id).child("swornMembers").setValue(members);

                                    members.clear();

                                    titles.clear();










                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                //houses.add(temp);

                                //listAdpater.notifyDataSetChanged();


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

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot shot : dataSnapshot.getChildren()) {

                    try {

                        String x = shot.child("name").getValue(String.class).trim();
                        HouseDetails temp = new HouseDetails(x);
                        houses.add(temp);

                    } catch (Exception e) {

                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Log.v("Error", "Firebase Error");

            }
        });

        adapter.notifyDataSetChanged();

    }

    public String IntegerExtractor(String m) {
        String t = m.replaceAll("[^0-9]", "");
        return t;
    }


}
