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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.turingtechnologies.materialscrollbar.AlphabetIndicator;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.Sort;

public class HouseDisplay extends AppCompatActivity {


    private RealmList<RealmHouseFormat> houseList = new RealmList<>();
    private Toolbar toolbar;
    private RecyclerView rv;
    private DatabaseReference mDatabaseFirebase = FirebaseDatabase.getInstance().getReference().child("Houses");
    private Realm mDatabaseRealm;
    private HousesRVAdapter houseAdapter;
    private ChildEventListener mChildEventListener;
    private com.turingtechnologies.materialscrollbar.DragScrollBar scrollBar;


    @Override
    protected void onResume() {

        Log.e("Network Status",isNetworkAvailable() + "");
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Initialize Realm here
        mDatabaseRealm = Realm.getDefaultInstance();

        Log.e("PAth",mDatabaseRealm.getPath());

        //Firebase Reference
        mDatabaseFirebase.keepSynced(true);

        //Add Data into List of Houses from Realm Local DB
        houseList.addAll(mDatabaseRealm.where(RealmHouseFormat.class).
                findAllSorted(new String[]{"houseId", "name", "words", "coatOfArms", "region"},
                        new Sort[]{Sort.ASCENDING, Sort.ASCENDING, Sort.ASCENDING, Sort.ASCENDING, Sort.ASCENDING}));

        houseAdapter = new HousesRVAdapter(houseList,this);
        rv.setAdapter(houseAdapter);
        scrollBar.setIndicator(new AlphabetIndicator(this),true);

        mChildEventListener = generateChildEventListener();

        mDatabaseFirebase.addChildEventListener(mChildEventListener);

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

        //Recycler View Stuff
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setHasFixedSize(true);

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

    public ChildEventListener generateChildEventListener(){

        ChildEventListener listener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                try{

                    final String primaryKey = dataSnapshot.getKey();
                    final String name = dataSnapshot.child("name").getValue(String.class);
                    final String region = dataSnapshot.child("region").getValue(String.class);
                    final String words = dataSnapshot.child("words").getValue(String.class);
                    final String currentLord = IntegerExtractor(dataSnapshot.child("currentLord").getValue(String.class));
                    final String coatOfArms = dataSnapshot.child("coatOfArms").getValue(String.class);


                    //Used to Test if any entry with the given House ID exists in the Local DB
                    RealmHouseFormat testInRealm = mDatabaseRealm.where(RealmHouseFormat.class).equalTo("houseId",primaryKey).findFirst();

                    if(testInRealm==null) {

                        mDatabaseRealm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {

                                RealmHouseFormat houseToBeAdded = realm.createObject(RealmHouseFormat.class,primaryKey);
                                houseToBeAdded.setName(name);
                                houseToBeAdded.setCurrentLord(currentLord);
                                houseToBeAdded.setCoatOfArms(coatOfArms);
                                //houseToBeAdded.setMembers(house.getMembers());
                                houseToBeAdded.setWords(words);
                                houseToBeAdded.setRegion(region);
                                //houseToBeAdded.setTitles(house.getTitles());

                            }
                        });

                        houseList.add(new RealmHouseFormat(primaryKey,name,region,coatOfArms,words,null,currentLord,null));
                        houseList.sort("houseId",Sort.ASCENDING);
                        houseAdapter.notifyDataSetChanged();
                        //houseAdapter.notifyItemInserted(houseList.size() - 1);

                    }
                    else
                    {
                        onChildChanged(dataSnapshot,s);
                    }

                }
                catch (Exception e)
                {
                    Log.e(getLocalClassName(),"OnChildAdded Parse error");
                    e.printStackTrace();
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                final String primaryKey = dataSnapshot.getKey();

                //Test if already exists in Realm
                RealmHouseFormat testInRealm = mDatabaseRealm.where(RealmHouseFormat.class).equalTo("houseId",primaryKey).findFirst();

                if( testInRealm == null ){

                    //Code is common so call Child Added instead
                    onChildAdded(dataSnapshot,s);

                }
                else{

                    //Parse error could occur
                    try{

                        final String name = dataSnapshot.child("name").getValue(String.class);
                        final String region = dataSnapshot.child("region").getValue(String.class);
                        final String words = dataSnapshot.child("words").getValue(String.class);
                        final String currentLord = dataSnapshot.child("currentLord").getValue(String.class);
                        final String coatOfArms = dataSnapshot.child("coatOfArms").getValue(String.class);


                        mDatabaseRealm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                RealmHouseFormat houseToBeChanged = realm.createObject(RealmHouseFormat.class,primaryKey);
                                houseToBeChanged.setName(name);
                                houseToBeChanged.setCurrentLord(currentLord);
                                houseToBeChanged.setCoatOfArms(coatOfArms);
                                //houseToBeChanged.setMembers(house.getMembers());
                                houseToBeChanged.setWords(words);
                                houseToBeChanged.setRegion(region);
                                //houseToBeChanged.setTitles(house.getTitles());

                            }
                        });
                        sortDataSet();
                        houseAdapter.notifyDataSetChanged();

                    }
                    catch (Exception e){

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

        return listener;

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void sortDataSet() {

        houseList.sort(new String[]{"houseId", "name", "words", "coatOfArms", "region"},
                new Sort[]{Sort.ASCENDING, Sort.ASCENDING, Sort.ASCENDING, Sort.ASCENDING, Sort.ASCENDING});

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

    }
}
