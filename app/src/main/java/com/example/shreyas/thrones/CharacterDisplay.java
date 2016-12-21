package com.example.shreyas.thrones;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.turingtechnologies.materialscrollbar.AlphabetIndicator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class CharacterDisplay extends AppCompatActivity {

    private Toolbar toolbar;
    private ArrayList<CharacterFormat> characters = new ArrayList<>();
    private DatabaseReference mDatabase;
    private RecyclerView rv;
    private ProgressBar progress;
    //private com.turingtechnologies.materialscrollbar.DragScrollBar scrollBar;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        characters.clear();
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
        rv = (RecyclerView) findViewById(R.id.characters_rv);
        progress = (ProgressBar)findViewById(R.id.progressbar);
        //scrollBar = (com.turingtechnologies.materialscrollbar.DragScrollBar)findViewById(R.id.dragScrollBar);

        //Toolbar Stuff
        toolbar.setTitle("Characters");
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        final CharactersRVAdapter adapter = new CharactersRVAdapter(characters, this);

        //Recycler View Stuff
        rv.setAdapter(adapter);
        rv.setLayoutManager(new StaggeredGridLayoutManager(2,1));
        rv.setHasFixedSize(true);
        //scrollBar.setIndicator(new AlphabetIndicator(this),true);

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

               progress.setIndeterminate(true);

               for (DataSnapshot shot : dataSnapshot.child("Characters").getChildren()) {

                   try {

                       String name = shot.child("name").getValue(String.class);
                       String playedBy = shot.child("playedBy").getValue(String.class);
                       String gender = shot.child("gender").getValue(String.class);
                       String born = shot.child("born").getValue(String.class);
                       String died = shot.child("died").getValue(String.class);
                       String imageUrl = shot.child("imageUrl").getValue(String.class);


                       if(name!=null && imageUrl!=null)

                        {
                            characters.add(new CharacterFormat(name,playedBy,gender,born,died,imageUrl));
                        }


                       adapter.notifyDataSetChanged();
                       progress.setVisibility(View.INVISIBLE);
                   }
                   catch(Exception e)
                   {
                       e.printStackTrace();
                   }

               }

               characters = removeDuplicates(characters);
               adapter.notifyDataSetChanged();
           }

           @Override
           public void onCancelled(DatabaseError databaseError) {

               progress.setVisibility(View.GONE);
               Log.v("Error","Firebase Error");

           }
       });

        class CharacterDetailsComparator implements Comparator<CharacterFormat> {

            @Override
            public int compare(CharacterFormat t1,CharacterFormat t2) {

                String one = t1.getName();
                String two = t2.getName();
                Log.e(one,two);

                return one.compareToIgnoreCase(two);

            }
        }

        Collections.sort(characters,new CharacterDetailsComparator());
        adapter.notifyDataSetChanged();


    }

    public ArrayList<CharacterFormat> removeDuplicates(ArrayList<CharacterFormat> temp)
    {
        Set<CharacterFormat> t = new HashSet<>();
        t.addAll(temp);
        temp.clear();
        temp.addAll(t);
        return temp;
    }


}
