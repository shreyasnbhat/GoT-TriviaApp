package com.example.shreyas.thrones;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.example.shreyas.thrones.Adapters.SearchRVAdapter;
import com.example.shreyas.thrones.ItemFormats.CharacterFormat;
import com.example.shreyas.thrones.ItemFormats.DividerFormat;
import com.example.shreyas.thrones.ItemFormats.RealmCharacterFormat;
import com.example.shreyas.thrones.ItemFormats.RealmHouseFormat;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ListView mDrawerList;
    private DatabaseReference mDatabase;
    private DatabaseReference charactersRef, houseRef;
    private FloatingSearchView searchView;
    private TextView noResultTextView;
    private ArrayList<Object> results = new ArrayList<>();
    private RecyclerView searchRecyclerView;
    private Realm mDatabaseRealm;
    private SearchRVAdapter searchAdapter = new SearchRVAdapter(results, this);

    @Override
    protected void onStart() {
        super.onStart();

        //Initialize realm for this actviity
        mDatabaseRealm = Realm.getDefaultInstance();

        //Firebase Stuff
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fresco.initialize(this);

        //charactersRef = mDatabase.child("Characters");
        //houseRef = mDatabase.child("Houses");
        //Reference Views
        searchView = (FloatingSearchView) findViewById(R.id.floating_search_view);
        searchRecyclerView = (RecyclerView) findViewById(R.id.search_recycler_view);
        noResultTextView = (TextView) findViewById(R.id.no_result);

        //Recycler View Stuff
        searchRecyclerView.setAdapter(searchAdapter);
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchRecyclerView.setHasFixedSize(true);

        //Navigation Drawer Stuff
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        searchView.attachNavigationDrawerToMenuButton(drawer);

        searchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {
                results.clear();
                searchQuery(newQuery);
                searchAdapter.notifyDataSetChanged();

            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_houses) {
            Intent intent = new Intent(MainActivity.this, HouseDisplay.class);
            startActivity(intent);
        } else if (id == R.id.nav_characters) {
            Intent intent = new Intent(MainActivity.this, CharacterDisplay.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabaseRealm.close();
    }

    public void searchQuery(String query) {

        String querySplit[] = query.trim().split(" ");
        final String tempCheck = querySplit[0].toLowerCase();


        RealmResults<RealmHouseFormat> houseResult = mDatabaseRealm.where(RealmHouseFormat.class).contains("name",tempCheck).findAll();

        //Put Results in Recycler View

        if(houseResult.size() > 0 )
        {
            results.add(new DividerFormat("Houses (" + houseResult.size() + ")"));

            noResultTextView.setVisibility(View.INVISIBLE);

            results.addAll(houseResult);

            searchAdapter.notifyDataSetChanged();
        }



        RealmResults<RealmCharacterFormat> characterResult = mDatabaseRealm.where(RealmCharacterFormat.class).contains("name",tempCheck).findAll();


        if(characterResult.size() > 0) {

            results.add(new DividerFormat("Characters (" + characterResult.size() + ")"));

            results.addAll(characterResult);

            searchAdapter.notifyDataSetChanged();

        }



        /*houseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot childShot : dataSnapshot.getChildren()) {

                    String name = childShot.child("name").getValue(String.class);
                    String nameLower = name.toLowerCase();

                    if (nameLower.contains(tempCheck)) {

                        try {

                            String houseId = childShot.getKey();
                            String region = childShot.child("region").getValue(String.class);
                            String coatOfArms = childShot.child("coatOfArms").getValue(String.class);
                            String words = childShot.child("words").getValue(String.class);
                            //Need to Implement
                            RealmList<RealmString> titles = new RealmList<>();
                            String currentLord = "Jon Snow";
                            RealmList<RealmString> members = new RealmList<>();
                            //

                            results.add(new RealmHouseFormat(houseId, name, region, coatOfArms, words, titles, currentLord, members));

                            searchAdapter.notifyDataSetChanged();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (results.size() != 0) {
                            noResultTextView.setVisibility(View.INVISIBLE);
                        } else {
                            searchRecyclerView.setVisibility(View.INVISIBLE);
                            noResultTextView.setVisibility(View.VISIBLE);
                        }

                    }


                }

                results.add(new DividerFormat("Characters"));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        charactersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot childShot : dataSnapshot.getChildren()) {

                    String name = childShot.child("name").getValue(String.class);
                    String nameLower = name.toLowerCase();

                    if (nameLower.contains(tempCheck)) {

                        try {

                            String playedBy = childShot.child("playedBy").getValue(String.class);
                            String gender = childShot.child("gender").getValue(String.class);
                            String born = childShot.child("born").getValue(String.class);
                            String died = childShot.child("died").getValue(String.class);
                            String imageUrl = childShot.child("imageUrl").getValue(String.class);
                            results.add(new CharacterFormat(name, playedBy, gender, born, died, imageUrl));
                            searchAdapter.notifyDataSetChanged();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        if (results.size() != 0) {
                            noResultTextView.setVisibility(View.INVISIBLE);
                        } else {
                            searchRecyclerView.setVisibility(View.INVISIBLE);
                            noResultTextView.setVisibility(View.VISIBLE);
                        }

                    }


                }




            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    */

    }
}
