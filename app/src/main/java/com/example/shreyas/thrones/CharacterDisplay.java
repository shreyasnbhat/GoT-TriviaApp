package com.example.shreyas.thrones;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.shreyas.thrones.Adapters.CharactersRVAdapter;
import com.example.shreyas.thrones.ItemFormats.CharacterFormat;
import com.example.shreyas.thrones.ItemFormats.RealmCharacterFormat;
import com.example.shreyas.thrones.ItemFormats.RealmHouseFormat;
import com.google.android.gms.phenotype.Configuration;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.Sort;

public class CharacterDisplay extends AppCompatActivity {

    private Toolbar toolbar;
    private RealmList<RealmCharacterFormat> characterList = new RealmList<>();
    private DatabaseReference mDatabaseFirebase = FirebaseDatabase.getInstance().getReference().child("Characters");
    private RecyclerView rv;
    private ChildEventListener mChildEventListener;
    private CharactersRVAdapter characterAdapter;
    private ProgressBar progress;
    private Realm mDatabaseRealm;

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabaseFirebase.removeEventListener(mChildEventListener);
        mDatabaseRealm.close();
        characterList.clear();
    }


    @Override
    protected void onStart() {
        super.onStart();

        //Initialize Realm here
        mDatabaseRealm = Realm.getDefaultInstance();

        //Firebase Stuff
        mDatabaseFirebase.keepSynced(true);

        //Add Data into List of Characters from Realm Local DB
        characterList.addAll(mDatabaseRealm.where(RealmCharacterFormat.class).
                findAllSorted(new String[]{"characterId", "name", "playedBy", "gender", "born", "died", "imageUrl"},
                        new Sort[]{Sort.ASCENDING, Sort.ASCENDING, Sort.ASCENDING, Sort.ASCENDING, Sort.ASCENDING, Sort.ASCENDING, Sort.ASCENDING}));




        //Populate Data from Firebase
        mChildEventListener = generateChildEventListener();
        mDatabaseFirebase.addChildEventListener(mChildEventListener);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_display);


        //View Referencing
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        rv = (RecyclerView) findViewById(R.id.characters_rv);
        progress = (ProgressBar) findViewById(R.id.progressbar);

        //Toolbar Stuff
        toolbar.setTitle("Characters");
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Recycler View Stuff
        characterAdapter = new CharactersRVAdapter(characterList, this);
        rv.setAdapter(characterAdapter);

        Log.e("Display Metrics",rv.getResources().getDisplayMetrics().toString());

        int span = calculateSpan();

        rv.setLayoutManager(new StaggeredGridLayoutManager(span, 1));
        rv.setHasFixedSize(true);


    }

    public ChildEventListener generateChildEventListener() {

        ChildEventListener listener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                if (dataSnapshot.child("imageUrl").getValue(String.class) != null) {


                    try {

                        final String primaryKey = dataSnapshot.getKey();
                        final String name = dataSnapshot.child("name").getValue(String.class);
                        final String playedBy = dataSnapshot.child("playedBy").getValue(String.class);
                        final String gender = dataSnapshot.child("gender").getValue(String.class);
                        final String born = dataSnapshot.child("born").getValue(String.class);
                        final String died = dataSnapshot.child("died").getValue(String.class);
                        final String imageUrl = dataSnapshot.child("imageUrl").getValue(String.class);


                        //Used to Test if any entry with the given Character ID exists in the Local DB
                        RealmCharacterFormat testInRealm = mDatabaseRealm.where(RealmCharacterFormat.class).equalTo("characterId", primaryKey).findFirst();

                        if (testInRealm == null) {

                            mDatabaseRealm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {

                                    RealmCharacterFormat characterToBeAdded = realm.createObject(RealmCharacterFormat.class, primaryKey);
                                    characterToBeAdded.setName(name);
                                    characterToBeAdded.setBorn(born);
                                    characterToBeAdded.setImageUrl(imageUrl);
                                    characterToBeAdded.setDied(died);
                                    characterToBeAdded.setGender(gender);
                                    characterToBeAdded.setPlayedBy(playedBy);

                                }
                            });

                            Log.e("ImageUrl", imageUrl);

                            characterList.add(new RealmCharacterFormat(primaryKey, name, playedBy, gender, born, died, imageUrl));
                            characterAdapter.notifyDataSetChanged();
                        } else {
                            onChildChanged(dataSnapshot, s);
                        }

                    } catch (Exception e) {
                        Log.e(getLocalClassName(), "OnChildAdded Parse error");
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                final String primaryKey = dataSnapshot.getKey();

                //Test if already exists in Realm
                RealmCharacterFormat testInRealm = mDatabaseRealm.where(RealmCharacterFormat.class).equalTo("characterId", primaryKey).findFirst();

                if (testInRealm == null) {

                    //Code is common so call Child Added instead
                    onChildAdded(dataSnapshot, s);

                } else {

                    //Parse error could occur
                    try {

                        //Firebase Data
                        final String name = dataSnapshot.child("name").getValue(String.class);
                        final String playedBy = dataSnapshot.child("playedBy").getValue(String.class);
                        final String gender = dataSnapshot.child("gender").getValue(String.class);
                        final String born = dataSnapshot.child("born").getValue(String.class);
                        final String died = dataSnapshot.child("died").getValue(String.class);
                        final String imageUrl = dataSnapshot.child("imageUrl").getValue(String.class);

                        //Realm Data
                        String realmName = mDatabaseRealm.where(RealmCharacterFormat.class).equalTo("characterId",primaryKey).findFirst().getName();
                        String realmPlayedBy = mDatabaseRealm.where(RealmCharacterFormat.class).equalTo("characterId",primaryKey).findFirst().getPlayedBy();
                        String realmGender = mDatabaseRealm.where(RealmCharacterFormat.class).equalTo("characterId",primaryKey).findFirst().getGender();
                        String realmBorn = mDatabaseRealm.where(RealmCharacterFormat.class).equalTo("characterId",primaryKey).findFirst().getBorn();
                        String realmDied = mDatabaseRealm.where(RealmCharacterFormat.class).equalTo("characterId",primaryKey).findFirst().getDied();
                        String realmImageUrl = mDatabaseRealm.where(RealmCharacterFormat.class).equalTo("characterId",primaryKey).findFirst().getImageUrl();


                        //Tweak to avoid un-necessary updation of data
                        boolean ifAnyThingChanged = (realmName.equalsIgnoreCase(name) && realmPlayedBy.equalsIgnoreCase(playedBy) && realmGender.equalsIgnoreCase(gender) && realmBorn.equalsIgnoreCase(born) && realmDied.equalsIgnoreCase(died) && realmImageUrl.equalsIgnoreCase(imageUrl));


                        if (!ifAnyThingChanged) {
                            mDatabaseRealm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    RealmCharacterFormat characterToBeChanged = realm.where(RealmCharacterFormat.class).equalTo("characterId", primaryKey).findFirst();
                                    characterToBeChanged.setName(name);
                                    characterToBeChanged.setPlayedBy(playedBy);
                                    characterToBeChanged.setGender(gender);
                                    characterToBeChanged.setBorn(born);
                                    characterToBeChanged.setDied(died);
                                    characterToBeChanged.setImageUrl(imageUrl);
                                }
                            });
                        }
                        characterAdapter.notifyDataSetChanged();
                        //sortDataSet();

                    } catch (Exception e) {

                        e.printStackTrace();

                    }

                }

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };


        progress.setVisibility(View.INVISIBLE);
        return listener;

    }

    public ArrayList<CharacterFormat> removeDuplicates(ArrayList<CharacterFormat> temp) {
        Set<CharacterFormat> t = new HashSet<>();
        t.addAll(temp);
        temp.clear();
        temp.addAll(t);
        return temp;
    }

    public int calculateSpan()
    {
        //Check if Portrait Or Landscape
        //Get span value from xdpi or ydpi

        int span = 2;

        DisplayMetrics screenMetrics = getResources().getDisplayMetrics();
        float widthPixels = screenMetrics.widthPixels;
        float heightPixels = screenMetrics.heightPixels;
        float density = screenMetrics.density;
        //Gets net dp width
        float dpWidth = widthPixels/density;

        float temp = dpWidth/150;
        span = (int)temp;

        return span;

    }


}
