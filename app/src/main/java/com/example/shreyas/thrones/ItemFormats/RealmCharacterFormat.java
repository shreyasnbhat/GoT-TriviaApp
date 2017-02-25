package com.example.shreyas.thrones.ItemFormats;

import io.realm.Realm;
import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

/**
 * Created by shreyas on 24/2/17.
 */

public class RealmCharacterFormat extends RealmObject {

    @PrimaryKey
    String characterId="";

    @Index
    String name = "";

    String gender = "Unknown :p";
    String culture = "Not Found";
    String born = "Not Found";
    String died = "Not Found/Not Dead";
    String playedBy = "Not Found";
    String imageUrl = "https://upload.wikimedia.org/wikipedia/commons/7/7a/Kit_Harington.jpg";

    //Need Empty Constructor
    public RealmCharacterFormat() {

    }

    public RealmCharacterFormat(String name) {
        this.name = name;
    }

    public RealmCharacterFormat(String characterId,String name,String playedBy, String gender, String born, String died,  String imageUrl) {

        if (name.length() != 0 && name!=null)
            this.name = name;
        if (playedBy.length() != 0 && playedBy!=null)
            this.playedBy = playedBy;
        if (gender.length() != 0 && gender!=null)
            this.gender = gender;
        if (born.length() != 0)
            this.born = born;
        if (died.length() != 0)
            this.died = died;
        if(imageUrl!=null)
            this.imageUrl = imageUrl;
        this.characterId = characterId;
    }


    public String getCharacterId() {
        return characterId;
    }

    public void setCharacterId(String characterId) {
        this.characterId = characterId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCulture() {
        return culture;
    }

    public void setCulture(String culture) {
        this.culture = culture;
    }

    public String getBorn() {
        return born;
    }

    public void setBorn(String born) {
        this.born = born;
    }

    public String getDied() {
        return died;
    }

    public void setDied(String died) {
        this.died = died;
    }

    public String getPlayedBy() {
        return playedBy;
    }

    public void setPlayedBy(String playedBy) {
        this.playedBy = playedBy;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
