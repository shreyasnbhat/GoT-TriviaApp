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
import com.example.shreyas.thrones.ItemFormats.RealmCharacterFormat;
import com.example.shreyas.thrones.ItemFormats.RealmHouseFormat;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.turingtechnologies.materialscrollbar.AlphabetIndicator;

import java.util.Comparator;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.Sort;

import static com.example.shreyas.thrones.R.id.currentLord;

public class HouseDisplay extends AppCompatActivity {


    private RealmList<RealmHouseFormat> houseList = new RealmList<>();
    private Toolbar toolbar;
    private RecyclerView rv;
    private DatabaseReference mDatabaseFirebase = FirebaseDatabase.getInstance().getReference().child("Houses");
    private DatabaseReference mDatabaseLord = FirebaseDatabase.getInstance().getReference().child("Characters");
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

        //Firebase Reference
        mDatabaseFirebase.keepSynced(true);

        //Add Data into List of Houses from Realm Local DB
        houseList.addAll(mDatabaseRealm.where(RealmHouseFormat.class).
                findAllSorted(new String[]{"houseId", "name", "words", "coatOfArms", "region"},
                        new Sort[]{Sort.ASCENDING, Sort.ASCENDING, Sort.ASCENDING, Sort.ASCENDING, Sort.ASCENDING}));


        scrollBar.setIndicator(new AlphabetIndicator(this),true);

        mChildEventListener = generateChildEventListener();

        mDatabaseFirebase.addChildEventListener(mChildEventListener);

        //Sorting Operation
        //Could be moved to an AsyncTask
        houseList.sort(new Comparator<RealmHouseFormat>() {
            @Override
            public int compare(RealmHouseFormat realmHouseFormat, RealmHouseFormat t1) {
                return IntegerConvertor(realmHouseFormat.getHouseId()) - IntegerConvertor(t1.getHouseId());
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

        //Recycler View Stuff
        houseAdapter = new HousesRVAdapter(houseList,this);
        rv.setAdapter(houseAdapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setHasFixedSize(true);

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

                    //Firebase Data
                    final String primaryKey = dataSnapshot.getKey();
                    final String name = dataSnapshot.child("name").getValue(String.class);
                    final String region = dataSnapshot.child("region").getValue(String.class);
                    final String words = dataSnapshot.child("words").getValue(String.class);
                    final String currentLordLink = dataSnapshot.child("currentLord").getValue(String.class);
                    final String coatOfArms = dataSnapshot.child("coatOfArms").getValue(String.class);
                    //Extract Lord ID
                    final String currentLordID = IntegerExtractor(currentLordLink);
                    String currentLordTemp;
                    //Test if Lord exits in Realm
                    RealmCharacterFormat testIfLordInRealm = mDatabaseRealm.where(RealmCharacterFormat.class).equalTo("characterId",currentLordID).findFirst();

                    if(testIfLordInRealm != null){
                        currentLordTemp = mDatabaseRealm.where(RealmCharacterFormat.class).equalTo("characterId",currentLordID).findFirst().getName();
                    }
                    else {
                        currentLordTemp = "Not in Realm";
                    }

                    final String currentLord = currentLordTemp;

                    //Used to Test if any entry with the given House ID exists in the Local DB
                    RealmHouseFormat testInRealm = mDatabaseRealm.where(RealmHouseFormat.class).equalTo("houseId",primaryKey).findFirst();

                    if(testInRealm==null) {

                        mDatabaseRealm.executeTransactionAsync(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {

                                RealmHouseFormat houseToBeAdded = realm.createObject(RealmHouseFormat.class,primaryKey);
                                houseToBeAdded.setName(name);
                                houseToBeAdded.setCurrentLord(currentLord);
                                houseToBeAdded.setCoatOfArms(coatOfArms);
                                houseToBeAdded.setWords(words);
                                houseToBeAdded.setRegion(region);

                            }
                        });

                        houseList.add(new RealmHouseFormat(primaryKey,name,region,coatOfArms,words,null,currentLord,null));
                        houseAdapter.notifyItemInserted(houseList.size() - 1);

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

                        //Firebase Data
                        final String name = dataSnapshot.child("name").getValue(String.class);
                        final String region = dataSnapshot.child("region").getValue(String.class);
                        final String words = dataSnapshot.child("words").getValue(String.class);
                        final String currentLordLink = dataSnapshot.child("currentLord").getValue(String.class);
                        final String coatOfArms = dataSnapshot.child("coatOfArms").getValue(String.class);

                        //Extract Lord ID
                        final String currentLordID = IntegerExtractor(currentLordLink);
                        String currentLordTemp;
                        //Test if Lord exits in Realm
                        RealmCharacterFormat testIfLordInRealm = mDatabaseRealm.where(RealmCharacterFormat.class).equalTo("characterId",currentLordID).findFirst();

                        if(testIfLordInRealm != null){
                            currentLordTemp = mDatabaseRealm.where(RealmCharacterFormat.class).equalTo("characterId",currentLordID).findFirst().getName();
                        }
                        else {
                            currentLordTemp = "Not in Realm";
                        }

                        final String currentLord = currentLordTemp;


                        //Realm Data
                        String realmName = mDatabaseRealm.where(RealmHouseFormat.class).equalTo("houseId",primaryKey).findFirst().getName();
                        String realmRegion = mDatabaseRealm.where(RealmHouseFormat.class).equalTo("houseId",primaryKey).findFirst().getRegion();
                        String realmWords = mDatabaseRealm.where(RealmHouseFormat.class).equalTo("houseId",primaryKey).findFirst().getWords();
                        String realmLord = mDatabaseRealm.where(RealmHouseFormat.class).equalTo("houseId",primaryKey).findFirst().getCurrentLord();
                        String realmCoat = mDatabaseRealm.where(RealmHouseFormat.class).equalTo("houseId",primaryKey).findFirst().getCoatOfArms();

                        //Tweak to avoid un-necessary updation of data
                        boolean ifAnyThingChanged = (realmName.equalsIgnoreCase(name) && realmRegion.equalsIgnoreCase(region) && realmWords.equalsIgnoreCase(words) && realmCoat.equalsIgnoreCase(coatOfArms) && realmLord.equalsIgnoreCase(currentLord));

                        if(!ifAnyThingChanged) {
                            mDatabaseRealm.executeTransactionAsync(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    RealmHouseFormat houseToBeChanged = realm.where(RealmHouseFormat.class).equalTo("houseId", primaryKey).findFirst();
                                    houseToBeChanged.setName(name);
                                    houseToBeChanged.setCurrentLord(currentLord);
                                    houseToBeChanged.setCoatOfArms(coatOfArms);
                                    houseToBeChanged.setWords(words);
                                    houseToBeChanged.setRegion(region);
                                }
                            });
                        }
                        houseAdapter.notifyDataSetChanged();
                        //sortDataSet();

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

        Log.e("Sort","Need to Sort");

    }


    @Override
    protected void onStop() {
        super.onStop();
        houseList.clear();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabaseFirebase.removeEventListener(mChildEventListener);
        mDatabaseRealm.close();

    }

    public int IntegerConvertor(String s)
    {
        return Integer.valueOf(s);
    }
}
