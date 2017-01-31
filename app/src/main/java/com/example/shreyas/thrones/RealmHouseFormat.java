package com.example.shreyas.thrones;

import java.util.ArrayList;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Shreyas on 11/4/2016.
 */

public class RealmHouseFormat extends RealmObject{

    @PrimaryKey
    private String houseId="";

    private String name = "";
    private String region = "";
    private String coatOfArms = "";
    private String words = "";
    private RealmList<RealmString> titles = new RealmList<>();
    private String currentLord = "";
    private RealmList<RealmString> members = new RealmList<>();

    public void setCurrentLord(String currentLord) {
        this.currentLord = currentLord;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setCoatOfArms(String coatOfArms) {
        this.coatOfArms = coatOfArms;
    }

    public void setWords(String words) {
        this.words = words;
    }

    public void setTitles(RealmList<RealmString> titles) {
        this.titles = titles;
    }

    public void setMembers(RealmList<RealmString> members) {
        this.members = members;
    }



    public RealmHouseFormat() {}

    public RealmHouseFormat(String name) {

        this.name = name;
        this.region = " - ";
        this.currentLord=" - ";
        this.coatOfArms = " - ";
        this.words = " - ";
    }

    public RealmHouseFormat(String houseId,String name, String region, String coatOfArms, String words, RealmList<RealmString> titles, String currentLord, RealmList<RealmString> members) {

        this.houseId = houseId;
        this.name = name;
        this.region = region;
        this.coatOfArms = coatOfArms;
        this.words = words;
        this.titles = titles;
        this.currentLord = currentLord;
        this.members = members;
    }


    public String getName() {
        return name;
    }

    public String getRegion() {
        return region;
    }

    public String getCoatOfArms() {
        return coatOfArms;
    }

    public String getWords() {
        return words;
    }

    public RealmList<RealmString> getTitles() {
        return titles;
    }

    public String getCurrentLord() {
        return currentLord;
    }

    public RealmList<RealmString> getMembers() {
        return members;
    }

    public String getHouseId() {
        return houseId;
    }

    public void setHouseId(String houseId) {
        this.houseId = houseId;
    }
}
