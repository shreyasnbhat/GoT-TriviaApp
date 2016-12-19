package com.example.shreyas.thrones;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.DraweeView;
import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.io.StreamCorruptedException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CharacterInfoActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView playedByText;
    private TextView genderText;
    private TextView bornText;
    private TextView diedText;
    private SimpleDraweeView image;
    private ProgressBar progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_character_info);

        //Getting Intent Extras from Adapter
        String character = getIntent().getStringExtra("CharacterName");
        String playedBy = getIntent().getStringExtra("playedBy");
        String died = getIntent().getStringExtra("died");
        String born = getIntent().getStringExtra("born");
        String gender = getIntent().getStringExtra("gender");

        Log.e("PLAYED BY",playedBy);
        Log.e("GENDER",gender);

        //Referencing Views
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        playedByText = (TextView)findViewById(R.id.playedBy_textView);
        genderText = (TextView)findViewById(R.id.gender_textView);
        diedText = (TextView)findViewById(R.id.died_textView);
        bornText = (TextView)findViewById(R.id.born_textView);
        progress = (ProgressBar)findViewById(R.id.progressbar);
        image = (SimpleDraweeView) findViewById(R.id.character_image);

        progress.getIndeterminateDrawable().setColorFilter(Color.parseColor("#FFFFFF"), android.graphics.PorterDuff.Mode.SRC_ATOP);

        if(character!=null)
        toolbar.setTitle(character);
        if(playedBy!=null)
        playedByText.setText(playedBy);
        if(gender!=null)
        genderText.setText(gender);
        if(born!=null)
        bornText.setText(born);
        if(died!=null)
        diedText.setText(died);

        //Toolbar Stuff
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        //URL Constants for JSON Parsing and data Retrieval from awoiaf MediaWiki page
        String CONSTANT_URL = "http://awoiaf.westeros.org/api.php?&action=query&format=json&prop=extracts&titles=";
        String url = CONSTANT_URL + character;
        //String IMAGE_URL = "https://commons.wikimedia.org/w/api.php?action=query&titles=File:";
        //IMAGE_URL = IMAGE_URL + playedBy + ".jpg&prop=imageinfo&iiprop=url";

        Uri imageUri = Uri.parse("https://upload.wikimedia.org/wikipedia/commons/7/7a/Kit_Harington.jpg");
        image.setImageURI(imageUri);

        //Setting Up OkHttp to run a Asynchronous Service
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

                                                    progress.setIndeterminate(true);

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
                                                                    String temp1[]  = temp[0].split("<h2>Family</h2>");
                                                                    myTextView.setText(Html.fromHtml(temp1[0]));

                                                                    progress.setVisibility(View.INVISIBLE);

                                                                } catch (Exception e) {
                                                                    e.printStackTrace();
                                                                }
                                                            }
                                                        });
                                                    } catch (JSONException e) {

                                                        progress.setVisibility(View.INVISIBLE);
                                                        e.printStackTrace();
                                                    }
                                                    // Run view-related code back on the main thread

                                                }
                                            }
                                        }

        );


    }


}
