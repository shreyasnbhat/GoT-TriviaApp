package com.example.shreyas.thrones;

import io.realm.RealmObject;

/**
 * Created by Shreyas on 1/22/2017.
 */

public class RealmString extends RealmObject {

    private String string;

    public RealmString()
    {

    }

    public RealmString(String string) {
        this.string = string;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }
}
