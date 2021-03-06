package com.example.shreyas.thrones;

import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.test.suitebuilder.TestMethod;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.shreyas.thrones.Adapters.CharactersRVAdapter;
import com.example.shreyas.thrones.ItemFormats.CharacterFormat;
import com.example.shreyas.thrones.ItemFormats.RealmCharacterFormat;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HouseInfo extends AppCompatActivity {

    private Toolbar houseToolbar;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();;
    private RecyclerView swornMemberRecyclerView;
    private ArrayList<RealmCharacterFormat> swornMembersList = new ArrayList<>();
    private TextView errorText;
    private ProgressBar progress;
    private SimpleDraweeView image;
    private TextView houseData;
    private TextView swornMembersTextView;
    private TextView wikiTextView;
    private TextView regionTextView,wordsTextView,lordTextView;

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_info);

        //Firebase Stuff
        mDatabase.keepSynced(true);

        //Reference Views
        houseToolbar = (Toolbar) findViewById(R.id.toolbar);
        swornMemberRecyclerView = (RecyclerView) findViewById(R.id.sworn_member_recycler_view);
        errorText = (TextView) findViewById(R.id.error_text);
        progress = (ProgressBar)findViewById(R.id.progressbar);
        houseData = (TextView)findViewById(R.id.house_wiki);
        //image = (SimpleDraweeView)findViewById(R.id.house_image);
        regionTextView = (TextView)findViewById(R.id.region_details);
        wordsTextView = (TextView)findViewById(R.id.words_details);
        lordTextView = (TextView)findViewById(R.id.lord_details);
        wikiTextView = (TextView)findViewById(R.id.wiki_details);
        swornMembersTextView = (TextView)findViewById(R.id.sworn_member_text);

        progress.getIndeterminateDrawable().setColorFilter(Color.parseColor("#FFFFFF"), android.graphics.PorterDuff.Mode.SRC_ATOP);

        //Get Intent Data
        String houseName = getIntent().getStringExtra("houseName");
        String temp = getIntent().getStringExtra("HousePosition");
        String words = getIntent().getStringExtra("words");
        String currentLord = getIntent().getStringExtra("currentLord");
        String region = getIntent().getStringExtra("region");
        String coatOfArms = getIntent().getStringExtra("coatOfArms");
        final int loc = Integer.parseInt(temp) - 1;


        //Toolbar Stuff
        houseToolbar.setTitle(houseName);
        setSupportActionBar(houseToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        houseToolbar.setTitleTextColor(getResources().getColor(R.color.white));

        //Setup Typeface
        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/helsinki.ttf");
        houseData.setTypeface(face);
        swornMembersTextView.setTypeface(face);
        wikiTextView.setTypeface(face);

        //Setup Wiki TextViews
        setupWikiTextViews(words,currentLord,region);

        //ArrayAdapter Stuff
        final CharactersRVAdapter adapter = new CharactersRVAdapter(swornMembersList, this);
        swornMemberRecyclerView.setAdapter(adapter);
        swornMemberRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        swornMemberRecyclerView.setHasFixedSize(true);

        mDatabase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for (DataSnapshot shot : dataSnapshot.child("Houses").child(loc + 1 + "").child("swornMembers").getChildren()) {

                    try {

                        String temp = shot.getValue(String.class);

                        try {

                            String key = dataSnapshot.child("Characters").child(temp).getKey();
                            String name = dataSnapshot.child("Characters").child(temp).child("name").getValue(String.class);
                            String playedBy = dataSnapshot.child("Characters").child(temp).child("playedBy").getValue(String.class);
                            String gender = dataSnapshot.child("Characters").child(temp).child("gender").getValue(String.class);
                            String born = dataSnapshot.child("Characters").child(temp).child("born").getValue(String.class);
                            String died = dataSnapshot.child("Characters").child(temp).child("died").getValue(String.class);
                            String imageUrl = dataSnapshot.child("Characters").child(temp).child("imageUrl").getValue(String.class);


                            if (name != null) {

                                swornMembersList.add(new RealmCharacterFormat(key,name, playedBy, gender, born, died, imageUrl));
                                adapter.notifyDataSetChanged();

                            }

                        } catch (Exception e) {

                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }

                adapter.notifyDataSetChanged();
                if (adapter.getItemCount() > 0) {
                    errorText.setVisibility(View.INVISIBLE);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Log.v("Error", "Firebase Error");

            }
        });


       Uri imageUri = Uri.parse("http://img1.rnkr-static.com/user_node_img/50025/1000492231/C350/rickard-stark-tv-characters-photo-u1.jpg");
       //image.setImageURI(imageUri);

        //URL Constants for JSON Parsing and data Retrieval from awoiaf MediaWiki page
        String CONSTANT_URL = "http://awoiaf.westeros.org/api.php?&action=query&format=json&prop=extracts&titles=";
        String url = CONSTANT_URL +  "House " + houseName;
        String urlTemp[] = url.split("of");
        url = urlTemp[0];

        OkHttpClient client = new OkHttpClient();

        final Request request = new Request.Builder()
                .url(url)
                .build();


        //Request 1 for retreival of wiki data
        client.newCall(request).enqueue(new Callback() {

                                            @Override
                                            public void onFailure(Call call, IOException e) {
                                                e.printStackTrace();

                                            }

                                            @Override
                                            public void onResponse(Call call, final Response response) throws IOException {

                                                if (!response.isSuccessful()) {

                                                    throw new IOException("Unexpected code " + response);

                                                } else {

                                                    //progress.setIndeterminate(true);

                                                    try {

                                                        final String responseData = response.body().string();
                                                        JSONObject query = new JSONObject(responseData);
                                                        Log.e("TAG",query+"");
                                                        JSONObject pages = query.getJSONObject("query").getJSONObject("pages");
                                                        Iterator<String> keyIterator = pages.keys();
                                                        String randomKey;
                                                        String temp = "";

                                                        if (keyIterator.hasNext()) {
                                                            randomKey = keyIterator.next();
                                                            temp = pages.getJSONObject(randomKey).getString("extract");
                                                        }

                                                        final String details = temp;


                                                        HouseInfo.this.runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                try {
                                                                    TextView myTextView = (TextView) findViewById(R.id.house_extract);
                                                                    String temp[] = details.split("<h2>References and Notes</h2>");
                                                                    //String temp1[] = temp[0].split("<h2>Family</h2>");
                                                                    myTextView.setText(Html.fromHtml(temp[0]));

                                                                    progress.setVisibility(View.INVISIBLE);

                                                                } catch (Exception e) {

                                                                    //progress.setVisibility(View.INVISIBLE);
                                                                    e.printStackTrace();
                                                                }
                                                            }
                                                        });
                                                    } catch (JSONException e) {

                                                        //progress.setVisibility(View.INVISIBLE);
                                                        e.printStackTrace();
                                                    }
                                                    // Run view-related code back on the main thread

                                                }
                                            }
                                        }

        );


    }

    public void setupWikiTextViews(String words,String currentLord,String region){

        if(region!=null)
        {
            regionTextView.setText(region);
        }
        if(currentLord!=null)
        {
            lordTextView.setText(currentLord);
        }
        if(words!=null)
        {
            wordsTextView.setText(words);
        }
    }
}