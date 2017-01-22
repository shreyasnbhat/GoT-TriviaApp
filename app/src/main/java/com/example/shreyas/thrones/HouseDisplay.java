package com.example.shreyas.thrones;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.example.shreyas.thrones.Adapters.HousesRVAdapter;
import com.example.shreyas.thrones.ItemFormats.HouseFormat;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.turingtechnologies.materialscrollbar.AlphabetIndicator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import io.realm.Realm;

public class HouseDisplay extends AppCompatActivity {


    private ArrayList<HouseFormat> houses = new ArrayList<>();
    private Toolbar toolbar;
    private RecyclerView rv;
    private DatabaseReference mDatabase;
    private Realm realm;
    private com.turingtechnologies.materialscrollbar.DragScrollBar scrollBar;


    @Override
    protected void onResume() {

        Log.e("Network Status",isNetworkAvailable() + "");
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_display);

        //Firebase Stuff
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);

        //Reference Views
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        rv = (RecyclerView) findViewById(R.id.houses_rv);
        scrollBar = (com.turingtechnologies.materialscrollbar.DragScrollBar)findViewById(R.id.dragScrollBar);

        //Toolbar Stuff
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitle("Houses");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        final HousesRVAdapter adapter = new HousesRVAdapter(houses, this);

        //Recycler View Stuff
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setHasFixedSize(true);
        scrollBar.setIndicator(new AlphabetIndicator(this),true);

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

                for (DataSnapshot shot : dataSnapshot.child("Houses").getChildren()) {

                    try {

                        String houseId = shot.getKey();
                        String name = shot.child("name").getValue(String.class).trim();
                        String words = shot.child("words").getValue(String.class).trim();
                        String region = shot.child("region").getValue(String.class).trim();
                        String coatOfArms = shot.child("coatOfArms").getValue(String.class).trim();
                        String LordID = IntegerExtractor(shot.child("currentLord").getValue(String.class).trim());

                        HouseFormat temp = new HouseFormat(name);
                        String currentLord="";
                        if(dataSnapshot.child("Characters").child(LordID).child("name").getValue(String.class)!=null)
                        {
                            currentLord = dataSnapshot.child("Characters").child(LordID).child("name").getValue(String.class);
                        }

                        if (!words.equals(""))
                            temp.setWords(words);
                        if (!region.equals(""))
                            temp.setRegion(region);
                        if (!coatOfArms.equals(""))
                            temp.setCoatOfArms(coatOfArms);
                        if (!currentLord.equals(""))
                            temp.setCurrentLord(currentLord);
                        temp.setHouseId(houseId);

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

        class HouseDetailsComparator implements Comparator<HouseFormat> {

            @Override
            public int compare(HouseFormat t1, HouseFormat t2) {

                return t1.getName().compareTo(t2.getName());

            }
        }

        Collections.sort(houses, new HouseDetailsComparator());
        adapter.notifyDataSetChanged();

    }

    public String IntegerExtractor(String m) {
        String t = m.replaceAll("[^0-9]", "");
        return t;
    }

    private boolean isNetworkAvailable() {

        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }


}
