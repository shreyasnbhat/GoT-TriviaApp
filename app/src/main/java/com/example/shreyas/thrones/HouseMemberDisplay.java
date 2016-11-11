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

public class HouseMemberDisplay extends AppCompatActivity {

    private Toolbar toolbar_house;

    private DatabaseReference mDatabase;

    private ListView swornList;

    private ArrayList<String> sworn = new ArrayList<>();

    private ArrayList<String> swornTemp = new ArrayList<>(9);

    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_member_display);

        //Firebase Stuff
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);

        //Reference Views
        toolbar_house = (Toolbar) findViewById(R.id.toolbar);
        swornList = (ListView) findViewById(R.id.list_house);

        //Get Intent Data
        String toolbartext = getIntent().getStringExtra("houseName");
        String temp = getIntent().getStringExtra("HousePosition");
        final int loc = Integer.parseInt(temp);

        //Toolbar Stuff
        toolbar_house.setTitle(toolbartext);
        setSupportActionBar(toolbar_house);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //ArrayAdapter Stuff
        adapter = new ArrayAdapter<>(this, R.layout.list_house_member_format, R.id.house_names, sworn);
        swornList.setAdapter(adapter);

        mDatabase.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot shot : dataSnapshot.child("Houses").child(loc + 1 + "").child("swornMembers").getChildren()) {

                    String temp = shot.getValue(String.class);

                    try {

                        swornTemp.add(temp);
                        if (dataSnapshot.child("Characters").child(temp).child("name").getValue(String.class) != null) {

                            sworn.add(dataSnapshot.child("Characters").child(temp).child("name").getValue(String.class));
                            adapter.notifyDataSetChanged();
                        }

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
}