package com.example.shreyas.thrones;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Shreyas on 11/4/2016.
 */

public class Firebase extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    /* Enable disk persistence  */
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

    }
}

