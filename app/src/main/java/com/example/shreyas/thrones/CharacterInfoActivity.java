package com.example.shreyas.thrones;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.StreamCorruptedException;
import java.util.Iterator;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CharacterInfoActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_info);

        String character = getIntent().getStringExtra("CharacterName");


        //Referencing Views
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(character);

        //Toolbar Stuff
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        //URL Constants for JSON Parsing and data Retrieval from awoiaf MediaWiki page
        String CONSTANT_URL = "http://awoiaf.westeros.org/api.php?&action=query&format=json&prop=extracts&titles=";
        String url = CONSTANT_URL + character;

        //Setting Up OkHttp to run a Asynchronous Service
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

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

                                                    try {
                                                        final String responseData = response.body().string();
                                                        JSONObject query = new JSONObject(responseData);
                                                        JSONObject pages = query.getJSONObject("query").getJSONObject("pages");
                                                        Iterator<String> keyIterator = pages.keys();
                                                        String randomKey;
                                                        String temp = "";

                                                        if (keyIterator.hasNext()) {
                                                            randomKey = keyIterator.next();
                                                            temp = pages.getJSONObject(randomKey).getString("extract");
                                                        }

                                                        final String details = temp;


                                                        CharacterInfoActivity.this.runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                try {
                                                                    TextView myTextView = (TextView) findViewById(R.id.character_extract);
                                                                    String temp[] = details.split("<h2>References and Notes</h2>");
                                                                    myTextView.setText(Html.fromHtml(temp[0]));
                                                                } catch (Exception e) {
                                                                    e.printStackTrace();
                                                                }
                                                            }
                                                        });
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                    // Run view-related code back on the main thread

                                                }
                                            }
                                        }

        );


    }


}
