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


    private ArrayList<RealmHouseFormat> houses = new ArrayList<>();
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
    protected void onStart() {
        super.onStart();

        //Firebase Reference
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);

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

                        RealmHouseFormat temp = new RealmHouseFormat(name);
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


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_display);

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

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
