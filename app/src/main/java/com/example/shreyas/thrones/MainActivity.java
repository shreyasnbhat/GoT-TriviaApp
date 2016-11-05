package com.example.shreyas.thrones;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private ListView mainList;
    private ArrayList<String> houses = new ArrayList<>();
    private ArrayAdapter<String> listAdpater ;
    private Toolbar toolbar;
    private Button button;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Houses");



        mDatabase.keepSynced(true);

        //Reference Views
        mainList = (ListView)findViewById(R.id.house_name_list_main);
        toolbar = (Toolbar)findViewById(R.id.toolbar_main);
        button = (Button)findViewById(R.id.button);

        listAdpater = new ArrayAdapter<String>(this,R.layout.list_item_main,R.id.house_names,houses);

        toolbar.setTitle(R.string.app_name);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        mainList.setAdapter(listAdpater);

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

                        String x = shot.child("name").getValue(String.class);

                        houses.add(x);

                        listAdpater.notifyDataSetChanged();
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

        Collections.sort(houses);
        listAdpater.notifyDataSetChanged();


        mainList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(MainActivity.this,HouseActivity.class);
                TextView v = (TextView)view.findViewById(R.id.house_names);
                String houseName = v.getText().toString();

                final int loc = i;

                intent.putExtra("houseName",houseName);
                intent.putExtra("HousePosition",loc+"");

                startActivity(intent);



            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,Characters.class);
                startActivity(intent);
            }
        });

    }


    public String IntegerExtractor(String m)
    {
        String t =  m.replaceAll("[^0-9]","");
        return t;
    }



}
